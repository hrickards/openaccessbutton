package org.openaccessbutton.openaccessbutton.intro;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;

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

        switch (mPageNumber) {
            case 3:
                Button gotoMainButton = (Button) childView.findViewById(R.id.goto_main_button);
                gotoMainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent k = new Intent(getActivity(), MainActivity.class);
                        startActivity(k);
                        getActivity().finish();
                    }
                });
        }

        return childView;
    }
}