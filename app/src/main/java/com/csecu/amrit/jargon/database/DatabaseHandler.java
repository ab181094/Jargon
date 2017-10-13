package com.csecu.amrit.jargon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.csecu.amrit.jargon.model.ModelWord;

import java.util.ArrayList;

/**
 * Created by Amrit on 11-09-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wordStorage";
    private static final String TABLE_WORD = "wordTable";
    private static final String TABLE_TYPE = "typeTable";
    private static final String KEY_ID = "_id";
    private static final String KEY_WORD = "word";
    private static final String KEY_MEANING = "meaning";
    private static final String KEY_TYPE = "type";
    private static final String KEY_LIST = "list";

    String CREATE_TYPE_TABLE = "CREATE TABLE " + TABLE_TYPE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TYPE + " TEXT" + ")";

    String CREATE_WORD_TABLE = "CREATE TABLE " + TABLE_WORD + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_WORD + " TEXT,"
            + KEY_MEANING + " TEXT," + KEY_TYPE + " TEXT," + KEY_LIST + " TEXT" + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TYPE_TABLE);
        sqLiteDatabase.execSQL(CREATE_WORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        onCreate(sqLiteDatabase);
    }

    public void addWordtoDatabase(ModelWord modelWord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WORD, modelWord.getWord());
        values.put(KEY_MEANING, modelWord.getMeaning());

        db.insert(TABLE_WORD, null, values);
        db.close();
    }

    public boolean checkWordinDatabase(String word) {
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABLE_WORD + " WHERE "
                + KEY_WORD + " = '"+ word +"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        if (count == 0)
            return false;
        else
            return true;
    }

    public ArrayList<ModelWord> getAllWords() {
        ArrayList<ModelWord> wordList = new ArrayList<ModelWord>();
        String selectQuery = "SELECT * FROM " + TABLE_WORD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelWord modelWord = new ModelWord();
                modelWord.setId(String.valueOf(cursor.getInt(0)));
                modelWord.setWord(cursor.getString(1));
                modelWord.setMeaning(cursor.getString(2));
                modelWord.setType(cursor.getString(3));
                modelWord.setList(cursor.getString(4));
                wordList.add(modelWord);
            } while (cursor.moveToNext());
        }

        return wordList;
    }

    public void addTypetoDatabase(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, name);

        db.insert(TABLE_TYPE, null, values);
        db.close();
    }


    public boolean checkTypeinDatabase(String name) {
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABLE_TYPE + " WHERE "
                + KEY_TYPE + " = '"+ name +"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        if (count == 0)
            return false;
        else
            return true;
    }

    public ArrayList<String> getAllTypes() {
        ArrayList<String> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TYPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1) != null) {
                    list.add(cursor.getString(1));
                } else
                    continue;
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<String> getAllLists() {
        ArrayList<String> list = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT " + KEY_LIST + " FROM " + TABLE_WORD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) != null) {
                    list.add(cursor.getString(0));
                } else
                    continue;
            } while (cursor.moveToNext());
        }

        return list;
    }

    public void updateWord(ModelWord modelWord, int index) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WORD, modelWord.getWord());
        values.put(KEY_MEANING, modelWord.getMeaning());
        values.put(KEY_TYPE, modelWord.getType());
        values.put(KEY_LIST, modelWord.getList());

        db.update(TABLE_WORD, values, KEY_ID + " = " +index, null);
        db.close();
    }

    public void updateAll(ArrayList<ModelWord> wordList, String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < wordList.size(); i++) {
            ModelWord modelWord = wordList.get(i);
            int index = Integer.parseInt(modelWord.getId());

            ContentValues values = new ContentValues();
            values.put(KEY_LIST, listName);

            db.update(TABLE_WORD, values, KEY_ID + " = " +index, null);
        }
        db.close();
    }

    public void deleteItem(ModelWord modelWord) {
        SQLiteDatabase db = this.getWritableDatabase();
        int index = Integer.parseInt(modelWord.getId());
        db.delete(TABLE_WORD, KEY_ID + "=" + index, null);
    }

    public ArrayList<ModelWord> getListWords(String listName) {
        ArrayList<ModelWord> wordList = new ArrayList<ModelWord>();
        String selectQuery = "SELECT * FROM " + TABLE_WORD + " WHERE "
                + KEY_LIST + " = '"+ listName +"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelWord modelWord = new ModelWord();
                modelWord.setId(String.valueOf(cursor.getInt(0)));
                modelWord.setWord(cursor.getString(1));
                modelWord.setMeaning(cursor.getString(2));
                modelWord.setType(cursor.getString(3));
                modelWord.setList(cursor.getString(4));
                wordList.add(modelWord);
            } while (cursor.moveToNext());
        }

        return wordList;
    }

    public void removeList(String string) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIST, (byte[]) null);

        db.update(TABLE_WORD, values, KEY_LIST + " = ? ",new String[]{string});
        db.close();
    }

    public void removeType(String string) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, (byte[]) null);

        db.update(TABLE_WORD, values, KEY_TYPE + " = ? ",new String[]{string});
        db.close();

        db = this.getWritableDatabase();
        db.delete(TABLE_TYPE, KEY_TYPE + " = ?", new String[] {string});
        db.close();
    }

    public ArrayList<String> getAllAnswers(String realAnswer) {
        ArrayList<String> list = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_WORD + " FROM " + TABLE_WORD + " WHERE "
                + KEY_MEANING + " = '"+ realAnswer +"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) != null) {
                    list.add(cursor.getString(0));
                } else
                    continue;
            } while (cursor.moveToNext());
        }

        return list;
    }

    public Cursor getAllData() {
        String selectQuery = "SELECT * FROM " + TABLE_WORD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
}
