package org.openaccessbutton.openaccessbutton.advocacy;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goebl.david.Webb;
import com.parse.ParseInstallation;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openaccessbutton.openaccessbutton.R;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class QuestionsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadQuestionsTask task = new LoadQuestionsTask(this);
        task.execute();

    }

    protected class QuestionsArrayAdapter extends ArrayAdapter<Question> {
        private final Context context;
        private final Question[] questions;

        public QuestionsArrayAdapter(Context context, Question[] questions) {
            super(context, R.layout.question_list_item, questions);
            this.context = context;
            this.questions = questions;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.question_list_item, parent, false);

            TextView questionView = (TextView) rowView.findViewById(R.id.question_question);
            TextView answerView = (TextView) rowView.findViewById(R.id.question_answer);
            questionView.setText(questions[position].question());
            answerView.setText(questions[position].answer(getResources().getString(R.string.answerPlaceholder)));

            return rowView;
        }
    }

    protected class LoadQuestionsTask extends AsyncTask<Void, Void, Question[]> {
        private Context mContext;
        private ProgressDialog mProgress;

        public LoadQuestionsTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            // Show progress dialog
            mProgress = new ProgressDialog(mContext);
            mProgress.setTitle("Loading");
            mProgress.setMessage("Please wait...");
            mProgress.show();
        }

        @Override
        protected Question[] doInBackground(Void... params) {
            // User ID from Parse so we can tie back questions to this specific installation
            String user_id;
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            if (installation == null) {
                user_id = "";
            } else {
                user_id = installation.getObjectId();
            }

            Webb webb = Webb.create();
            JSONObject result = webb
                    .get("http://oabuttonquestions.herokuapp.com/questions.json")
                    .param("user_id", user_id)
                    .ensureSuccess()
                    .asJsonObject()
                    .getBody();


            Question[] questions;

            try {
                JSONArray questionsJson = result.getJSONArray("questions");
                questions = new Question[questionsJson.length()];
                for(int i=0; i<questionsJson.length(); i++) {
                    JSONObject questionJson = questionsJson.getJSONObject(i);
                    Question question = new Question(questionJson.getString("question"), questionJson.getString("answer"));
                    questions[i] = question;
                }

            } catch (Exception e) {
                questions = null;
                e.printStackTrace();
                // TODO Do something here
            }

            return questions;
        }

        @Override
        protected void onPostExecute(Question[] questions) {
            mProgress.dismiss();

            if (questions == null || questions.length == 0) {
                questions = new Question[] {new Question(getResources().getString(R.string.noQuestionsTitle), getResources().getString(R.string.noQuestionsDescription)) };
            }

            QuestionsArrayAdapter adapter = new QuestionsArrayAdapter(mContext, questions);
            setListAdapter(adapter);

        }
    }
}
