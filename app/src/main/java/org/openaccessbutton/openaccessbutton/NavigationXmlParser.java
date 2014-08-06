/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton;


import android.content.Context;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;
import org.openaccessbutton.openaccessbutton.blog.Post;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Parse the custom XML containing the navigation
 */
public class NavigationXmlParser {
    private List<NavigationItem> mItems;
    // Namespaces aren't needed at this point
    private static final String ns = null;

    /**
     * Parse an InputStream containing the navigation items
     * @param in InputStream containing navigation items XML
     * @return List of NavigationItems
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<NavigationItem> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            mItems = readFeed(parser);
        } finally {
            in.close();
        }

        return mItems;
    }

    /**
     * Parse the entire XML.
     */
    private List<NavigationItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        // Everything's contained in an <navigation> tag
        parser.require(XmlPullParser.START_TAG, ns, "navigation");

        // To store parsed items in
        List<NavigationItem> items = new ArrayList<NavigationItem>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            // Items are <item> tags
            if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    /**
     * Parse an individual navigation item from a <item> tag.
     */
    private NavigationItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");

        // Item properties
        String title = null;
        String className = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Read post properties
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("class")) {
                className = readClassName(parser);
            } else {
                skip(parser);
            }
        }
        return new NavigationItem(title, className);
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    /**
     * Extract text from a <title> tag
     */
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    /**
     * Extract text from a <class> tag
     */
    private String readClassName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "class");
        String className = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "class");
        return className;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        // Remove adjacent spaces and newlines
        result = result.trim().replaceAll("[\r\n]+", " ").replaceAll(" +", " ");
        return result;
    }

    /**
     * Obtain an array of just the NavigationItem titles
     */
    public String[] getTitles() {
        String[] titles = new String[mItems.size()];
        for(int i=0; i<mItems.size(); i++) {
            titles[i] = mItems.get(i).title;
        }
        return titles;
    }

    /**
     * Return a specific NavigationItem
     */
    public NavigationItem get(int position) {
        return mItems.get(position);
    }

    /**
     * Return the number of NavigationItems
     */
    public int size() {
        return mItems.size();
    }
}