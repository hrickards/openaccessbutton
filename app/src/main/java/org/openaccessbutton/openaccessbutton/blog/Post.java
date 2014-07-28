/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton.blog;

/**
 * Structure containing one blog post
 */
public class Post {
    public final String title;
    public final String description;

    public Post(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    /**
     * Shows the title + description as a simple textual representation of the post
     */
    public String toString() {
        return this.title + "\n" + this.description;
    }
}