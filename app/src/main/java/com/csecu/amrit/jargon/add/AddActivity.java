package com.csecu.amrit.jargon.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.database.DatabaseHandler;
import com.csecu.amrit.jargon.model.ModelWord;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AddActivity extends AppCompatActivity {
    EditText etword, etmeaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etword = (EditText) findViewById(R.id.etAddWord);
        etmeaning = (EditText) findViewById(R.id.etAddMeaning);

        etword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etword.getText().toString().trim().length() <= 0)
                    etword.setError("Enter a word");
                else
                    etword.setError(null);
            }
        });

        etmeaning.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etmeaning.getText().toString().trim().length() <= 0)
                    etmeaning.setError("Enter meaning of the word");
                else
                    etmeaning.setError(null);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.this.getWindow().setSoftInputMode(WindowManager.
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                String word = etword.getText().toString().trim().toLowerCase();
                String meaning = etmeaning.getText().toString().trim().toLowerCase();

                if (word == null || word.equals("")) {
                    etword.setError("Enter a word");
                }

                if (meaning == null || meaning.equals("")) {
                    etmeaning.setError("Enter meaning of the word");
                }

                if (word != null && meaning != null && !word.equals("") && !meaning.equals("")) {
                    boolean result = addWord(word, meaning);
                    if (result) {
                        Snackbar.make(view, "Successful"
                                , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        Snackbar.make(view, "Failed"
                                , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean addWord(String word, String meaning) {
        etword.setText("");
        etmeaning.setText("");

        etword.requestFocus();

        word = encodeString(word);
        meaning = encodeString(meaning);

        ModelWord modelWord = new ModelWord();
        modelWord.setWord(word);
        modelWord.setMeaning(meaning);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        boolean isExist = databaseHandler.checkWordinDatabase(modelWord.getWord());
        if (isExist) {
            toastIt("This word already exists");
            return false;
        } else {
            databaseHandler.addWordtoDatabase(modelWord);
            return true;
        }
    }

    private String encodeString(String word) {
        try {
            return URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return word;
        }
    }

    private void toastIt(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
