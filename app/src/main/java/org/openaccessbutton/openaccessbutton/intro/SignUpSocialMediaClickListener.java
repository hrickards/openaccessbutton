package org.openaccessbutton.openaccessbutton.intro;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class SignUpSocialMediaClickListener implements Button.OnClickListener{
    Context mContext;

    public SignUpSocialMediaClickListener(Context context) { mContext = context; }

    @Override public void onClick(View v){
        //Launch SignInSocialMedia activity
        Intent k = new Intent(mContext, OAuthActivity.class);
        mContext.startActivity(k);
    }
}