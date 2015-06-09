package org.openaccessbutton.openaccessbutton.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.goebl.david.Webb;
import com.goebl.david.WebbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.map.Item;

/**
 * Created by rickards on 10/14/14.
 */
public class API {
    public final static String API_URL = "https://openaccessbutton.org/api";

    public interface SignupCallback {
        void onComplete(String username, String apikey);
        void onError(String message);
    }

    public interface OAuthSignupCallback {
        void onComplete(String username, String apikey);
    }

    public interface SigninCallback {
        void onComplete(String username, String apikey);
        void onError(String message);
    }

    public interface StoryListCallback {
        void onComplete(Item[] stories);
        void onError(String message);
    }

    public interface Callback {
        void onComplete();
    }

    public static void oauthSignupRequest(OAuthSignupCallback callback) {
        Log.w("oab", "oauth not implemented");
        callback.onComplete("", "");
    }

    public static void signupRequest(final SignupCallback callback, final Context context, final String email, final String profession, final String username, final String password) {
        final ProgressDialog dialog = createProgressDialog(context);
        Runnable r = new Runnable() {
            public void run() {
                try {
                    Webb webb = Webb.create();
                    JSONObject result = webb
                            .post(API_URL + "/register")
                            .param("email", email)
                            .param("profession", profession)
                            .param("username", username)
                            .param("password", password)
                            .asJsonObject()
                            .getBody();

                    if (result == null) {
                        throw new Error("Email address already registered");
                    }

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

                    closeProgressDialog(dialog, context);
                    callback.onComplete(username, apiKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgressDialog(dialog, context);
                    callback.onError(context.getResources().getString(R.string.username_already_taken));
                } catch (Error e) {
                    e.printStackTrace();
                    closeProgressDialog(dialog, context);
                    callback.onError(context.getResources().getString(R.string.username_already_taken));
                }
            }
        };
        (new Thread(r)).start();
    }

    public static void signinRequest(final SigninCallback callback, final Context context, final String username, final String password) {
        final ProgressDialog dialog = createProgressDialog(context);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Webb webb = Webb.create();
                    JSONObject result = webb
                            .post(API_URL + "/retrieve")
                            .param("username", username)
                            .param("password", password)
                            .ensureSuccess()
                            .asJsonObject()
                            .getBody();
                    String apiKey = result.getString("api_key");
                    if ((apiKey == null) || (apiKey.equals(""))) {
                        Log.w("result", result.toString());
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

                    closeProgressDialog(dialog, context);
                    callback.onComplete(username, apiKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgressDialog(dialog, context);
                    callback.onError(context.getResources().getString(R.string.invalid_username_password));
                } catch (Error e) {
                    e.printStackTrace();
                    closeProgressDialog(dialog, context);
                    callback.onError(context.getResources().getString(R.string.invalid_username_password));
                } catch (WebbException e) {
                    e.printStackTrace();
                    closeProgressDialog(dialog, context);
                    callback.onError(context.getResources().getString(R.string.invalid_username_password));}
                }
        };
        (new Thread(r)).start();

    }

    public static void blockedRequest(final Callback callback, final Context context, final String url, final Double mLatitude, final Double mLongitude, final String story, final boolean wishlist) {
        SharedPreferences prefs = context.getSharedPreferences("org.openaccessbutton.openaccessbutton", Context.MODE_PRIVATE);
        final String apiKey = prefs.getString("api_key", "");

        final ProgressDialog dialog = createProgressDialog(context);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                JSONObject data = new JSONObject();
                // on errors, we just post off whatever data we could dump into JSON for now
                try {
                    data.put("url", url);
                } catch (JSONException e) { e.printStackTrace(); }
                try {
                    data.put("story", story);
                } catch (JSONException e) { e.printStackTrace(); }
                try {
                    data.put("wishlist", wishlist);
                } catch (JSONException e) { e.printStackTrace(); }
                try {
                    if (mLatitude != null && mLongitude != null) {
                        JSONObject geo = new JSONObject();
                        // lat and lng should be strings for the API
                        geo.put("lat", mLatitude.toString());
                        geo.put("lon", mLongitude.toString());
                        data.put("location", geo);
                    }
                } catch (JSONException e) { e.printStackTrace(); }

                Webb webb = Webb.create();
                JSONObject result = webb
                        .post(API_URL + "/blocked")
                        .param("api_key", apiKey) // We need to get this when the user signs up
                        .body(data)
                        .ensureSuccess()
                        .asJsonObject()
                        .getBody();

                closeProgressDialog(dialog, context);
                callback.onComplete();
            }
        };
        (new Thread(r)).start();

    }

    public static void storyListRequest(final StoryListCallback callback, final Context context) {
        final ProgressDialog dialog = createProgressDialog(context);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO are we getting all of them?
                    String query = "{\"size\":1000,\"query\":{\"match_all\":{}},\"fields\":[\"coords.lat\",\"coords.lng\",\"story\",\"doi\",\"user_profession\",\"url\",\"accessed\",\"user_name\",\"description\"]}";

                    Webb webb = Webb.create();
                    JSONObject results = webb
                        .get("https://openaccessbutton.org/query")
                        .param("source", query)
                        .ensureSuccess()
                        .asJsonObject()
                        .getBody();
                    closeProgressDialog(dialog, context);

                    JSONArray hits = results.getJSONObject("hits").getJSONArray("hits");
                    Item[] items = new Item[hits.length()];
                    for (int i=0; i<hits.length(); i++) {
                        JSONObject fields = hits.getJSONObject(i).getJSONObject("fields");
                        // These are to 1dp however we want to spread them out
                        // TODO Use some super-ultra-fast-but-still-provably-random-library rather than doing this
                        double lat = fields.getDouble("coords.lat") + 0.0005*(((i*i)%200)-100);
                        double lng = fields.getDouble("coords.lng") + 0.0005*(((i*i*i)%200)-100);
                        String story = fields.getString("story");
                        String doi = fields.getString("doi");
                        String userProfession = fields.getString("user_profession");
                        String url = fields.getString("url");
                        String accessed = fields.getString("accessed");
                        String userName = fields.getString("user_name");
                        String description = fields.getString("description");
                        items[i] = new Item(lat, lng, story, doi, userProfession, url, accessed, userName, description);
                    }

                    callback.onComplete(items);
                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgressDialog(dialog, context);
                    callback.onError(context.getResources().getString(R.string.map_error));
                } catch (Error e) {
                    e.printStackTrace();
                    closeProgressDialog(dialog, context);
                    callback.onError(context.getResources().getString(R.string.map_error));
                } catch (WebbException e) {
                    e.printStackTrace();
                    closeProgressDialog(dialog, context);
                    callback.onError(context.getResources().getString(R.string.map_error));}
            }
        };
        (new Thread(r)).start();
    }

    protected static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.show();

        return progress;
    }

    // Context must be an Activity
    protected static void closeProgressDialog(final ProgressDialog dialog, Context context) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
    }
}
