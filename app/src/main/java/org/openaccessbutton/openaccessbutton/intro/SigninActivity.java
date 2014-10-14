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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.api.API;
import org.openaccessbutton.openaccessbutton.menu.MenuActivity;

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
                API.signinRequest(new API.SigninCallback() {
                    @Override
                    public void onComplete(String username, String apikey) {
                        // Go to MenuActivity
                        Intent k = new Intent(context, MenuActivity.class);
                        startActivity(k);
                        finish();
                    }
                });
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

        // Show the social login buttons
        TextView socialButtonShow = (TextView) findViewById(R.id.socialSignInButton);
        socialButtonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View v = findViewById(R.id.socialSignInButtons);

                // Animate showing the social sign in buttons
                // Copied from Tom Esterez @ http://stackoverflow.com/questions/4946295
                v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                final int targtetHeight = v.getMeasuredHeight();
                v.getLayoutParams().height = 0;
                v.setVisibility(View.VISIBLE);
                Animation a = new Animation()
                {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        v.getLayoutParams().height = interpolatedTime == 1
                                ? LinearLayout.LayoutParams.WRAP_CONTENT
                                : (int)(targtetHeight * interpolatedTime);
                        v.requestLayout();
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };
                a.setDuration((int)(ANIMATION_DP_MS*targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
                findViewById(R.id.socialSignInButton).setVisibility(View.GONE);
                v.startAnimation(a);
            }
        });


        // Social signin buttons
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
        getMenuInflater().inflate(R.menu.signin, menu);
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
