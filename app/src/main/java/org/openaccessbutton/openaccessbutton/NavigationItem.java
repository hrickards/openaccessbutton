package org.openaccessbutton.openaccessbutton;

/**
 * A module in the navigation list (e.g., blog)
 */
public class NavigationItem {
    public final String title;
    public final String className;

    public NavigationItem(String title, String className) {
        this.title = title;
        this.className = className;
    }
}
