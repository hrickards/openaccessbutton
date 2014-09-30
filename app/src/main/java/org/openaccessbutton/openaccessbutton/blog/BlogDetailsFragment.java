package org.openaccessbutton.openaccessbutton.blog;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.openaccessbutton.openaccessbutton.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class BlogDetailsFragment extends Fragment {
    Post mPost;

    public BlogDetailsFragment() {
        // Required empty public constructor
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

        return view;
    }


}
