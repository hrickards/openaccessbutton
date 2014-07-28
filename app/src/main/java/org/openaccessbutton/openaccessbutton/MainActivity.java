/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.openaccessbutton.openaccessbutton.advocacy.AdvocacyFragment;
/**
 * Wrapper activity that provides navigation for the entire app, and loads the relevant fragment
 * based on that navigation.
 */

public class MainActivity extends Activity {
    // Navigation drawer
    private String[] mNavigationTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Position of the various fragments in the navigation drawer
    // TODO: Can this be done programatically from arrays.xml?
    private static final int sNavigationAdvocacy = 0;
    private static final int sNavigationBlog = 1;
    private static final int sNavigationBrowser = 2;
    private static final int sNavigationMap = 3;
    private static final int sDefaultFragment = sNavigationAdvocacy;

    private Fragment mAdvocacyFragment;
    private Fragment mBlogFragment;
    private Fragment mBrowserFragment;
    private Fragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseNavigation();

        // Show the default fragment
        switchToFragment(sDefaultFragment);
    }

    /**
     * Setup (bind various handlers) the navigation drawer on the left hand side of the screen
     * that enables the user to switch between activities.
     */
    private void initialiseNavigation() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Swap the fragments when an item is selected
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Populate the list of places
        mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
                mNavigationTitles));

        // Tie in navigation drawer to action bar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    /**
     * When a navigation item is selected, load and show the corresponding fragment.
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switchToFragment(position);
        }
    }

    /**
     * Load and show the corresponding fragment, updating the UI of the navigation drawer at the
     * same time.
     */
    protected void switchToFragment(int position) {
        // Switch to the new fragment, instantiating it if a previous instance isn't around
        Fragment mFragment;
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case sNavigationAdvocacy:
                if (mAdvocacyFragment == null) {
                    mAdvocacyFragment = new AdvocacyFragment();
                }
                mFragment = mAdvocacyFragment;
                break;

            case sNavigationBlog:
                if (mBlogFragment == null) {
                    mBlogFragment = new BlogFragment();
                }
                mFragment = mBlogFragment;
                break;

            case sNavigationBrowser:
                if (mBrowserFragment == null) {
                    mBrowserFragment = new BrowserFragment();
                }
                mFragment = mBrowserFragment;
                break;

            case sNavigationMap:
                if (mMapFragment == null) {
                    mMapFragment = new MapFragment();
                }
                mFragment = mMapFragment;
                break;

            default:
                // TODO: Look at sDefaultFragment
                mFragment = new AdvocacyFragment();
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mNavigationTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    // Called when an item in the action bar is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // TODO: Handle other action bar presses

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
