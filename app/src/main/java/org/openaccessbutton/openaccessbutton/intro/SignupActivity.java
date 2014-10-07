package org.openaccessbutton.openaccessbutton.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.goebl.david.Webb;

import org.json.JSONObject;
import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.advocacy.QuestionsActivity;

public class SignupActivity extends Activity {
    private static final String REGISTER_API_URL = "http://oabutton.cottagelabs.com/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Check if the user's already signed in
        SharedPreferences prefs = getSharedPreferences("org.openaccessbutton.openaccessbutton", 0);
        String apiKey = prefs.getString("api_key", "");
        if (apiKey.length() > 0) {
            // Go to MainActivity
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
            finish();
        }

        Spinner spinner = (Spinner) findViewById(R.id.signupProfession);
        final ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.job, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int textColor;
                if (i == 0) {
                    textColor = Color.parseColor("#DDDDDD");
                } else {
                    textColor = Color.WHITE;
                }
                ((TextView) adapterView.getChildAt(0)).setTextColor(textColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Bind signin button
        TextView signinButton = (TextView) findViewById(R.id.globalSigninButton);
        signinButton.setOnClickListener(new SigninButtonClickListener(this));

        // Bind signupSocialMedia button
        TextView signupSocialMediaButton = (TextView) findViewById(R.id.globalSignUpSocialMedia);
        final Context context = this;
        signupSocialMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch SignupSocialMediaActivity activity
                Intent k = new Intent(context, SignupSocialMediaActivity.class);
                context.startActivity(k);
            }
        });

        // Bind listener to signup button
        final Button submitButton = (Button) findViewById(R.id.signupButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = ((EditText) findViewById(R.id.signUpEmail)).getText().toString();
                String professionT = ((Spinner) findViewById(R.id.signupProfession)).getSelectedItem().toString();
                if (professionT.equals(getResources().getStringArray(R.array.job)[0])) {
                    professionT = "";
                }
                final String profession = professionT;
                // Use email as username for now (API does this if we don't send a username value)
                final String name = ((EditText) findViewById(R.id.signUpName)).getText().toString();
                final String password = ((EditText) findViewById(R.id.signUpPassword)).getText().toString();
                final String passwordConfirmation = ((EditText) findViewById(R.id.signUpRetypePassword)).getText().toString();

                if (!password.equals(passwordConfirmation)) {
                    Toast.makeText(context, getResources().getString(R.string.passwords_not_matching), Toast.LENGTH_SHORT).show();
                    return;
                }

                submitButton.setEnabled(false);

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
                            edit.apply();


                            // Go to IntroActivity
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    submitButton.setEnabled(true);
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
                                    submitButton.setEnabled(true);
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
