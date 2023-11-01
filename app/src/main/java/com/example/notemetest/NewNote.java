package com.example.notemetest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import yuku.ambilwarna.AmbilWarnaDialog;
public class NewNote extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;

    //upload
    byte[] selectedImageBytes;

    //camera
    byte[] imageBytes;


    EditText etNoteTitle, etNoteSubtitle, etNoteDescription;
    Button btnPickColor;
    FloatingActionButton fabCapturePhoto, fabUploadImage, fabBack, fabSaveNote;
    int defaultColor = Color.WHITE;
    CardView cvDescription;
    ImageView ivImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);



        btnPickColor = findViewById(R.id.btnPickColor);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteSubtitle = findViewById(R.id.etNoteSubtitle);
        etNoteDescription = findViewById(R.id.etNoteDescription);

        cvDescription = findViewById(R.id.cvDescription);

        fabCapturePhoto = findViewById(R.id.fabCapturePhoto);
        fabUploadImage = findViewById(R.id.fabUploadImage);

        fabBack = findViewById(R.id.fabBack);
        fabSaveNote = findViewById(R.id.fabSaveNote);

        ivImage = findViewById(R.id.ivImage);

        btnPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNote.this, MainActivity.class);
                startActivity(intent);
            }
        });

        fabSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNote.this, MainActivity.class);

                Note note;

                String title = etNoteTitle.getText().toString().trim();

                // If title is empty, don't let user make a note, and tell them that Title cant be empty
                if (!title.isEmpty()) {
                    try {

                        //if image is selected
                        if (selectedImageBytes != null) {
                            note = new Note(-1, etNoteTitle.getText().toString(), etNoteSubtitle.getText().toString(),
                                    etNoteDescription.getText().toString(), defaultColor, selectedImageBytes);
                        } else {
                            note = new Note(-1, etNoteTitle.getText().toString(), etNoteSubtitle.getText().toString(),
                                    etNoteDescription.getText().toString(), defaultColor, null);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(NewNote.this, "Error creating note", Toast.LENGTH_SHORT).show();
                        note = new Note(-1, "error", "error", "error", 0, null);

                    }

                    DataBaseHelper dataBaseHelper = new DataBaseHelper(NewNote.this);
                    dataBaseHelper.addOne(note);
                } else {
                    Toast.makeText(NewNote.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        });

        fabUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQ_CODE);
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
                        ivImage.setImageBitmap(capturedImage);

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
                    ivImage.setImageURI(imageUri);
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

    // Function to convert an image to a byte array for DB storing
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


    private byte[] convertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}