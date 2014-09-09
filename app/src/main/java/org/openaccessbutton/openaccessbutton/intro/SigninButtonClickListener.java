package org.openaccessbutton.openaccessbutton.intro;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class SigninButtonClickListener implements Button.OnClickListener {
    Context mContext;

    public SigninButtonClickListener(Context context) {
        mContext = context;
    }

    @Override public void onClick(View v) {
        // Launch SigninActivity
        Intent k = new Intent(mContext, SigninActivity.class);
        mContext.startActivity(k);
    }
}