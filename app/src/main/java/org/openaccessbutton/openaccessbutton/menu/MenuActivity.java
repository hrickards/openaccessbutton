package org.openaccessbutton.openaccessbutton.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.about.AboutActivity;
import org.openaccessbutton.openaccessbutton.advocacy.QuestionsActivity;
import org.openaccessbutton.openaccessbutton.intro.LaunchActivity;
import org.openaccessbutton.openaccessbutton.intro.SignupActivity;
import org.openaccessbutton.openaccessbutton.preferences.AppPreferencesActivity;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Context context = this;

        // 0 -- Information
        // 1 -- FAQ
        // 2 - Blog
        // 3 - Browser
        // 4 - Map
        // 5 - Take Action
        // 6 - Support Diego

        // Do research
        findViewById(R.id.doResearchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", 3);
                startActivity(k);
            }
        });

        // Information
        findViewById(R.id.infoHubButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", 0);
                startActivity(k);
            }
        });
        findViewById(R.id.otherMaterialsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", 0);
                startActivity(k);
            }
        });

        // FAQ
        findViewById(R.id.interactiveFaqButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", 1);
                startActivity(k);
            }
        });

        // Map
        findViewById(R.id.mapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", 4);
                startActivity(k);
            }
        });

        // Blog
        findViewById(R.id.moreOnAppButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", 2);
                startActivity(k);
            }
        });

        // Take action
        findViewById(R.id.takeActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", 5);
                startActivity(k);
            }
        });
        findViewById(R.id.supportDiegoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", 6);
                startActivity(k);
            }
        });
        findViewById(R.id.becomeExpertButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", 5);
                startActivity(k);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
        } else if (item.getItemId() == R.id.action_logout) {
            // Remove the api key from the SharedPreferences indicating no user's logged in
            SharedPreferences prefs = getSharedPreferences("org.openaccessbutton.openaccessbutton", 0);
            prefs.edit().remove("api_key").apply();

            // Go back to LaunchActivity
            Intent k = new Intent(this, LaunchActivity.class);
            startActivity(k);
            finish();

            return true;
        } else if (item.getItemId() == R.id.action_about) {
            // Open up AboutActivity
            Intent k = new Intent(this, AboutActivity.class);
            startActivity(k);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
