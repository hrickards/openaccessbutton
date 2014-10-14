package org.openaccessbutton.openaccessbutton.blog;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.OnShareIntentInterface;
import org.openaccessbutton.openaccessbutton.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class BlogDetailsFragment extends Fragment {
    Post mPost;
    OnShareIntentInterface mCallback;

    public BlogDetailsFragment() {
        // Required empty public constructor
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnShareIntentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnShareIntentInterface");
        }
    }

    public Intent onShareButtonPressed(Resources resources) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        if (mPost != null) {
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mPost.shortTitle);
            shareIntent.putExtra(Intent.EXTRA_TEXT, mPost.link);
        }
        shareIntent.setType("text/plain");

        return shareIntent;
    }

    public void updateShareIntent() {
        Intent shareIntent = onShareButtonPressed(getResources());
        mCallback.onShareIntentUpdated(shareIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateShareIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Post data
        Bundle args = getArguments();
        mPost = new Gson().fromJson(args.getString("post"), Post.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blog_details, container, false);

        // Set post data in view
        WebView contentView = (WebView) view.findViewById(R.id.blog_content);
        contentView.getSettings().setJavaScriptEnabled(true);
        contentView.loadDataWithBaseURL("", mPost.html(), "text/html", "UTF-8", "");
        // Transparent WebView
        contentView.setBackgroundColor(0x00000000);
        contentView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        updateShareIntent();

        return view;
    }

}
