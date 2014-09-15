package org.openaccessbutton.openaccessbutton.intro;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.menu.MenuActivity;

/**
 * Created by harry on 06/08/14.
 */
// Based on google's AnimationsDemo code
public class ScreenSlidePageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    private int mPageNumber;

    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static int numberPages(Context context) {
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(context).inflate(
                R.layout.fragment_intro_pages, null);
        return rootView.getChildCount();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_intro_pages, container, false);

        View childView = rootView.getChildAt(mPageNumber);
        if (childView.getParent() != null) {
            rootView.removeView(childView);
        }

        // Logic we need to run on a per-view basis
        switch (mPageNumber) {
            case 4:
                Button signupButton = (Button) childView.findViewById(R.id.signupEmailButton);
                signupButton.setOnClickListener(new SignupEmailButtonClickListener(getActivity()));
                TextView signinButton = (TextView) childView.findViewById(R.id.signinButton);
                signinButton.setOnClickListener(new SigninButtonClickListener(getActivity()));

                // Social sign in buttons
                TextView signupGoogleButton = (Button) childView.findViewById(R.id.signupGoogleButton);
                TextView signupTwitterButton = (Button) childView.findViewById(R.id.signupTwitterButton);
                TextView signupFacebookButton = (Button) childView.findViewById(R.id.signupFacebookButton);
                signupGoogleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent k = new Intent(getActivity(), OAuthActivity.class);
                        k.putExtra("provider", "google");
                        startActivity(k);
                        getActivity().finish();
                    }
                });
                signupTwitterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent k = new Intent(getActivity(), OAuthActivity.class);
                        k.putExtra("provider", "twitter");
                        startActivity(k);
                        getActivity().finish();
                    }
                });
                signupFacebookButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent k = new Intent(getActivity(), OAuthActivity.class);
                        k.putExtra("provider", "facebook");
                        startActivity(k);
                        getActivity().finish();
                    }
                });

            case 5:
                Button gotoMenuButton = (Button) childView.findViewById(R.id.goto_menu_button);
                gotoMenuButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent k = new Intent(getActivity(), MenuActivity.class);
                        startActivity(k);
                        getActivity().finish();
                    }
                });
        }

        return childView;
    }
}