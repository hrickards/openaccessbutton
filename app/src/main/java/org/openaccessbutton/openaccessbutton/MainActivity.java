/*
 * Copyright (C) 2014 Open Access Button
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;

import org.openaccessbutton.openaccessbutton.advocacy.QuestionsActivity;
import org.openaccessbutton.openaccessbutton.intro.SignupActivity;
import org.openaccessbutton.openaccessbutton.preferences.AppPreferencesActivity;
import org.openaccessbutton.openaccessbutton.push.Push;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper activity that provides navigation for the entire app, and loads the relevant fragment
 * based on that navigation.
 */
public class MainActivity extends Activity implements OnFragmentNeededListener,
        FragmentManager.OnBackStackChangedListener, OnShareIntentInterface {
    // Navigation drawer
    private String[] mNavigationTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    //private SearchView mSearchView;

    private NavigationXmlParser mNavigationParser;
    private Fragment[] mFragments;
    private Fragment mFragment;

    private ShareActionProvider mShareActionProvider;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseNavigation();
        Push.initialisePushNotifications(this);

        // We might need to launch a specific fragment passed in via the intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (savedInstanceState != null) {
            // Restore the fragment's instance
            mFragment = getFragmentManager().getFragment(savedInstanceState, "mFragment");
        } else if (extras != null && extras.containsKey("fragmentNo")) {
            // Launch the specified fragment
            int fragmentNo = extras.getInt("fragmentNo");
            switchToFragment(fragmentNo);
        } else {
            // Show the default fragment
            switchToFragment(0);
        }
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

        // Parse the navigation drawer XML
        mNavigationParser = new NavigationXmlParser();
        try {
            InputStream object = this.getResources().openRawResource(R.raw.navigation);
            mNavigationParser.parse(object);
        } catch (IOException e) {
            // TODO: Do something
            Log.e("openaccess", "exception", e);
        } catch (XmlPullParserException e) {
            // TODO: Do something
            Log.e("openaccess", "exception", e);
        }

        // Obtain an array of the navigation titles
        mNavigationTitles = mNavigationParser.getTitles();
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
                mNavigationTitles));

        // Initialise mFragments to the right length
        mFragments = new Fragment[mNavigationParser.size()];

        // Tie in navigation drawer to action bar, switching between up caret and menu
        // caret depending on how deep in the fragment stack we are
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                setActionBarArrowDependingOnFragmentsBackStack();
            }

            public void onDrawerOpened(View drawerView) {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getFragmentManager().addOnBackStackChangedListener(this);
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
        FragmentManager fragmentManager = getFragmentManager();

        // Find a Fragment of the right class
        NavigationItem item = mNavigationParser.get(position);
        if (mFragments[position] == null) {
            // This is safe because:
            // - Hardcoded XML: *never* do this from any network-downloaded or user-editable source
            // - All our fragment constructors need no arguments
            try {
                Class<?> fragmentClass = Class.forName(item.className);
                mFragments[position] = (Fragment) fragmentClass.newInstance();
            } catch (ClassNotFoundException e) {
                mFragments[position] = null;
                Log.e("openaccess", "exception", e);
            } catch (IllegalAccessException e) {
                mFragments[position] = null;
                Log.e("openaccess", "exception", e);
            } catch (InstantiationException e) {
                mFragments[position] = null;
                Log.e("openaccess", "exception", e);
            }
        }
        mFragment = mFragments[position];

        fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment, "mFragment").commit();
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
        // If the navigation drawer toggle is enabled, let that handle the click
        if (mDrawerToggle.isDrawerIndicatorEnabled() && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        // Otherwise go back up the fragment stack
        } else if (item.getItemId() == android.R.id.home && getFragmentManager().popBackStackImmediate()) {
            return true;
        // Otherwise if the user wants to see answers to the questions they've asked
        } else if (item.getItemId() == R.id.action_questions) {
            // Open up QuestionsActivity for them to do that
            Intent k = new Intent(this, QuestionsActivity.class);
            startActivity(k);
            return true;
        // Otherwise if the logout button was pressed
        } else if (item.getItemId() == R.id.action_logout) {
            // Remove the api key from the SharedPreferences indicating no user's logged in
            SharedPreferences prefs = getSharedPreferences("org.openaccessbutton.openaccessbutton", 0);
            prefs.edit().remove("api_key").apply();

            // Go back to SignupActivity
            Intent k = new Intent(this, SignupActivity.class);
            startActivity(k);
            finish();

            return true;
        // Otherwise if the settings button was pressed
        } else if (item.getItemId() == R.id.action_settings) {
            // Open up AppPreferencesActivity
            Intent k = new Intent(this, AppPreferencesActivity.class);
            startActivity(k);
            return true;
        // Otherwise let Android handle the default behaviour
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;

        // Setup search
        /*
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // TODO Do: something with this:
                Log.w("oab", "Search: " + s);
                return false;
            }
        });
        */

        // Setup sharing
        Intent standardShareIntent = new Intent();
        standardShareIntent.setAction(Intent.ACTION_SEND);
        standardShareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.generic_share_message));
        standardShareIntent.setType("text/plain");

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        mShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        mShareActionProvider.setShareIntent(standardShareIntent);

        return super.onCreateOptionsMenu(menu);
    }

    /*protected void updateShareIntent() {
        if (mMenu != null) {
            MenuItem shareItem = mMenu.findItem(R.id.action_share);
            mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
            mShareActionProvider.setShareIntent(createShareIntent());
        }
    }*/

    public void onShareIntentUpdated(Intent intent) {
        if (mMenu != null) {
            MenuItem shareItem = mMenu.findItem(R.id.action_share);
            mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
            mShareActionProvider.setShareIntent(intent);
        }
    }

    /*protected Intent createShareIntent() {
        return createShareIntent(mFragment);
    }*/

    /*protected Intent createShareIntent(Fragment fragment) {
        Intent shareIntent;
        if (mFragment instanceof OnShareButtonInterface) {
            // Fragments might not be bound yet so
            shareIntent = ((OnShareButtonInterface) fragment).onShareButtonPressed(getResources());
        } else {
            shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.generic_share_message));
            shareIntent.setType("text/plain");
        }

        return shareIntent;
    }*/

    public void onBackStackChanged() {
        setActionBarArrowDependingOnFragmentsBackStack();
    }

    /**
     * Switch between navigation-open-button and back-button in action bar depending on how
     * deep the fragment stack is
     */
    private void setActionBarArrowDependingOnFragmentsBackStack() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        mDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }

    // Override back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // If current fragment implements OnBackButtonInterface
            if ((mFragment instanceof OnBackButtonInterface) && ((OnBackButtonInterface) mFragment).onBackButtonPressed()) {
                return true;
            //} else if (mSearchView.isIconified() != true) {
            //    mSearchView.setIconified(true);
            } else {
                super.onKeyDown(keyCode, event);
            }
        }

        return true;
    }

    public void launchFragment(Fragment fragment, String tag, Bundle data, boolean backstack) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, tag);
        if (backstack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    public interface OnBackButtonInterface {
        // If back handled return true, otherwise return false
        public boolean onBackButtonPressed();
    }

    /*public interface OnShareButtonInterface {
        public Intent onShareButtonPressed(Resources resources);
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getFragmentManager().putFragment(outState, "mFragment", mFragment);
    }
}

