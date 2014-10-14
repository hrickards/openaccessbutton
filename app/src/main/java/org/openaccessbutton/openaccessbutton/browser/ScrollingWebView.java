package org.openaccessbutton.openaccessbutton.browser;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by rickards on 9/29/14.
 */
public class ScrollingWebView extends WebView {
    public interface ScrollingCallback {
        void onYChanged(int y);
    }
    ScrollingCallback mScrollingCallback;

    public ScrollingWebView(Context context) {
        super(context);
    }

    public ScrollingWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollingWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollingCallback(ScrollingCallback callback) {
        mScrollingCallback = callback;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        if (mScrollingCallback != null) {
            mScrollingCallback.onYChanged(y);
        }

        super.onScrollChanged(x, y, oldx, oldy);
    }
}
