package com.example.notemetest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    private final OnNoteClickListener onNoteClickListener;

    public NoteAdapter(Context context, List<Note> notes, OnNoteClickListener onNoteClickListener) {
        super(context, 0, notes);
        this.onNoteClickListener = onNoteClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_note, parent, false);
        }

        Note note = getItem(position);

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvSubtitle = convertView.findViewById(R.id.tvSubtitle);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);

        ImageView ivImageNote = convertView.findViewById(R.id.ivImageNote);

        tvTitle.setText(note.getTitle());
        tvSubtitle.setText(note.getSubtitle());
        tvDescription.setText(note.getDescription());

        ivImageNote.setImageBitmap(convertBytesToImage(note.getImage()));

        // Set color selected to note background
        convertView.setBackgroundColor(note.getColor());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNoteClickListener.onNoteClick(note);
            }
        });

        return convertView;
    }

    // Function to convert the byte array to an image
    private Bitmap convertBytesToImage(byte[] imageBytes) {
        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }
}