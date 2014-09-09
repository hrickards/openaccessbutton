package org.openaccessbutton.openaccessbutton.intro;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.openaccessbutton.openaccessbutton.MainActivity;

public class SignupEmailButtonClickListener implements Button.OnClickListener {
    Context mContext;

    public SignupEmailButtonClickListener(Context context) {
        mContext = context;
    }

    @Override public void onClick(View v) {
        // Launch SignupActivity
        Intent k = new Intent(mContext, SignupActivity.class);
        mContext.startActivity(k);
    }
}