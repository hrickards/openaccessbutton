package org.openaccessbutton.openaccessbutton.intro;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.R;

public class ForgotPasswordActivity extends Activity {
    // Animation speed
    public final static double ANIMATION_DP_MS = 0.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Submit the forgotten password form and show the user a message
        TextView socialButtonShow = (TextView) findViewById(R.id.forgotPasswordSubmit);
        socialButtonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Actually submit the form to the API

                // Hide the keyboard
                EditText myEditText = (EditText) findViewById(R.id.forgotPasswordEmail);
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);

                // Show the form submission message
                final View v = findViewById(R.id.forgotPasswordSubmittedText);
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
                findViewById(R.id.forgotPasswordSubmit).setVisibility(View.GONE);
                v.startAnimation(a);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.forgot_password, menu);
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
