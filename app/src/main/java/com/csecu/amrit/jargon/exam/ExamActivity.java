package com.csecu.amrit.jargon.exam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.copy.CopyActivity;
import com.csecu.amrit.jargon.database.DatabaseHandler;
import com.csecu.amrit.jargon.list.ListActivity;
import com.csecu.amrit.jargon.listName.ViewListActivity;
import com.csecu.amrit.jargon.model.ModelWord;
import com.csecu.amrit.jargon.result.ResultActivity;

import java.util.ArrayList;
import java.util.Collections;

public class ExamActivity extends AppCompatActivity {

    String listName = null;
    ArrayList<ModelWord> wordList = new ArrayList<>();
    ArrayList<Integer> sequenceList = new ArrayList<>();
    TextView tvTotal, tvCorrect, tvWrong, tvQuestion;
    EditText etAnswer;
    ToggleButton toggleButton;
    boolean toggle = false;

    int totalWords = 0, serial = 0, totalCorrect = 0, totalWrong = 0;
    String question = null, realAnswer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTotal = (TextView) findViewById(R.id.tvExamTotal);
        tvCorrect = (TextView) findViewById(R.id.tvExamCorrect);
        tvWrong = (TextView) findViewById(R.id.tvExamWrong);
        tvQuestion = (TextView) findViewById(R.id.tvExamQuestion);
        etAnswer = (EditText) findViewById(R.id.etExamAns);
        toggleButton = (ToggleButton) findViewById(R.id.tgExam);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            listName = bundle.getString("listName");
            toggle = bundle.getBoolean("toggle");
        }

        if (listName != null) {
            DatabaseHandler databaseHandler = new DatabaseHandler(this);
            wordList = databaseHandler.getListWords(listName);
            startExam(wordList);
        } else {
            DatabaseHandler databaseHandler = new DatabaseHandler(this);
            wordList = databaseHandler.getAllWords();
            startExam(wordList);
        }

        toggleButton.setChecked(toggle);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggle = true;
                    Intent intent1 = new Intent(ExamActivity.this, ExamActivity.class);
                    intent1.putExtra("toggle", toggle);
                    if (listName != null) {
                        intent1.putExtra("listName", listName);
                    }
                    startActivity(intent1);
                    finish();
                } else {
                    toggle = false;
                    Intent intent1 = new Intent(ExamActivity.this, ExamActivity.class);
                    intent1.putExtra("toggle", toggle);
                    if (listName != null) {
                        intent1.putExtra("listName", listName);
                    }
                    startActivity(intent1);
                    finish();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExamActivity.this.getWindow().setSoftInputMode(WindowManager.
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                String answer = etAnswer.getText().toString().trim().toLowerCase();
                DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
                if (answer.equals(realAnswer)) {
                    totalCorrect++;
                    etAnswer.setText("");
                    getQuestion();

                    Snackbar.make(view, "Correct Answer", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    if (toggle) {
                        ArrayList<String> answerList = handler.getAllAnswers(question);
                        int count = 0;
                        for (int i = 0; i < answerList.size(); i++) {
                            if (answer.equals(answerList.get(i))) {
                                count++;
                            } else
                                continue;
                        }
                        if (count > 0) {
                            totalCorrect++;
                            etAnswer.setText("");
                            getQuestion();

                            Snackbar.make(view, "Correct Answer", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            totalWrong++;
                            etAnswer.setText("");
                            Snackbar.make(view, "Wrong Answer. Correct Answer is " + realAnswer,
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            getQuestion();
                        }
                    } else {
                        ArrayList<String> answerList = handler.getAllAnswers(realAnswer);
                        int count = 0;
                        for (int i = 0; i < answerList.size(); i++) {
                            if (answer.equals(answerList.get(i))) {
                                count++;
                            } else
                                continue;
                        }
                        if (count > 0) {
                            totalCorrect++;
                            etAnswer.setText("");
                            getQuestion();

                            Snackbar.make(view, "Correct Answer", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            totalWrong++;
                            etAnswer.setText("");
                            Snackbar.make(view, "Wrong Answer. Correct Answer: " + realAnswer,
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            getQuestion();
                        }
                    }
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startExam(ArrayList<ModelWord> wordList) {
        totalWords = wordList.size();
        for (int i = 0; i < wordList.size(); i++) {
            sequenceList.add(i);
        }

        Collections.shuffle(sequenceList);
        getQuestion();
    }

    private void getQuestion() {
        if (serial < totalWords) {
            ModelWord modelWord = wordList.get(sequenceList.get(serial));
            if (toggle) {
                question = modelWord.getMeaning();
                setScore(totalWords, totalCorrect, totalWrong, question);
                realAnswer = modelWord.getWord();
            } else {
                question = modelWord.getWord();
                setScore(totalWords, totalCorrect, totalWrong, question);
                realAnswer = modelWord.getMeaning();
            }

            serial++;
        } else if (serial == totalWords) {
            setScore(totalWords, totalCorrect, totalWrong);
            toastIt("Exam completed");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
                    intent.putExtra("correct", totalCorrect);
                    intent.putExtra("wrong", totalWrong);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
    }

    private void setScore(int totalWords, int totalCorrect, int totalWrong) {
        tvTotal.setText(String.valueOf(this.totalWords));
        tvCorrect.setText(String.valueOf(this.totalCorrect));
        tvWrong.setText(String.valueOf(this.totalWrong));
        tvQuestion.setText("");
    }

    private void setScore(int totalWords, int totalCorrect, int totalWrong, String question) {
        tvTotal.setText(String.valueOf(this.totalWords));
        tvCorrect.setText(String.valueOf(this.totalCorrect));
        tvWrong.setText(String.valueOf(this.totalWrong));
        tvQuestion.setText(question.substring(0, 1).toUpperCase() + question.substring(1));
    }

    private void toastIt(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
