/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.OnShareIntentInterface;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.api.API;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Shows paywalled journal requests, just like the map on openaccessbutton.org.
 */
public class MapFragment extends Fragment {
    private MapView m;
    private ClusterManager<Item> mClusterManager;
    private Item mClickedClusterItem;
    private Map<String, Item> mMarkers;  // For unique markers
    private final String WEB_MAP_URL = "http://openaccessbutton.org";
    private OnShareIntentInterface mCallback;

    public MapFragment() {
        // Required empty public constructor
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnShareIntentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnShareIntentInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        m = (MapView) v.findViewById(R.id.map_view);
        m.onCreate(savedInstanceState);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupMap();
    }

    class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoContents(Marker marker) {
            LayoutInflater li = getActivity().getLayoutInflater();
            View view = li.inflate(R.layout.map_info_window, null);

            TextView name = (TextView) view.findViewById(R.id.map_item_name);
            TextView story = (TextView) view.findViewById(R.id.map_item_story);
            TextView description = (TextView) view.findViewById(R.id.map_item_description);
            name.setText(mClickedClusterItem.name());
            story.setText(mClickedClusterItem.mStory);
            description.setText(mClickedClusterItem.mDescription);

            return view;
        }

        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }
    }

    public void setupMap() {
        MapsInitializer.initialize(getActivity());
        mMarkers = new HashMap<String, Item>();

        // Auto center map
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // Based on http://stackoverflow.com/questions/17668917
                LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = lm.getProviders(true);
                Location location = null;

                // Try every possible provider
                for (int i=providers.size()-1; i>=0; i--) {
                    location = lm.getLastKnownLocation(providers.get(i));
                    if (location != null) break;
                }

                final LatLng center;
                if (location == null) {
                    center = new LatLng(51.503186, -0.126446);
                } else {
                    center = new LatLng(location.getLatitude(), location.getLongitude());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(center, 10));
                    }
                });
            }
        };
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sp.getBoolean("location", true)) {
            (new Thread(r)).start();
        }

        // Setup clustering
        mClusterManager = new ClusterManager<Item>(getActivity(), m.getMap());
        mClusterManager.setRenderer(new ItemRenderer());
        m.getMap().setOnCameraChangeListener(mClusterManager);
        m.getMap().setOnMarkerClickListener(mClusterManager);

        // Setup info windows
        m.getMap().setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MarkerInfoWindowAdapter());
        m.getMap().setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Item>() {
            @Override
            public boolean onClusterClick(Cluster<Item> cluster) {
                float newZoomLevel = m.getMap().getCameraPosition().zoom + 1;
                if (newZoomLevel < m.getMap().getMinZoomLevel()) {
                    newZoomLevel = m.getMap().getMinZoomLevel();
                } else if (newZoomLevel > m.getMap().getMaxZoomLevel()) {
                    newZoomLevel = m.getMap().getMaxZoomLevel();
                }
                // TODO: Why does this not zoom when we use animateCamera()?
                m.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), newZoomLevel));
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Item>() {
            @Override
            public boolean onClusterItemClick(Item item) {
                mClickedClusterItem = item;
                return false;
            }
        });

        API.storyListRequest(new API.StoryListCallback() {
            @Override
            public void onComplete(final Item[] stories) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i=0; i<stories.length; i++) {
                            addItem(stories[i]);
                        }
                    }
                });
            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        m.onResume();
        updateShareIntent();
    }

    @Override
    public void onPause() {
        super.onPause();
        m.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        m.onLowMemory();
    }

    // Implement a custom Cluster renderer so we can use our own icons for the marker pins
    // Based upon android-maps-utils CustomMarkerClusteringDemoActivity
    public class ItemRenderer extends DefaultClusterRenderer<Item> {
        private final IconGenerator mIconGenerator = new IconGenerator(getActivity().getApplicationContext());
        private final ImageView mImageView;
        private final int mDimension;

        public ItemRenderer() {
            super(getActivity().getApplicationContext(), m.getMap(), mClusterManager);

            mImageView = new ImageView(getActivity().getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.map_marker_width);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.map_marker_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Item item, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(item.mIcon);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.toString());
        }
    }


    private void addItem(Item item) {
        // Don't add unique markers
        String key = item.mPosition.latitude + "," + item.mPosition.longitude;

        // Move the marker by up to 10m if it's got exactly the same lat/lng as another one
        if (mMarkers.containsKey(key)) {
            // DEGREE APPROXIMATIONS
            // 1 latitude degree approx = 111,111m
            // 1 longitude degree approx = 111,111*cos(latitude)
            double latitudeMaxOffset = 10.0/111111.0;
            double longitudeMaxOffset = 10.0/(111111.0*Math.cos(Math.toRadians((item.mPosition.latitude))));

            Random generator = new Random();
            double latitudeOffset = latitudeMaxOffset * generator.nextDouble();
            double longitudeOffset = longitudeMaxOffset * generator.nextDouble();

            LatLng newPosition = new LatLng(item.mPosition.latitude + latitudeOffset, item.mPosition.longitude + longitudeOffset);
            item.mPosition = newPosition;
            addItem(item);
        } else {
            mMarkers.put(key, item);
            mClusterManager.addItem(item);
        }

    }

    public Intent onShareButtonPressed(Resources resources) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.map_share_text));
        shareIntent.putExtra(Intent.EXTRA_TEXT, WEB_MAP_URL);
        shareIntent.setType("text/plain");

        return shareIntent;
    }

    public void updateShareIntent() {
        Intent shareIntent = onShareButtonPressed(getResources());
        mCallback.onShareIntentUpdated(shareIntent);
    }
}
