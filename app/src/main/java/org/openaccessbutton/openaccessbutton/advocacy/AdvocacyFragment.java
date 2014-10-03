/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton.advocacy;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.parse.ParseInstallation;

import org.json.JSONObject;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.advocacy.XmlParser;
import org.openaccessbutton.openaccessbutton.intro.IntroActivity;
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


        // Section for submitting an additional question
        final TextView askQuestion = (TextView) view.findViewById(R.id.askQuestion);
        final LinearLayout newQuestionWrapper = (LinearLayout) view.findViewById(R.id.newQuestionWrapper);
        final EditText newQuestion = (EditText) view.findViewById(R.id.newQuestion);
        final TextView questionSubmitted = (TextView) view.findViewById(R.id.questionSubmitted);
        Button newQuestionSubmit = (Button) view.findViewById(R.id.newQuestionSubmit);

        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newQuestionWrapper.getVisibility() == View.VISIBLE) {
                    newQuestionWrapper.setVisibility(View.GONE);
                    questionSubmitted.setVisibility(View.GONE);
                    askQuestion.setText(getResources().getString(R.string.askQuestion));
                } else {
                    newQuestionWrapper.setVisibility(View.VISIBLE);
                    questionSubmitted.setVisibility(View.GONE);
                    newQuestion.setText("");
                    askQuestion.setText(getResources().getString(R.string.askQuestionClosed));
                }
            }
        });
        newQuestionSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Submit the question to the web service
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String user_id;
                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            if (installation == null) {
                                user_id = "";
                            } else {
                                user_id = installation.getObjectId();
                            }

                            Log.w("asdf", user_id);

                            Webb webb = Webb.create();
                            JSONObject response = webb
                                    .post("http://oabuttonquestions.herokuapp.com/questions/new")
                                    .param("user_id", user_id)
                                    .param("question", newQuestion.getText())
                                    .ensureSuccess()
                                    .asJsonObject()
                                    .getBody();

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    questionSubmitted.setVisibility(View.VISIBLE);
                                    newQuestion.setText("");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();

                            // TODO: Show error
                        }
                    }
                });
                thread.start();
            }
        });


        return view;
    }
}
