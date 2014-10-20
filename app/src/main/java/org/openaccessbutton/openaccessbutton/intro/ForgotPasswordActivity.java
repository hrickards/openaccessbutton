package org.openaccessbutton.openaccessbutton.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import org.openaccessbutton.openaccessbutton.api.API;
import org.openaccessbutton.openaccessbutton.preferences.AppPreferencesActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
                try {
                    // Open forgot password form in browser
                    String email = ((EditText) findViewById(R.id.forgotPasswordEmail)).getText().toString();
                    String url = "http://www.openaccessbutton.org/forgot_password?email=" + URLEncoder.encode(email, "UTF-8");

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
