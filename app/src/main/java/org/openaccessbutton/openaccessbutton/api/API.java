package org.openaccessbutton.openaccessbutton.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.goebl.david.Webb;
import com.goebl.david.WebbException;

import org.json.JSONException;
import org.json.JSONObject;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.map.Item;

/**
 * Created by rickards on 10/14/14.
 */
public class API {
    public final static String API_URL = "http://openaccessbutton.org/api";

    public interface SignupCallback {
        void onComplete(String username, String apikey);
    }

    public interface SigninCallback {
        void onComplete(String username, String apikey);
        void onError(String message);
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

    public static void oauthSignupRequest(SignupCallback callback) {
        Log.w("oab", "oauth not implemented");
        callback.onComplete("", "");
    }

    public static void signupRequest(final SignupCallback callback, final Context context, final String email, final String profession, final String name, final String password) {
        Runnable r = new Runnable() {
            public void run() {
                try {
                    Webb webb = Webb.create();
                    JSONObject result = webb
                            .post(API_URL + "/register")
                            .param("email", email)
                            .param("profession", profession)
                            .param("name", name)
                            .param("password", password)
                            .ensureSuccess()
                            .asJsonObject()
                            .getBody();
                    String apiKey = result.getString("api_key");
                    String username = result.getString("username");

                    // Store API key so we know we're authenticated (and skip intro pages)
                    // and also for making API requests
                    SharedPreferences prefs = context.getSharedPreferences("org.openaccessbutton.openaccessbutton", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.clear();
                    edit.putString("api_key", apiKey);
                    edit.putString("username", username);
                    edit.apply();

                    callback.onComplete(username, apiKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        (new Thread(r)).start();
    }

    public static void signinRequest(final SigninCallback callback, final Context context, final String username, final String password) {
        // TODO Call the API
        // TODO Store these details in SharedPrefs
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Webb webb = Webb.create();
                    JSONObject result = webb
                            .post(API_URL + "/retrieve")
                            .param("email", username)
                            .param("password", password)
                            .ensureSuccess()
                            .asJsonObject()
                            .getBody();
                    String apiKey = result.getString("api_key");
                    if ((apiKey == null) || (apiKey.equals(""))) {
                        throw new Error("Invalid username or password");
                    }

                    // Store API key so we know we're authenticated (and skip intro pages)
                    // and also for making API requests
                    SharedPreferences prefs = context.getSharedPreferences("org.openaccessbutton.openaccessbutton", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.clear();
                    edit.putString("api_key", apiKey);
                    edit.putString("username", username);
                    edit.apply();

                    callback.onComplete(username, apiKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(context.getResources().getString(R.string.invalid_username_password));
                } catch (Error e) {
                    e.printStackTrace();
                    callback.onError(context.getResources().getString(R.string.invalid_username_password));
                } catch (WebbException e) {
                    e.printStackTrace();
                    callback.onError(context.getResources().getString(R.string.invalid_username_password));}
                }
        };
        (new Thread(r)).start();

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
