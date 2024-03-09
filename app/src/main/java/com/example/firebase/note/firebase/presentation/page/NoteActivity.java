package com.example.firebase.note.firebase.presentation.page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.firebase.R;
import com.example.firebase.note.firebase.domain.callback.NoteLoadedCallback;
import com.example.firebase.note.firebase.domain.entities.NoteEntity;
import com.example.firebase.note.firebase.domain.usecases.NoteUseCases;
import com.example.firebase.note.firebase.presentation.listener.NoteClickListener;
import com.example.firebase.note.firebase.presentation.widget.NoteHolder;
import com.example.firebase.note.firebase.presentation.widget.DatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends NoteUseCases implements NoteClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private final List<NoteEntity> noteList = new ArrayList<>();
    private NoteHolder noteAdapter;
    private ImageView uploadImageView;
    private Uri imageUri;

    /** @noinspection deprecation*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == requireActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(uploadImageView);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_firebase, container, false);

        ExtendedFloatingActionButton extendedFab = rootView.findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> onNoteAdd(inflater, container));

        RecyclerView itemGalleryRecyclerView = rootView.findViewById(R.id.galleryRecyclerView);
        itemGalleryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        noteAdapter = new NoteHolder(noteList, this);
        itemGalleryRecyclerView.setAdapter(noteAdapter);

        onNoteRead();
        return rootView;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onNoteRead() {
        noteList.clear();
        readNoteService(new NoteLoadedCallback() {
            @Override
            public void onNotesLoaded(List<NoteEntity> notes) {
                noteList.addAll(notes);
                noteAdapter.notifyDataSetChanged();
            }
        });
    }

    /** @noinspection deprecation*/
    @Override
    public void onNoteAdd(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View dialogView = inflater.inflate(R.layout.firebase_upload, container, false);
        EditText _titleEditText = dialogView.findViewById(R.id.firebase_Upload_Title_Edit_Text);
        EditText _noteEditText = dialogView.findViewById(R.id.firebase_Upload_Note_Edit_Text);
        TextView _dateTextView = dialogView.findViewById(R.id.firebase_Upload_Date_Text_View);
        uploadImageView = dialogView.findViewById(R.id.firebase_Upload_Image_View);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Add New Note")
                .setView(dialogView)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String _title = _titleEditText.getText().toString();
                    String _note = _noteEditText.getText().toString();
                    String _date = _dateTextView.getText().toString();
                    String _fileName = "image_" + System.currentTimeMillis() + ".jpg";

                    if (addNoteChecker(_title, _note, _date, imageUri, _fileName)) {
                        boolean isAdded = addNoteService(_title, _note, _date, imageUri, _fileName);
                        if (isAdded) {
                            onNoteRead();
                            Toast.makeText(requireContext(), "Note added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Failed to add note. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to add note: Invalid data", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();

        uploadImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        _dateTextView.setOnClickListener(view -> DatePicker.showDatePickerDialog(requireContext(), _dateTextView));
     }


    @Override
    public void onNoteUpdate(@NonNull ViewGroup container, final int position, @NonNull NoteEntity note) {
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.firebase_update, container, false);

        // Initialize UI components
        EditText _titleEditText = dialogView.findViewById(R.id.firebase_Update_Title_Edit_Text);
        EditText _noteEditText = dialogView.findViewById(R.id.firebase_Update_Note_Edit_Text);
        TextView _dateTextView = dialogView.findViewById(R.id.firebase_Update_Date_Text_View);

        // Set current item values to the dialog
        _titleEditText.setText(note.getTitle());
        _noteEditText.setText(note.getNote());
        _dateTextView.setText(note.getDate());

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Edit Note")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String _key = noteList.get(position).getKey();
                    String _title = _titleEditText.getText().toString();
                    String _note = _noteEditText.getText().toString();
                    String _date = _dateTextView.getText().toString();

                    if (updateNoteChecker(_key, _title, _note, _date)) {
                        boolean isUpdated = updateNoteService(_key, _title, _note, _date);
                        if (isUpdated) {
                            onNoteRead();
                            Toast.makeText(requireContext(), "Note updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Failed to update note. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to update note: Invalid data", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

        _dateTextView.setOnClickListener(view -> DatePicker.showDatePickerDialog(requireContext(), _dateTextView));
    }

    @Override
    public void onNoteDelete(@NonNull NoteEntity note) {
        String key = note.getKey();
        String fileName = note.getFileName();

        if (deleteNoteChecker(key, fileName)) {
            boolean isDeleted = deleteNoteService(key, fileName);
            if (isDeleted) {
                onNoteRead();
                Toast.makeText(requireContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to delete note. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Failed to delete note: Invalid note ID", Toast.LENGTH_SHORT).show();
        }
    }
}