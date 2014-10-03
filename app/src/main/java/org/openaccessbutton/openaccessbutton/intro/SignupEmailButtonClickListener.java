package org.openaccessbutton.openaccessbutton.intro;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import org.openaccessbutton.openaccessbutton.menu.MenuActivity;

public class SignupEmailButtonClickListener implements Button.OnClickListener {
    Context mContext;

    public SignupEmailButtonClickListener(Context context) {
        mContext = context;
    }

    @Override public void onClick(View v) {
        // Launch MenuActivity
        Intent k = new Intent(mContext, MenuActivity.class);
        mContext.startActivity(k);
    }
}