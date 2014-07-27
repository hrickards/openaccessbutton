/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.openaccessbutton.openaccessbutton.advocacy.XmlParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Shows static/semi-dynamic content (regarding open access advocacy) in a WebView.
 */
public class AdvocacyFragment extends Fragment {
    public AdvocacyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advocacy, container, false);

        // XML file containing content
        InputStream object = this.getResources().openRawResource(R.raw.advocacy);
        // LinearLayout to append content to
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.advocacy_content);

        try {
            XmlParser xmlParser = new XmlParser();
            xmlParser.parseToView(object, layout, getActivity().getApplicationContext());
        } catch (IOException e) {
            // TODO: Do something
            Log.e("openaccess", "exception", e);
        } catch (XmlPullParserException e) {
            // TODO: Do something
            Log.e("openaccess", "exception", e);
        }

        return view;
    }
}
