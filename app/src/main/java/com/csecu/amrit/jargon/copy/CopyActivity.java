package com.csecu.amrit.jargon.copy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.database.DatabaseHandler;
import com.csecu.amrit.jargon.listName.ViewListActivity;
import com.csecu.amrit.jargon.model.ModelWord;

import java.util.ArrayList;

public class CopyActivity extends AppCompatActivity {

    String listName = null;
    ListView listView;
    ArrayList<ModelWord> wordList = new ArrayList<>();
    CustomCopyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.copyListView);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            listName = bundle.getString("list");
        }

        showList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
                ModelWord modelWord = (ModelWord) parent.getAdapter().getItem(position);
                if (wordList.contains(modelWord)) {
                    wordList.remove(modelWord);
                } else {
                    wordList.add(modelWord);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wordList != null && wordList.size() > 0 && listName != null) {
                    DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                    databaseHandler.updateAll(wordList, listName);
                } else {
                    toastIt("No word selected for private listName");
                }
                Snackbar.make(view, "Saved successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(CopyActivity.this, ViewListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showList() {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        ArrayList<ModelWord> wordArrayList = databaseHandler.getAllWords();

        if (wordArrayList != null && wordArrayList.size() > 0) {
            adapter = new CustomCopyAdapter(this, wordArrayList);
            listView.setAdapter(adapter);
        } else if (wordArrayList.size() == 0) {
            toastIt("No data found");
        }
    }

    private void toastIt(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
