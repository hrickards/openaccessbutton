package org.openaccessbutton.openaccessbutton.about;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.R;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        TextView aboutText = (TextView) findViewById(R.id.aboutText);
        aboutText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
