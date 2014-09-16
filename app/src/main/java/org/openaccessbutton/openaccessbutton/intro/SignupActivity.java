package org.openaccessbutton.openaccessbutton.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.goebl.david.Webb;

import org.json.JSONObject;
import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;

public class SignupActivity extends Activity {
    private static final String REGISTER_API_URL = "http://oabutton.cottagelabs.com/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Bind listener to signup button
        Button submitButton = (Button) findViewById(R.id.signupButton);
        final Context context = this;
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = ((EditText) findViewById(R.id.signUpEmail)).getText().toString();
                final String profession = ((Spinner) findViewById(R.id.signupProfession)).getSelectedItem().toString();
                // Use email as username for now (API does this if we don't send a username value)
                final String name = ((EditText) findViewById(R.id.signUpName)).getText().toString();
                final String password = ((EditText) findViewById(R.id.signUpPassword)).getText().toString();

                // Create account using OAB API
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            // Register using API
                            Webb webb = Webb.create();
                            JSONObject result = webb
                                    .post(REGISTER_API_URL)
                                    .param("email", email)
                                    .param("profession", profession)
                                    .param("name", name)
                                    .param("password", password)
                                    .ensureSuccess()
                                    .asJsonObject()
                                    .getBody();
                            String apiKey = result.getString("api_key");

                            // Store API key so we know we're authenticated (and skip intro pages)
                            // and also for making API requests
                            SharedPreferences prefs = context.getSharedPreferences("org.openaccessbutton.openaccessbutton", MODE_PRIVATE);
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.clear();
                            edit.putString("api_key", apiKey);
                            edit.commit();


                            // Go to IntroActivity
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent k = new Intent(context, IntroActivity.class);
                                    startActivity(k);
                                    finish();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();

                            // TODO Really we should show an error message to the user, however
                            // to test navigation logic while the API is broken we'll just go
                            // straight into the MainActivity

                            // Go to IntroActivity
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent k = new Intent(context, IntroActivity.class);
                                    startActivity(k);
                                    finish();
                                }
                            });
                        }
                    }
                });

                thread.start();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
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
