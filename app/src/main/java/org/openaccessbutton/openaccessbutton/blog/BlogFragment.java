/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton.blog;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.OnFragmentNeededListener;
import org.openaccessbutton.openaccessbutton.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows a list of blog entries, automatically loading more as user scrolls
 *
 */
public class BlogFragment extends android.app.ListFragment implements AbsListView.OnScrollListener {
    public BlogFragment() {}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO
    }

    OnFragmentNeededListener mCallback;

    // The posts shown
    List<Post> mItems = new ArrayList<Post>();
    BlogAdapter mAdapter;

    // Loading spinner
    View footerView;

    // For pagination
    // TODO: Stop when we get to the end of all posts
    private int mCurrentPage = 0;

    // Number of posts above the bottom at which the threshold to load more posts is reached
    private static int sThreshold = 2;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure parent activity has implemented comm. interface for launching fragments
        try {
            mCallback = (OnFragmentNeededListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentNeededListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Setup customised ArrayAdapter
        mAdapter = new BlogAdapter(inflater.getContext(),
                R.layout.blog_list_item, mItems);
        setListAdapter(mAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Call this class on scroll movements
        getListView().setOnScrollListener(this);

        // Setup footer view and hide it
        footerView = getActivity().getLayoutInflater().inflate(R.layout.blog_list_footer, null);
        getListView().addFooterView(footerView);
        footerView.setVisibility(View.GONE);

        // TODO: Automatically determine how many posts to load
        loadMore();

        if (savedInstanceState != null) {
            // TODO
        }
    }

    /**
     * Show details fragment when a post is clicked
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Fragment blogDetailsFragment = new BlogDetailsFragment();

        // Send in the post as an argument
        Post post = mItems.get(position);
        Bundle data = new Bundle();
        data.putString("post", new Gson().toJson(post));
        blogDetailsFragment.setArguments(data);

        // Use the communication interface we have with the parent activity to launch the
        // fragment
        mCallback.launchFragment(blogDetailsFragment, "blogDetailsFragment", data, true);
    }

    /**
     * Load more posts
     */
    private void loadMore() {
        // Show loading spinner
        footerView.setVisibility(View.VISIBLE);
        // Update now not after posts downloaded in case two requests fired
        mCurrentPage++;
        new DownloadTask().execute(new DownloadTask.OnDownloadCompleteListener() {
            @Override
            public void onDownloadComplete(List<Post> items) {
                // Add posts to List and show them
                for (Post item : items) {
                    mItems.add(item);
                }
                mAdapter.notifyDataSetChanged();

                // Hide loading spinner
                footerView.setVisibility(View.GONE);
            }
        }, mCurrentPage);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {}

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        // Once scrolling has stopped
        if (scrollState == SCROLL_STATE_IDLE) {
            if (needsMorePosts()) { loadMore(); }
        }
    }

    /**
     * Whether more posts should be loaded (based on how far user has scrolled)
     */
    private boolean needsMorePosts() {
        AbsListView listView = getListView();
        return listView.getLastVisiblePosition() >= listView.getCount() - 1 - sThreshold;
    }
}
