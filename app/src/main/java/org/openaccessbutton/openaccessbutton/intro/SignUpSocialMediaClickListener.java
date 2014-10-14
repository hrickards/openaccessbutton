package org.openaccessbutton.openaccessbutton.intro;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class SignUpSocialMediaClickListener implements Button.OnClickListener{
    Context mContext;
    String mProvider;

    public SignUpSocialMediaClickListener(Context context, String provider) { mContext = context; mProvider = provider;}

    @Override public void onClick(View v){
        //Launch OAuthActivity activity
        Intent k = new Intent(mContext, OAuthActivity.class);
        k.putExtra("provider", mProvider);
        mContext.startActivity(k);
    }
}