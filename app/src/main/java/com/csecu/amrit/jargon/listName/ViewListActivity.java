package com.csecu.amrit.jargon.listName;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.database.DatabaseHandler;
import com.csecu.amrit.jargon.list.RecyclerItemListener;
import com.csecu.amrit.jargon.listWord.ViewListWordActivity;
import com.csecu.amrit.jargon.model.ModelWord;

import java.util.ArrayList;

public class ViewListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> stringList = new ArrayList<>();
    CustomListNameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rvViewList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        showWords();

        recyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(),
                recyclerView, new RecyclerItemListener.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                String string = adapter.getItem(position);
                Intent intent = new Intent(ViewListActivity.this, ViewListWordActivity.class);
                intent.putExtra("listName", string);
                startActivity(intent);
            }

            @Override
            public void onLongClickItem(View v, int position) {
                AlertDialog alertDialog = confirmation(position);
                alertDialog.show();
            }
        }));

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

    private AlertDialog confirmation(final int position) {
        final AlertDialog alertDialog =new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure to delete this list")
                .setIcon(R.drawable.remove)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int delete) {
                        DatabaseHandler databaseHandler =
                                new DatabaseHandler(getApplicationContext());
                        String string = adapter.getItem(position);
                        databaseHandler.removeList(string);
                        toastIt("Deleted");
                        recreate();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        return alertDialog;
    }

    private void showWords() {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        stringList = databaseHandler.getAllLists();

        if (stringList != null && stringList.size() > 0) {
            adapter = new CustomListNameAdapter(stringList);
            recyclerView.setAdapter(adapter);
        } else {
            toastIt("No data found");
        }
    }

    private void toastIt(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
