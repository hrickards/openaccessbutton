/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton.blog;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Structure containing one blog post
 */
public class Post {
    public final String title;
    public final String shortTitle;
    public final String description;
    public final Date date;
    public final String author;
    public final String content;
    public final String link;

    private static final int SHORT_TITLE_LENGTH = 100;

    public Post(String title, String description, Date date, String author, String content, String link) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.author = author;
        this.content = content;
        this.link = link;
        // Shorten blogpost titles if they're too long
        this.shortTitle = StringUtils.abbreviate(title, SHORT_TITLE_LENGTH);
    }

    @Override
    /**
     * Shows the title + description as a simple textual representation of the post
     */
    public String toString() {
        return this.title + "\n" + this.description;
    }

    public String html() {
        // White font and with blog title as header
        return "<html><head>"
                + "<style type=\"text/css\">body,a,a:hover,a:active,a:visited{color: #212F3F}"
                + "</style></head>"
                + "<body>"
                + "<h1>" + this.title + "</h1>"
                + this.content
                + "</body></html>";
    }
}