package com.example.notemetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import yuku.ambilwarna.AmbilWarnaDialog;

public class NoteViewer extends AppCompatActivity {

    EditText etNoteTitle, etNoteSubtitle, etNoteDescription;
    CardView cvDescription;
    int defaultColor;
    Button backButton, saveNoteButton, btnPickColor;

    FloatingActionButton fabDeleteNote;

    DataBaseHelper dataBaseHelper = new DataBaseHelper(NoteViewer.this);

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);


        Intent intent = getIntent();

        id = intent.getIntExtra("note_id", -1);

        String title = intent.getStringExtra("note_title");
        String subtitle = intent.getStringExtra("note_subtitle");
        String description = intent.getStringExtra("note_description");
        defaultColor = intent.getIntExtra("note_color", Color.WHITE);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteSubtitle = findViewById(R.id.etNoteSubtitle);
        etNoteDescription = findViewById(R.id.etNoteDescription);
        cvDescription = findViewById(R.id.cvDescription);

        etNoteTitle.setText(title);
        etNoteSubtitle.setText(subtitle);
        etNoteDescription.setText(description);
        cvDescription.setCardBackgroundColor(defaultColor);

        backButton = findViewById(R.id.btnBack);
        saveNoteButton = findViewById(R.id.btnSaveNote);
        btnPickColor = findViewById(R.id.btnPickColor);

        fabDeleteNote = findViewById(R.id.fabDeleteNote);

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

        fabDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBaseHelper != null && id != -1) {
                    dataBaseHelper.deleteNote(id);
                    Toast.makeText(NoteViewer.this, "Note deleted", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(NoteViewer.this, MainActivity.class);
                startActivity(intent);
            }
        });

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteViewer.this, MainActivity.class);

                Note note;

                String title = etNoteTitle.getText().toString().trim();

                if (!title.isEmpty()) {
                    try {
                        note = new Note(id, etNoteTitle.getText().toString(), etNoteSubtitle.getText().toString(),
                                etNoteDescription.getText().toString(), defaultColor);

                    } catch (Exception e) {
                        Toast.makeText(NoteViewer.this, "Error creating note", Toast.LENGTH_SHORT).show();
                        note = new Note(-1, "error", "error", "error", 0);
                    }
                    dataBaseHelper = new DataBaseHelper(NoteViewer.this);
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