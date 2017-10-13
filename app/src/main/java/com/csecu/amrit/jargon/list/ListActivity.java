package com.csecu.amrit.jargon.list;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.about.AboutDialog;
import com.csecu.amrit.jargon.add.AddActivity;
import com.csecu.amrit.jargon.copy.CopyActivity;
import com.csecu.amrit.jargon.database.DatabaseHandler;
import com.csecu.amrit.jargon.exam.ExamActivity;
import com.csecu.amrit.jargon.listName.ViewListActivity;
import com.csecu.amrit.jargon.model.ModelWord;
import com.csecu.amrit.jargon.type.TypeActivity;
import com.csecu.amrit.jargon.update.UpdateActivity;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ModelWord> wordArrayList = new ArrayList<>();
    CustomListAdapter adapter;
    String csvFileName = "wordStorage.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        showWords();

        recyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(),
                recyclerView, new RecyclerItemListener.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                ModelWord modelWord = adapter.getItem(position);
                Intent intent = new Intent(ListActivity.this, UpdateActivity.class);
                intent.putExtra("model", modelWord);
                startActivity(intent);
            }

            @Override
            public void onLongClickItem(View v, int position) {
                AlertDialog alertDialog = confirmation(position);
                alertDialog.show();
            }
        }));
    }

    private AlertDialog confirmation(final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure to delete this word")
                .setIcon(R.drawable.remove)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int delete) {
                        DatabaseHandler databaseHandler =
                                new DatabaseHandler(getApplicationContext());
                        ModelWord modelWord = adapter.getItem(position);
                        databaseHandler.deleteItem(modelWord);
                        showWords();
                        toastIt("Deleted");
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
        wordArrayList = databaseHandler.getAllWords();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addWord) {
            Intent intent = new Intent(ListActivity.this, AddActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.createType) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.custom_dialog, null);
            final EditText etType = (EditText) layout.findViewById(R.id.etCustomType);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            toastIt("OK clicked");
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String type = etType.getText().toString().trim();

                            if (type.isEmpty()) {
                                toastIt("Type can't be empty");
                            } else {
                                dialog.cancel();
                                addType(type);
                                alertDialog.getWindow().setSoftInputMode
                                        (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            }
                        }
                    });
                }
            });
            alertDialog.getWindow().setSoftInputMode
                    (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            alertDialog.show();
            return true;
        } else if (id == R.id.createList) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.custom_dialog_list, null);
            final EditText etList = (EditText) layout.findViewById(R.id.etCustomList);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            toastIt("OK clicked");
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String list = etList.getText().toString().trim();

                            if (list.isEmpty()) {
                                toastIt("Type can't be empty");
                            } else {
                                dialog.cancel();
                                alertDialog.getWindow().setSoftInputMode
                                        (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                                Intent intent = new Intent(ListActivity.this, CopyActivity.class);
                                intent.putExtra("list", list);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
            alertDialog.getWindow().setSoftInputMode
                    (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            alertDialog.show();
            return true;
        } else if (id == R.id.viewList) {
            Intent intent = new Intent(ListActivity.this, ViewListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.exam) {
            Intent intent = new Intent(ListActivity.this, ExamActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.allTypes) {
            Intent intent = new Intent(ListActivity.this, TypeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.developerInfo) {
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.show(getSupportFragmentManager(), "about_dialog");
            return true;
        } else if (id == R.id.backup) {
            exportDB();
            return true;
        } else if (id == R.id.importData) {
            readCSV();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readCSV() {
        ArrayList<ModelWord> wordsList = new ArrayList<>();
        try {
            File csvfile = new File(Environment.getExternalStorageDirectory() +
                    "/Jargon/" + csvFileName);
            CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                ModelWord modelWord = new ModelWord();
                String id = nextLine[0];
                String word = nextLine[1];
                String meaning = nextLine[2];
                modelWord.setId(id);
                modelWord.setWord(word);
                modelWord.setMeaning(meaning);
                wordsList.add(modelWord);
            }

            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            ArrayList<ModelWord> modelWords = databaseHandler.getAllWords();

            for (int i = 0; i < wordsList.size(); i++) {
                int count = 0;

                ModelWord object = wordsList.get(i);
                String word = object.getWord();

                for (int j = 0; j < modelWords.size(); j++) {
                    ModelWord object2 = modelWords.get(j);
                    String word2 = object2.getWord();

                    if (word.equals(word2)) {
                        count++;
                    }
                }

                if (count == 0 && !word.equals("word")) {
                    databaseHandler.addWordtoDatabase(object);
                }
            }

            recreate();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportDB() {
        DatabaseHandler dbhelper = new DatabaseHandler(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory() + "/Jargon/", "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, csvFileName);
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            Cursor curCSV = dbhelper.getAllData();
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                String arrStr[] = {String.valueOf(curCSV.getInt(0)), curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            toastIt("Data successfully exported to memory");
        } catch (IOException e) {
            e.printStackTrace();
            toastIt("Data backup failed");
        }
    }

    private void addType(String name) {
        if (name != null && !name.equals("")) {

            DatabaseHandler databaseHandler = new DatabaseHandler(this);
            boolean result = databaseHandler.checkTypeinDatabase(name);
            if (result) {
                toastIt("Type Exists");
            } else {
                databaseHandler.addTypetoDatabase(name);
                toastIt("Successful");
            }
        } else {
            toastIt("This field can't be blank");
        }
    }

}
