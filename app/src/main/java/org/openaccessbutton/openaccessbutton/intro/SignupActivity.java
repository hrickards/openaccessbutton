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
import org.openaccessbutton.openaccessbutton.api.API;
import org.openaccessbutton.openaccessbutton.menu.MenuActivity;
import org.openaccessbutton.openaccessbutton.preferences.AppPreferencesActivity;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

public class SignupActivity extends Activity {
    private static final String REGISTER_API_URL = "http://oabutton.cottagelabs.com/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Spinner spinner = (Spinner) findViewById(R.id.signupProfession);
        final ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.job_titles, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int textColor;
                if (i == 0) {
                    textColor = Color.parseColor("#888888");
                } else {
                    textColor = Color.parseColor("#212F3F");
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
        final Context context = this;

        // Bind listener to signup button
        final Button submitButton = (Button) findViewById(R.id.signupButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = ((EditText) findViewById(R.id.signUpEmail)).getText().toString();
                String professionT = ((Spinner) findViewById(R.id.signupProfession)).getSelectedItem().toString();
                if (professionT.equals(getResources().getStringArray(R.array.job_titles)[0])) {
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
                if (email.length() == 0) {
                    Toast.makeText(context, getResources().getString(R.string.blank_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() == 0) {
                    Toast.makeText(context, getResources().getString(R.string.blank_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                submitButton.setEnabled(false);

                // Create account using OAB API
                            API.signupRequest(new API.SignupCallback() {
                                @Override
                                public void onComplete(String username, String apikey) {
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

                                @Override
                                public void onError(final String message) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            submitButton.setEnabled(true);
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }, context, email, profession, name, password);

                    }


        });

        checkForUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }

    private void checkForCrashes() {
        CrashManager.register(this, "5adca98a3d144ef911ae72f7cc87e793");
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this, "5adca98a3d144ef911ae72f7cc87e793");
    }
}
