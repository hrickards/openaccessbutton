package org.openaccessbutton.openaccessbutton.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.menu.MenuActivity;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // Check if the user's already signed in
        SharedPreferences prefs = getSharedPreferences("org.openaccessbutton.openaccessbutton", 0);
        String apiKey = prefs.getString("api_key", "");
        if (apiKey.length() > 0) {
            // Go to MenuActivity
            Intent k = new Intent(this, MenuActivity.class);
            startActivity(k);
            finish();
        }

        // Make privacy links clickable
        TextView privacyView = (TextView) findViewById(R.id.privacy_text);
        privacyView.setMovementMethod(LinkMovementMethod.getInstance());

        // Set button callbacks
        final Context context = this;
        findViewById(R.id.signinButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, SigninActivity.class);
                startActivity(k);
            }
        });
        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, SignupActivity.class);
                startActivity(k);
            }
        });
        /*findViewById(R.id.signupWithSocialMediaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, SignupSocialMediaActivity.class);
                startActivity(k);
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launch, menu);
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
}
