package org.openaccessbutton.openaccessbutton.blog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Customised ArrayAdapter to show blog list item using custom view
 */
public class BlogAdapter extends ArrayAdapter<Post> {
    Context context;
    int layoutResourceId;
    List<Post> data = null;

    public BlogAdapter(Context context, int layoutResourceId, List<Post> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // Create a new view if one doesn't exist already
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        // Show data in the view
        Post post = data.get(position);
        TextView blogTitle = (TextView) row.findViewById(R.id.blog_title);
        TextView blogDate = (TextView) row.findViewById(R.id.blog_date);
        TextView blogAuthor = (TextView) row.findViewById(R.id.blog_author);
        TextView blogSummary = (TextView) row.findViewById(R.id.blog_summary);
        blogTitle.setText(post.shortTitle);
        blogDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(post.date));
        blogAuthor.setText(post.author);
        blogSummary.setText(post.description);

        return row;
    }
}
