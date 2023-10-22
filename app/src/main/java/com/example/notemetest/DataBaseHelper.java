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
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + NOTES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " + COLUMN_SUBTITLE + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_COLOR + " INT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addOne(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, note.getTitle());
        cv.put(COLUMN_SUBTITLE, note.getSubtitle());
        cv.put(COLUMN_DESCRIPTION, note.getDescription());
        cv.put(COLUMN_COLOR, note.getColor());

        db.insert(NOTES_TABLE, null, cv);
    }

    public List<Note> getAllNotes() {
        List<Note> returnList = new ArrayList<>();

        // Get all notes in DB
        String queryString = "SELECT * FROM " + NOTES_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {

            // Loop through cursor and create new note objects and then put them into return list
            do {
                int noteID = cursor.getInt(0);
                String noteTitle = cursor.getString(1);
                String noteSubtitle = cursor.getString(2);
                String noteDescription = cursor.getString(3);
                int noteColor = cursor.getInt(4);

                Note newNote = new Note(noteID, noteTitle, noteSubtitle, noteDescription, noteColor);
                returnList.add(newNote);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase(); // Assuming you have a DatabaseHelper class

        String tableName = "NOTES";

        db.delete(tableName, null, null);

        db.close();
    }
}
