package com.example.notemetest;

import android.content.Context;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_SUBTITLE = "SUBTITLE";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_COLOR = "COLOR";
    public static final String COLUMN_ID = "ID";
    public static final String NOTES_TABLE = "NOTES";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "notes.db", null, 1);
    }

    // This is called the first time a database is accessed. There should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + NOTES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " + COLUMN_SUBTITLE + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_COLOR + " INT)";

        db.execSQL(createTableStatement);
    }

    // This is called if the database version number changes. It prevents previous users apps from
    // breaking when you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, note.getTitle());
        cv.put(COLUMN_SUBTITLE, note.getSubtitle());
        cv.put(COLUMN_DESCRIPTION, note.getDescription());
        cv.put(COLUMN_COLOR, note.getColor());

        long insert = db.insert(NOTES_TABLE, null, cv);
        if (insert == -1){
            return false;
        } else {
            return true;
        }

    }

    public List<Note> getAllNotes() {
        List<Note> returnList = new ArrayList<>();

        // Get data from database

        String queryString = "SELECT * FROM " + NOTES_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            // Loop through cursor and create new note objects. Put them into return list
            do {
                int noteID = cursor.getInt(0);
                String noteTitle = cursor.getString(1);
                String noteSubtitle = cursor.getString(2);
                String noteDescription = cursor.getString(3);
                int noteColor = cursor.getInt(4);

                Note newNote = new Note(noteID, noteTitle, noteSubtitle, noteDescription, noteColor);
                returnList.add(newNote);


            } while (cursor.moveToNext());


        } else {
            // Failure. Do not add anything to list.
        }

//         Close cursor and db.

        cursor.close();
        db.close();


        return returnList;
    }
}
