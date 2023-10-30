package com.example.notemetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class NoteViewer extends AppCompatActivity {

    EditText etNoteTitle, etNoteSubtitle, etNoteDescription;
    CardView cvDescription;
    int defaultColor = Color.WHITE;
    Button backButton, saveNoteButton, btnPickColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);


        // Retrieve note details from the intent
        Intent intent = getIntent();

        int id = intent.getIntExtra("note_id", -1);

        String title = intent.getStringExtra("note_title");
        String subtitle = intent.getStringExtra("note_subtitle");
        String description = intent.getStringExtra("note_description");
        int color = intent.getIntExtra("note_color", Color.WHITE);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteSubtitle = findViewById(R.id.etNoteSubtitle);
        etNoteDescription = findViewById(R.id.etNoteDescription);
        cvDescription = findViewById(R.id.cvDescription);

        etNoteTitle.setText(title);
        etNoteSubtitle.setText(subtitle);
        etNoteDescription.setText(description);
        cvDescription.setCardBackgroundColor(color);

        backButton = findViewById(R.id.btnBack);
        saveNoteButton = findViewById(R.id.btnSaveNote);
        btnPickColor = findViewById(R.id.btnPickColor);

        btnPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteViewer.this, MainActivity.class);
                startActivity(intent);
            }
        });

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteViewer.this, MainActivity.class);

                Note note;
//                Note updatedNote;

                String title = etNoteTitle.getText().toString().trim();

                // If title is empty, don't let user make a note, and tell them that Title cant be empty
                if (!title.isEmpty()) {
                    try {
                        // Send new note info to the Note class
                        note = new Note(id, etNoteTitle.getText().toString(), etNoteSubtitle.getText().toString(),
                                etNoteDescription.getText().toString(), color);


                    } catch (Exception e) {
                        Toast.makeText(NoteViewer.this, "Error creating note", Toast.LENGTH_SHORT).show();
                        note = new Note(-1, "error", "error", "error", 0);
                    }
                    Toast.makeText(NoteViewer.this, note.toString(), Toast.LENGTH_SHORT).show();

                    DataBaseHelper dataBaseHelper = new DataBaseHelper(NoteViewer.this);
                    dataBaseHelper.updateNote(note);

                } else {
                    Toast.makeText(NoteViewer.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                }

                startActivity(intent);

            }
        });
    }

    public void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            // Set the note color
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                cvDescription.setCardBackgroundColor(defaultColor);
            }
        });

        ambilWarnaDialog.show();
    }
}
