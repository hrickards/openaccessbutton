package org.openaccessbutton.openaccessbutton;

/**
 * A module in the navigation list (e.g., blog)
 */
public class NavigationItem {
    public final String title;
    public final String className;
    public final String menu;
    public final String filename;

    public NavigationItem(String title, String className, String menu, String filename) {
        this.title = title;
        this.className = className;
        this.menu = menu;
        this.filename = filename;
    }
}
