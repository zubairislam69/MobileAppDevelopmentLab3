package com.example.notemetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import yuku.ambilwarna.AmbilWarnaDialog;
public class NewNote extends AppCompatActivity {

    EditText etNoteTitle, etNoteSubtitle, etNoteDescription;
    Button backButton, saveNoteButton, btnPickColor;
    int defaultColor = Color.WHITE;
    CardView cvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        backButton = findViewById(R.id.btnBack);
        saveNoteButton = findViewById(R.id.btnSaveNote);
        btnPickColor = findViewById(R.id.btnPickColor);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteSubtitle = findViewById(R.id.etNoteSubtitle);
        etNoteDescription = findViewById(R.id.etNoteDescription);

        cvDescription = findViewById(R.id.cvDescription);

        btnPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNote.this, MainActivity.class);
                startActivity(intent);
            }
        });

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNote.this, MainActivity.class);

                Note note;

                String title = etNoteTitle.getText().toString().trim();

                // If title is empty, don't let user make a note, and tell them that Title cant be empty
                if (!title.isEmpty()) {
                    try {
                        // Send new note info to the Note class
                        note = new Note(-1, etNoteTitle.getText().toString(), etNoteSubtitle.getText().toString(),
                                etNoteDescription.getText().toString(), defaultColor);

                    } catch (Exception e) {
                        Toast.makeText(NewNote.this, "Error creating note", Toast.LENGTH_SHORT).show();
                        note = new Note(-1, "error", "error", "error", 0);
                    }

                    DataBaseHelper dataBaseHelper = new DataBaseHelper(NewNote.this);
                    dataBaseHelper.addOne(note);
                } else {
                    Toast.makeText(NewNote.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
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