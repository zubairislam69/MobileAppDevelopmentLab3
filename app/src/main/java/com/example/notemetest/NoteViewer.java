package com.example.notemetest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import yuku.ambilwarna.AmbilWarnaDialog;

public class NoteViewer extends AppCompatActivity {

    int defaultColor;
    Button btnPickColor;

    byte[] selectedImageBytes;
    private final int GALLERY_REQ_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;
    int id;

    EditText etNoteTitle, etNoteSubtitle, etNoteDescription;
    CardView cvDescription;

    FloatingActionButton fabDeleteNote, fabCapturePhoto, fabUploadImage, fabBack, fabSaveNote;
    ImageView ivImageNoteViewer;

    DataBaseHelper dataBaseHelper = new DataBaseHelper(NoteViewer.this);

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
        byte[] image = intent.getByteArrayExtra("note_image");

        this.selectedImageBytes = image;

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteSubtitle = findViewById(R.id.etNoteSubtitle);
        etNoteDescription = findViewById(R.id.etNoteDescription);
        cvDescription = findViewById(R.id.cvDescription);
        ivImageNoteViewer = findViewById(R.id.ivImageNoteViewer);

        etNoteTitle.setText(title);
        etNoteSubtitle.setText(subtitle);
        etNoteDescription.setText(description);
        cvDescription.setCardBackgroundColor(defaultColor);
        ivImageNoteViewer.setImageBitmap(convertBytesToImage(image));

        btnPickColor = findViewById(R.id.btnPickColor);

        fabDeleteNote = findViewById(R.id.fabDeleteNote);

        fabCapturePhoto = findViewById(R.id.fabCapturePhoto);
        fabUploadImage = findViewById(R.id.fabUploadImage);

        fabBack = findViewById(R.id.fabBack);
        fabSaveNote = findViewById(R.id.fabSaveNote);

        btnPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
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

        fabSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteViewer.this, MainActivity.class);

                Note note;

                String title = etNoteTitle.getText().toString().trim();

                if (!title.isEmpty()) {
                    try {
                        note = new Note(id, etNoteTitle.getText().toString(), etNoteSubtitle.getText().toString(),
                                    etNoteDescription.getText().toString(), defaultColor, selectedImageBytes);

                    } catch (Exception e) {
                        Toast.makeText(NoteViewer.this, "Error creating note", Toast.LENGTH_SHORT).show();
                        note = new Note(-1, "error", "error", "error", 0, null);
                    }
                    dataBaseHelper = new DataBaseHelper(NoteViewer.this);

                    dataBaseHelper.updateNote(note);

                } else {
                    Toast.makeText(NoteViewer.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        });

        fabUploadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        fabCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null) {
                    // Handle capturing a photo from the camera
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap capturedImage = (Bitmap) extras.get("data");
                        // Display the image in your ImageView
                        ivImageNoteViewer.setImageBitmap(capturedImage);

                        // Convert the Bitmap to a byte array
                        selectedImageBytes = convertBitmapToBytes(capturedImage);

                    }
                }

            }
            if (requestCode == GALLERY_REQ_CODE) {
                if (data != null) {
                    Uri imageUri = data.getData();

                    // Convert the selected image to bytes
                    selectedImageBytes = convertImageToBytes(imageUri);

                    // Display image
                    ivImageNoteViewer.setImageURI(imageUri);
                }
            }
        }
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

    private byte[] convertImageToBytes(Uri imageUri) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private Bitmap convertBytesToImage(byte[] imageBytes) {
        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }

    private byte[] convertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}