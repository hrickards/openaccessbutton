/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Allows the user to browse the web and view journal articles, and submit
 * them to OAB if they're paywalled.
 * TODO: Implement
 */
public class BrowserFragment extends Fragment {
    public BrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browser, container, false);
    }
}
