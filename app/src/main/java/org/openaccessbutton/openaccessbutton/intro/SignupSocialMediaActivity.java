package org.openaccessbutton.openaccessbutton.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.preferences.AppPreferencesActivity;

public class SignupSocialMediaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_social_media);

        TextView googleButton = (TextView) findViewById(R.id.signupGoogleButton);
        googleButton.setOnClickListener(new SignUpSocialMediaClickListener(this, "google"));
        TextView facebookButton = (TextView) findViewById(R.id.signupFacebookButton);
        facebookButton.setOnClickListener(new SignUpSocialMediaClickListener(this, "facebook"));
        TextView twitterButton = (TextView) findViewById(R.id.signupTwitterButton);
        twitterButton.setOnClickListener(new SignUpSocialMediaClickListener(this, "twitter"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup_social_media, menu);
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
