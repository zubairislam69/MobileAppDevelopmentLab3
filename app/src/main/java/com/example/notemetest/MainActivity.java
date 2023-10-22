package com.example.notemetest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
    List<Note> allNotes;

    ListView lvNotes;

    SearchView svSearchNote;

    FloatingActionButton fab;

    ArrayAdapter noteArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvNotes = findViewById(R.id.lvNotes);
        svSearchNote = findViewById(R.id.svSearchNotes);
        fab = findViewById(R.id.fabAddNote);

        // Query listener to search for notes
        svSearchNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return false;
            }
        });

        try {
            allNotes = dataBaseHelper.getAllNotes();
            NoteAdapter adapter = new NoteAdapter(this, allNotes);
            lvNotes.setAdapter(adapter);

        } catch(Exception e) {
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewNote.class);
                startActivity(intent);
            }
        });
    }

    public void filterNotes(String query) {
        List <Note> filteredNotes = new ArrayList<>();
        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }

        // Update the ListView with the filtered list of notes
        NoteAdapter adapter = new NoteAdapter(this, filteredNotes);
        lvNotes.setAdapter(adapter);
    }
}


