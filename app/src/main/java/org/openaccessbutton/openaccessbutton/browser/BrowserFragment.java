/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.button.ButtonSubmitActivity;

/**
 * Allows the user to browse the web and view journal articles, and submit
 * them to OAB if they're paywalled.
 */

public class BrowserFragment extends Fragment implements MainActivity.OnBackButtonInterface {
    ScrollingWebView mWebView;
    EditText mUrlBox;
    RelativeLayout mHeader;

    public BrowserFragment() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // TODO
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browser, container, false);

        mWebView = (ScrollingWebView) view.findViewById(R.id.mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

        // Share page to any generic application
        ImageView shareButton = (ImageView) view.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launches the chooser popup so the user can choose where to share the page
                // From here on out, Android handles everything for us
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mWebView.getUrl());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        // Submit page to OAB as a paywall
        ImageView oabButton = (ImageView) view.findViewById(R.id.oabButton);
        oabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to launch ButtonSubmitActivity with the URl as the param
                Intent oabIntent = new Intent(getActivity(), ButtonSubmitActivity.class);
                oabIntent.setAction(Intent.ACTION_SEND);
                oabIntent.putExtra(Intent.EXTRA_TEXT, mWebView.getUrl());
                oabIntent.setType("text/plain");
                startActivity(oabIntent);
            }
        });

        // URL box
        mUrlBox = (EditText) view.findViewById(R.id.uri);
        mUrlBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    // Enter button pressed
                    mWebView.loadUrl(mUrlBox.getText().toString());
                    // Close keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    // Take focus away from URL box
                    mUrlBox.clearFocus();

                }
                return true;
            }
        });

        // Allow URL box to scroll with the page
        final Context context = this.getActivity();
        mHeader = (RelativeLayout) view.findViewById(R.id.browserHeader);
        mWebView.setScrollingCallback(new ScrollingWebView.ScrollingCallback() {
            @Override
            public void onYChanged(int y) {
                // TODO Don't hardcode this
                int boxHeight = Math.round(50 * context.getResources().getDisplayMetrics().density);
                int newHeight = (y > boxHeight) ? 0 : boxHeight - y;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHeader.getLayoutParams();
                params.height = newHeight;
                mHeader.setLayoutParams(params);
            }
        });

        setUrl("http://scholar.google.com");

        return view;
    }
    
    public void setUrl(String url) {
        // Navigate WebView to URL
        mWebView.loadUrl(url);
        
        // Show in address bar
        mUrlBox.setText(url);
    }

    public boolean onBackButtonPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return false;
        }
    }
}