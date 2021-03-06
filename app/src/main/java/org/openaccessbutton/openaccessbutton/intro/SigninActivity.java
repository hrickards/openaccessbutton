package org.openaccessbutton.openaccessbutton.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.api.API;
import org.openaccessbutton.openaccessbutton.menu.MenuActivity;
import org.openaccessbutton.openaccessbutton.preferences.AppPreferencesActivity;

public class SigninActivity extends Activity {
    // Animation speed
    public final static double ANIMATION_DP_MS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button loginButton = (Button) findViewById(R.id.signinButton);

        final Context context = this;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = ((EditText) findViewById(R.id.signInUsername)).getText().toString();
                final String password = ((EditText) findViewById(R.id.signInPassword)).getText().toString();
                API.signinRequest(new API.SigninCallback() {
                    @Override
                    public void onComplete(String username, String apikey) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Go to MenuActivity
                                Intent k = new Intent(context, MenuActivity.class);
                                startActivity(k);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(final String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }, context, username, password);
            }
        });

        // Forgotten password button
        TextView forgottenButton = (TextView) findViewById(R.id.forgotPasswordButton);
        forgottenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to ForgotPasswordActivity
                Intent k = new Intent(context, ForgotPasswordActivity.class);
                startActivity(k);
                finish();
            }
        });
    }
}
