package com.example.notemetest;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
    List<Note> everyNote;

    ListView lvNotes;

    ArrayAdapter noteArrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvNotes = findViewById(R.id.lvNotes);

        try {
            everyNote = dataBaseHelper.getAllNotes();
//            noteArrayAdapter = new ArrayAdapter<Note>(MainActivity.this, android.R.layout.simple_list_item_1, everyNote);
            NoteAdapter adapter = new NoteAdapter(this, everyNote);
            lvNotes.setAdapter(adapter);

        } catch(Exception e) {
        }



        Toast.makeText(MainActivity.this, everyNote.toString(), Toast.LENGTH_SHORT).show();


        FloatingActionButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewNote.class);
                startActivity(intent);
            }
        });
    }
}


