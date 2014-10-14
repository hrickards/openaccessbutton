/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package org.openaccessbutton.openaccessbutton.blog;

import android.util.Xml;

import org.apache.commons.lang3.StringEscapeUtils;
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
 * Parse the blog RSS (XML) feed into a List of Posts.
 * Based on the examples given in the Android documentation at
 * http://developer.android.com/training/basics/network-ops/xml.html.
 */
public class RssParser {
    // Namespaces aren't needed at this point
    private static final String ns = null;

    /**
     * Parse an InputStream containing the RSS feed.
     * @param in InputStream containing blog RSS feed
     * @return List of Posts
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<Post> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Parse the entire feed.
     */
    private List<Post> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        // Everything's contained in an <rss> tag
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // All posts are contained in a parent <channel> tag
            if (name.equals("channel")) {
                // There's only one channel, so we can simply return Posts from the first channel
                return readChannel(parser);
            } else {
                skip(parser);
            }
        }
        return null;
    }

    /**
     * Parse a <channel> tag.
     */
    private List<Post> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        // To store parsed posts in
        List<Post> posts = new ArrayList<Post>();

        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Posts are <item> tags
            if (name.equals("item")) {
                posts.add(readPost(parser));
            } else {
                skip(parser);
            }
        }

        return posts;
    }

    /**
     * Parse an individual post from a <post> tag.
     */
    private Post readPost(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");

        // Post properties
        String title = null;
        String description = null;
        Date date = null;
        String creator = null;
        String content = null;
        String link = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Read post properties
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("pubDate")) {
                date = readDate(parser);
            } else if (name.equals("dc:creator")) {
                creator = readCreator(parser);
            } else if (name.equals("content:encoded")) {
                content = readContent(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new Post(title, description, date, creator, content, link);
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
     * Extract text from a <pubDate> tag
     */
    private Date readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");

        String dateString = readText(parser);
        SimpleDateFormat dateParser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZZ", Locale.ENGLISH);
        Date date = null;

        try {
            date = dateParser.parse(dateString);
        } catch (Exception e) {
        }

        return date;
    }
    /**
     * Extract text from a <dc:creator> tag
     */
    private String readCreator(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "dc:creator");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "dc:creator");
        return title;
    }

    /**
     * Extract and format text from a <description> tag
     */
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        // Descriptions end in " [...]" (but with unicode ellipses) and then a 1x1px image so we
        // remove those
        String description = readText(parser).replaceAll("(?m) \\[\u2026\\]<img.*/>$", "");
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description + "...";
    }

    /**
     * Extract and format text from a <content:encoded> tag
     */
    private String readContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "content:encoded");
        // Descriptions end in " [...]" (but with unicode ellipses) and then a 1x1px image so we
        // remove those
        String content = readText(parser).replaceAll("<img.*/>$", "");
        parser.require(XmlPullParser.END_TAG, ns, "content:encoded");
        return content;
    }

    /**
     * Extract text from a <link> tag
     */
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    /**
     * Extract text from a tag
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        // Unicode characters are escaped in the XML, so we unescape those
        return StringEscapeUtils.unescapeXml(result);
    }
}