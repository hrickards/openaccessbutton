package org.openaccessbutton.openaccessbutton.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.api.API;
import org.openaccessbutton.openaccessbutton.preferences.AppPreferencesActivity;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;

public class OAuthActivity extends Activity {
    private static final String OAUTHIO_PUBLIC_KEY = "7NPsX6We9DN_PC797RFmaf7EooE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        // Get provider name from the parameters passed via the intent
        Intent intent = getIntent();
        String provider = intent.getStringExtra("provider");

        // Initialise oauth.io library
        final OAuth oauth = new OAuth(this);
        oauth.initialize(OAUTHIO_PUBLIC_KEY);

        final Context context = this;
        oauth.popup(provider, new OAuthCallback() {
            @Override
            public void onFinished(OAuthData oAuthData) {
                if (oAuthData.status.equals("success")) {
                    API.oauthSignupRequest(new API.OAuthSignupCallback() {
                        @Override
                        public void onComplete(String username, String apikey) {
                            // Go to IntroActivity
                            Intent k = new Intent(context, IntroActivity.class);
                            startActivity(k);
                            finish();
                        }
                    });
                } else {
                    // Go back to activity that launched us
                    onBackPressed();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.oauth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Open up AppPreferencesActivity
            Intent k = new Intent(this, AppPreferencesActivity.class);
            startActivity(k);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
