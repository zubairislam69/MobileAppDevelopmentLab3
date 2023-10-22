package com.example.notemetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;
import android.util.Log;
public class NewNote extends AppCompatActivity {

    EditText etNoteTitle, etNoteSubtitle, etNoteDescription;
    int defaultColor;
    CardView cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Button backButton = findViewById(R.id.btnBack);
        Button saveNoteButton = findViewById(R.id.btnSaveNote);
        Button btnPickColor = findViewById(R.id.btnPickColor);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteSubtitle = findViewById(R.id.etNoteSubtitle);
        etNoteDescription = findViewById(R.id.etNoteDescription);

        cv = findViewById(R.id.cvDescription);

        defaultColor = Color.WHITE;

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
                startActivity(intent);

                Note note;

                try {
                    note = new Note(-1, etNoteTitle.getText().toString(), etNoteSubtitle.getText().toString(),
                            etNoteDescription.getText().toString(), defaultColor);

                    Toast.makeText(NewNote.this, note.toString(), Toast.LENGTH_SHORT).show();

                    Log.d("note", note.toString());


                } catch (Exception e) {
                    Toast.makeText(NewNote.this, "Error creating note", Toast.LENGTH_SHORT).show();
                    note = new Note(-1, "error", "error", "error", 0);
                }


                DataBaseHelper dataBaseHelper = new DataBaseHelper(NewNote.this);
                boolean success = dataBaseHelper.addOne(note);
                Toast.makeText(NewNote.this, "Success = " + success, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                cv.setCardBackgroundColor(defaultColor);
            }
        });

        ambilWarnaDialog.show();
    }


}