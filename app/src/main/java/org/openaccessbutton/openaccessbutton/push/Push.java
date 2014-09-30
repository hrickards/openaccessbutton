package org.openaccessbutton.openaccessbutton.push;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;

public class Push {
    /**
     * Setup the app as a receiver for push notifications using the Parse API
     */
    public static void initialisePushNotifications(Context context) {
        Parse.initialize(context, "AWw3gd0gGnS5JDxepYKcPrL42G6FfSmntJiwPIfr", "4IugoFfEIc8YIAgsRm5elZuUGIB7z9bsPNHcfl3c");
        PushService.setDefaultPushCallback(context, MainActivity.class);
        PushService.subscribe(context, "Users", MainActivity.class);

        String user_id;
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        if (installation == null) {
            user_id = "";
        } else {
            user_id = installation.getObjectId();
        }
        PushService.subscribe(context, "user_" + user_id, MainActivity.class);
    }
}
