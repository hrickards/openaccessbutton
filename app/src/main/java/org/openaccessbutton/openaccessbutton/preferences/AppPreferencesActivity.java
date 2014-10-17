package org.openaccessbutton.openaccessbutton.preferences;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import org.openaccessbutton.openaccessbutton.R;

/**
 * Created by rickards on 10/14/14.
 */
public class AppPreferencesActivity extends PreferenceActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new AppPreferenceFragment()).commit();
        //Sets the background color to navy
        findViewById(android.R.id.list).setBackgroundColor(Color.parseColor("#212F3F"));
    }

    // Internal fragment copied from http://stackoverflow.com/questions/6822319
    public static class AppPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
