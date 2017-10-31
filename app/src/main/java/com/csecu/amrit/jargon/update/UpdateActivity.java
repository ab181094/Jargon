package com.csecu.amrit.jargon.update;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.database.DatabaseHandler;
import com.csecu.amrit.jargon.model.ModelWord;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    EditText etword, etmeaning;
    Spinner spType, spList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etword = (EditText) findViewById(R.id.etUpdateWord);
        etmeaning = (EditText) findViewById(R.id.etUpdateMeaning);
        spType = (Spinner) findViewById(R.id.spUpdateType);
        spList = (Spinner) findViewById(R.id.spUpdateList);

        final ModelWord modelWord = (ModelWord) getIntent().getParcelableExtra("model");
        showInfo(modelWord);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateActivity.this.getWindow().setSoftInputMode(WindowManager.
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                String word = etword.getText().toString().trim().toLowerCase();
                String meaning = etmeaning.getText().toString().trim().toLowerCase();
                String type = (String) spType.getSelectedItem();
                String list = (String) spList.getSelectedItem();

                word = encodeString(word);
                meaning = encodeString(meaning);

                ModelWord modelWord1 = new ModelWord();
                modelWord1.setWord(word);
                modelWord1.setMeaning(meaning);
                modelWord1.setType(type);
                modelWord1.setList(list);

                int index = Integer.parseInt(modelWord.getId());

                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                databaseHandler.updateWord(modelWord1, index);

                Snackbar.make(view, "Updated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showInfo(ModelWord modelWord) {
        String word = modelWord.getWord();
        String meaning = modelWord.getMeaning();

        word = decodeString(word);
        meaning = decodeString(meaning);

        etword.setText(word);
        etmeaning.setText(meaning);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        ArrayList<String> typeList = databaseHandler.getAllTypes();
        if (typeList.size() != 0) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                    R.layout.spinner_item, typeList);
            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
            spType.setAdapter(dataAdapter);

            if (modelWord.getType() != null) {
                int spinnerPosition = dataAdapter.getPosition(modelWord.getType());
                spType.setSelection(spinnerPosition);
            }
        }

        ArrayList<String> list = databaseHandler.getAllLists();
        if (list.size() != 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.spinner_item, list);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            spList.setAdapter(adapter);

            if (modelWord.getList() != null) {
                int spinnerPosition = adapter.getPosition(modelWord.getList());
                spList.setSelection(spinnerPosition);
            }
        }
    }

    private void toastIt(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private String encodeString(String word) {
        try {
            return URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return word;
        }
    }

    private String decodeString(String realAnswer) {
        try {
            realAnswer = URLDecoder.decode(realAnswer, "UTF-8");
            return realAnswer;
        } catch (UnsupportedEncodingException e) {
            return realAnswer;
        }
    }

}
