package org.openaccessbutton.openaccessbutton.push;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean pushEnabled = sp.getBoolean("pushNotifications", true);

        String user_id;
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        if (installation == null) {
            user_id = "";
        } else {
            user_id = installation.getObjectId();
        }

        if (pushEnabled) {
            PushService.setDefaultPushCallback(context, MainActivity.class);
            PushService.subscribe(context, "Users", MainActivity.class);
            PushService.subscribe(context, "user_" + user_id, MainActivity.class);
        } else {
            PushService.setDefaultPushCallback(context, null);
            PushService.unsubscribe(context, "Users");
            PushService.unsubscribe(context, "user_" + user_id);
        }

    }
}
