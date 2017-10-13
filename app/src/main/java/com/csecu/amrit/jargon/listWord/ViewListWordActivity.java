package com.csecu.amrit.jargon.listWord;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.add.AddActivity;
import com.csecu.amrit.jargon.database.DatabaseHandler;
import com.csecu.amrit.jargon.exam.ExamActivity;
import com.csecu.amrit.jargon.list.CustomListAdapter;
import com.csecu.amrit.jargon.list.ListActivity;
import com.csecu.amrit.jargon.model.ModelWord;

import java.util.ArrayList;

public class ViewListWordActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ModelWord> wordArrayList = new ArrayList<>();
    CustomListAdapter adapter;
    String listName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_word);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rvViewListWord);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            listName = bundle.getString("listName");
        }

        showWords(listName);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.exam) {
            Intent intent = new Intent(ViewListWordActivity.this, ExamActivity.class);
            if (listName != null) {
                intent.putExtra("listName", listName);
            }
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showWords(String listName) {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        wordArrayList = databaseHandler.getListWords(listName);

        if (wordArrayList != null && wordArrayList.size() > 0) {
            adapter = new CustomListAdapter(wordArrayList);
            recyclerView.setAdapter(adapter);
        } else {
            toastIt("No data found");
        }
    }

    private void toastIt(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
