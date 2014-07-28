package org.openaccessbutton.openaccessbutton.blog;/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Downloads (asynchronously) blog posts and calls a listener function with a List of parsed
 * Posts. Downloading XML based on the examples given in the Android documentation at
 * http://developer.android.com/training/basics/network-ops/xml.html.
 *
 * parameters: listener, page number
 */
public class DownloadTask extends AsyncTask<Object, Void, List<Post>> {
    /**
     * Called with the List of Posts when the download is finished
     */
    public interface OnDownloadCompleteListener {
        void onDownloadComplete(List<Post> posts);
    }
    OnDownloadCompleteListener mListener;

    /**
     * Blog feed URL
     */
    public static String BLOG_URL = "http://blog.openaccessbutton.org/feed/";

    @Override
    protected List<Post> doInBackground(Object... params) {
        // Extract the parameters
        mListener = (OnDownloadCompleteListener) params[0];
        int pageNumber = (Integer) params[1];

        // Create url
        // e.g., /feed/?paged=2
        String url = BLOG_URL + "?paged=" + Integer.toString(pageNumber);

        try {
            return loadPostsFromNetwork(url);
        } catch (IOException e) {
            // TODO: Show UI messages for these errors
            return new ArrayList<Post>();
        } catch (XmlPullParserException e) {
            return new ArrayList<Post>();
        }
    }

    @Override
    protected void onPostExecute(List<Post> posts) {
        // Call the listener
        mListener.onDownloadComplete(posts);
    }

    private List<Post> loadPostsFromNetwork(String urlString) throws XmlPullParserException,
            IOException {
        InputStream stream = null;
        RssParser rssParser = new RssParser();
        List<Post> posts = null;

        // Download and parse
        try {
            stream = downloadUrl(urlString);
            posts = rssParser.parse(stream);
        // Make sure input stream is closed
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return posts;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        return conn.getInputStream();
    }
}
