package org.openaccessbutton.openaccessbutton.advocacy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openaccessbutton.openaccessbutton.R;

/**
 * Custom compound view that shows a dynamic FAQ in the advocacy page. Split into thee parts:
 * a textual question, a textual answer and an HTML details section.
 */
public class FaqView extends LinearLayout {
    public FaqView(Context context) {
        super(context);
        initView(context);
    }

    public FaqView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FaqView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void setQuestion(String question) {
        ((TextView) findViewById(R.id.faqQuestion1)).setText(question);
        ((TextView) findViewById(R.id.faqQuestion2)).setText(question);
    }

    public void setAnswer(String answer) {
        ((TextView) findViewById(R.id.faqAnswer)).setText(answer);
    }

    public void setDetails(String html) {
        ((WebView) findViewById(R.id.detailsContent)).loadData(html, "text/html", "utf-8");
    }

    protected void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.advocacy_faq, this);

        // Initialise the details WebView with blank data
        WebView details = (WebView) findViewById(R.id.detailsContent);
        details.loadData("<html><body></body></html>", "text/html", "utf-8");

        // Start off with the answer and details hidden
        switchToQuestionView();

        // Buttons to switch between the different views
        ImageView expandAnswerButton = (ImageView) findViewById(R.id.expandAnswerButton);
        expandAnswerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToAnswerView();
            }
        });
        ImageView contractAnswerButton = (ImageView) findViewById(R.id.contractAnswerButton);
        contractAnswerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToQuestionView();
            }
        });
        TextView moreInfoButton = (TextView) findViewById(R.id.moreInfo);
        moreInfoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFullView();
            }
        });
    }

    /**
     * Hide the answer and details, and show the question only
     */
    protected void switchToQuestionView() {
        final View v = findViewById(R.id.answerDetailsContainer);

        // Animate hiding the answer and details by reducing the height to 0
        // Copied from Tom Esterez @ http://stackoverflow.com/questions/4946295
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                    findViewById(R.id.expandAnswerButton).setVisibility(View.VISIBLE);
                    findViewById(R.id.detailsContent).setVisibility(View.GONE);
                    findViewById(R.id.moreInfo).setVisibility(View.VISIBLE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    /**
     * Show the answer and question, but hide the details
     */
    protected void switchToAnswerView() {
        final View v = findViewById(R.id.answerDetailsContainer);

        // Animate showing the answer by increasing the height to it's full height
        // Copied from Tom Esterez @ http://stackoverflow.com/questions/4946295
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        findViewById(R.id.expandAnswerButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.detailsContent).setVisibility(View.GONE);
        findViewById(R.id.moreInfo).setVisibility(View.VISIBLE);
        v.startAnimation(a);
    }

    /**
     * Show the question, answer and details
     */
    protected void switchToFullView() {
        // Hide more info button, but leave whitespace where it used to be to avoid animations
        // clashing
        final View moreInfo = findViewById(R.id.moreInfo);
        moreInfo.setVisibility(View.INVISIBLE);

        // Show details content webview by animating it in
        // Copied from Tom Esterez @ http://stackoverflow.com/questions/4946295
        final View detailsContent = findViewById(R.id.detailsContent);
        detailsContent.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targtetHeight = detailsContent.getMeasuredHeight();
        detailsContent.getLayoutParams().height = 0;
        detailsContent.setVisibility(View.VISIBLE);
        Animation b = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                detailsContent.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                detailsContent.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        b.setDuration((int) (targtetHeight / detailsContent.getContext().getResources().getDisplayMetrics().density));
        detailsContent.startAnimation(b);
    }
}
