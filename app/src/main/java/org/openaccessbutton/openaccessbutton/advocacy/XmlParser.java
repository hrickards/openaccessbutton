/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton.advocacy;


import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Parse the custom XML containing the advocacy content, and add it to a view
 */
public class XmlParser {
    // No namespace yet
    private static final String ns = null;

    /**
     * Parse the XML and add it to the passed layout
     * @param in InputStream to read XML from
     * @param layout LinearLayout to append parsed content to
     * @param context Context to use to create views
     * @throws XmlPullParserException
     * @throws IOException
     */
    public void parseToView(InputStream in, LinearLayout layout, Context context) throws
            XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readFeed(parser, layout, context);
        } finally {
            in.close();
        }
    }

    private void readFeed(XmlPullParser parser, LinearLayout layout, Context context) throws
            XmlPullParserException, IOException {

        LayoutInflater li = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        parser.require(XmlPullParser.START_TAG, ns, "page");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            // No switch for Strings
            if (name.equals("paragraph")) {
                parser.require(XmlPullParser.START_TAG, ns, "paragraph");
                String text = readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, "paragraph");

                View view = li.inflate(R.layout.advocacy_paragraph, null);
                TextView tv = (TextView) view.findViewById(R.id.advocacy_paragraph_content);
                tv.setText(text);
                layout.addView(view);
            } else if (name.equals("faq")) {
                parser.require(XmlPullParser.START_TAG, ns, "faq");

                String question = "";
                String answer = "";
                String details = "";

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String subname = parser.getName();
                    // Read post properties
                    if (subname.equals("question")) {
                        question = readFaqQuestion(parser);
                    } else if (subname.equals("answer")) {
                        answer = readFaqAnswer(parser);
                    } else if (subname.equals("details")) {
                        details = readFaqDetails(parser);
                    } else {
                        skip(parser);
                    }
                }

                FaqView view = new FaqView(context);
                view.setQuestion(question);
                view.setAnswer(answer);
                view.setDetails(details);
                layout.addView(view);
            } else if (name.equals("html")) {
                String html = readHtml(parser, "html");

                View view = li.inflate(R.layout.advocacy_html, null);
                WebView wv = (WebView) view.findViewById(R.id.advocacy_html_webview);
                wv.loadData(html, "text/html", "utf-8");
                wv.setBackgroundColor(0x00000000); // Transparency
                layout.addView(view);
            }
        }
    }

    /**
     * Extract text from a <question> tag
     */
    private String readFaqQuestion(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "question");
        String question = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "question");
        return question;
    }

    /**
     * Extract text from a <answer> tag
     */
    private String readFaqAnswer(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "answer");
        String answer = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "answer");
        return answer;
    }

    /**
     * Extract html from a <details> tag
     */
    private String readFaqDetails(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readHtml(parser, "details");
    }

    /**
     * Extract HTML from a generic tag
     */
    private String readHtml(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);

        String html = "";
        while (true) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                html = html + "<" + parser.getName() + ">";
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(tag)) {
                    break;
                }
                html = html + "</" + parser.getName() + ">";
            } else {
                html = html + parser.getText();
            }
        }
        return html;
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
}