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
}
