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
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;

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
            }
        }
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
}