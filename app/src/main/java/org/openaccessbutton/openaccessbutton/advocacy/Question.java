package org.openaccessbutton.openaccessbutton.advocacy;

/**
 * Created by rickards on 9/30/14.
 */
public class Question {
    String mQuestion;
    String mAnswer;

    public Question(String question, String answer) {
        mQuestion = question;
        mAnswer = answer;
    }

    public String question() {
        return mQuestion;
    }

    public String answer(String placeholder) {
        if (mAnswer == null || mAnswer.length() == 0) {
            return placeholder;
        } else {
            return mAnswer;
        }
    }
}
