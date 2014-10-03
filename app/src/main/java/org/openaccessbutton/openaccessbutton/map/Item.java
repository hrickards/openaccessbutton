package org.openaccessbutton.openaccessbutton.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.openaccessbutton.openaccessbutton.R;

/**
 * A marker on the map representing someone trying to access a paywalled article
 */
public class Item implements ClusterItem {
    public LatLng mPosition;
    public String mStory;
    public String mDoi;
    public String mUserProfession;
    public String mUrl;
    public String mAccessed;
    public String mUserName;
    public String mDescription;
    public int mIcon;

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

        // Very pseudo random approach for choosing between the different possible icons,
        // but for our purposes it works well enough
        switch (mStory.length() % 4) {
            case 0:
                mIcon = R.drawable.ic_launcher;
                break;

            case 1:
                mIcon = R.drawable.ic_launcher_old;
                break;

            case 2:
                mIcon = R.drawable.ic_drawer;
                break;

            default:
                mIcon = R.drawable.ic_drawer_dark;
                break;
        }
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String name() {
        return mUserName + " (" + mUserProfession + ")";
    }
}