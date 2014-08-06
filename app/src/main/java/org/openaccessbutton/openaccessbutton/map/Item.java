package org.openaccessbutton.openaccessbutton.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by harry on 05/08/14.
 */
public class Item implements ClusterItem {
    private final LatLng mPosition;
    public String mStory;
    public String mDoi;
    public String mUserProfession;
    public String mUrl;
    public String mAccessed;
    public String mUserName;
    public String mDescription;

    public Item(double lat, double lng, String story, String doi, String userProfession,
                String url, String accesssed, String userName, String description) {
        mPosition = new LatLng(lat, lng);
        mStory = story;
        mDoi = doi;
        mUserProfession = userProfession;
        mUrl = url;
        mAccessed = accesssed;
        mUserName = userName;
        mDescription = description;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}