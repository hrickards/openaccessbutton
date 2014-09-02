package org.openaccessbutton.openaccessbutton.intro;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import com.viewpagerindicator.CirclePageIndicator;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;

/**
 * Take the user through some introductory pages they swipe through before launching
 * the app for the first time
 */
public class IntroActivity extends Activity {
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // Circle pager indicator to show the user visually they can swipe between pages
        CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.circles);
        pageIndicator.setViewPager(mPager);

        /*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", true)) {
            // If we're not on the first run skip straight to the main activity
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();

            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
            finish();
        }
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return ScreenSlidePageFragment.numberPages(getApplicationContext());
        }
    }
}
