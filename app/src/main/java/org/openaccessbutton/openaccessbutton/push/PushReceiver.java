package org.openaccessbutton.openaccessbutton.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.advocacy.QuestionsActivity;

import java.net.URI;
import java.util.Iterator;

/**
 * Receives a push notification URL and opens it in the browser
 */
public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // Obtain all the data we need for the notification and any actions
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            String title = json.getString("customTitle");
            String alert = json.getString("customAlert");
            String type = json.getString("type");

            // See http://stackoverflow.com/questions/20881226. This makes our PendingIntent's work
            // but no-one seems to know why? Android...
            int requestID = (int) System.currentTimeMillis();

            Intent i;
            if (type.equals("url")) {
                String url = json.getString("url");
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            } else if (type.equals("question")) {
                i = new Intent(context, QuestionsActivity.class);
            } else {
                i = new Intent(context, MainActivity.class);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pi = PendingIntent.getActivity(context, requestID, i, PendingIntent.FLAG_UPDATE_CURRENT);

            // Create a notification
            Notification.Builder nb = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(alert)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pi)
                    .setAutoCancel(true);

            // If we have a long description, show it in an expandeable notification\
            // TODO: Various things below require API level 16. Can we use compat libraries to make
            // them work in 15? Or just not use those specific features in 15? Or bump up the min
            // required version to 16 (last-case resort).
            String bigDescription = json.getString("bigDescription");
            if (bigDescription != null && bigDescription.length() > 0) {
                nb.setStyle(new Notification.BigTextStyle().bigText(bigDescription));
            }

            Notification n = nb.build();
            n.defaults |= Notification.DEFAULT_ALL;

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);
        } catch (JSONException e) {
            Log.d("PushReceiver", "JSONException: " + e.getMessage());
        }
    }
}
