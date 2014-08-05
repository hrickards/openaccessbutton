/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton.blog;

import java.util.Date;

/**
 * Structure containing one blog post
 */
public class Post {
    public final String title;
    public final String description;
    public final Date date;
    public final String author;
    public final String content;

    public Post(String title, String description, Date date, String author, String content) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.author = author;
        this.content = content;
    }

    @Override
    /**
     * Shows the title + description as a simple textual representation of the post
     */
    public String toString() {
        return this.title + "\n" + this.description;
    }
}