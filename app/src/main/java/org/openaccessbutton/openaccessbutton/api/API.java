package org.openaccessbutton.openaccessbutton.api;

import android.util.Log;

import org.openaccessbutton.openaccessbutton.map.Item;

/**
 * Created by rickards on 10/14/14.
 */
public class API {
    public interface SignupCallback {
        void onComplete(String username, String apikey);
    }

    public interface SigninCallback {
        void onComplete(String username, String apikey);
    }

    public interface StoryListCallback {
        void onComplete(Item[] stories);
    }

    public interface Callback {
        void onComplete();
    }

    public static void forgotPasswordRequest(Callback callback) {
        // TODO Implement forgot password
        Log.w("oab", "forgot password not implemented");
        callback.onComplete();
    }

    public static void signupRequest(SignupCallback callback) {
        // TODO Call the API
        // TODO Store these details in SharedPrefs
        Log.w("oab", "signup not implemented");
        callback.onComplete("hrickards", "foobar");
    }

    public static void signinRequest(SigninCallback callback) {
        // TODO Call the API
        // TODO Store these details in SharedPrefs
        Log.w("oab", "signin not implemented");
        callback.onComplete("hrickards", "foobar");
    }

    public static void blockedRequest(Callback callback) {
        // TODO Call the API
        Log.w("oab", "blocked not implemented");
        callback.onComplete();
    }

    public static void storyListRequest(StoryListCallback callback) {
        // TODO Call the API
        Log.w("oab", "storylist not implemented");
        callback.onComplete(new Item[] {});
    }
}
