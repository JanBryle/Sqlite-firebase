package com.example.firebase.note.sqlite.presentation.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.R;
import com.example.firebase.note.sqlite.domain.entities.NoteEntity;
import com.example.firebase.note.sqlite.domain.usecases.NoteUseCases;
import com.example.firebase.note.sqlite.presentation.listener.NoteClickListener;
import com.example.firebase.note.sqlite.presentation.widget.DatePicker;
import com.example.firebase.note.sqlite.presentation.widget.NoteHolder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends NoteUseCases implements NoteClickListener {
    private final List<NoteEntity> noteList = new ArrayList<>();
    private NoteHolder noteAdapter;

    public NoteActivity(Context context) {
        super(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_sqlite, container, false);

        ExtendedFloatingActionButton fabAddNote = rootView.findViewById(R.id.sqlite_Note_Extended_Fab);
        fabAddNote.setOnClickListener(view -> onNoteAdd());

        RecyclerView noteRecyclerView = rootView.findViewById(R.id.sqlite_Note_Recycler_View);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        noteAdapter = new NoteHolder(noteList, this);
        noteRecyclerView.setAdapter(noteAdapter);

        onNoteRead();

        return rootView;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onNoteRead() {
        noteList.clear();
        noteList.addAll(readNoteService());
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteAdd() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.sqlite_upload, null);
        EditText _titleEditText = dialogView.findViewById(R.id.sqlite_upload_title_edit_text);
        EditText _noteEditText = dialogView.findViewById(R.id.sqlite_upload_note_edit_text);
        TextView _dateTextView = dialogView.findViewById(R.id.sqlite_upload_date_text_view);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Add New Note")
                .setView(dialogView)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String _title = _titleEditText.getText().toString();
                    String _note = _noteEditText.getText().toString();
                    String _date = _dateTextView.getText().toString();

                    if (addNoteChecker(_title, _note, _date)) {
                        boolean isAdded = addNoteService(_title, _note, _date);
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
                .show();

        _dateTextView.setOnClickListener(v -> DatePicker.showDatePickerDialog(requireContext(), _dateTextView));
    }

    @Override
    public void onNoteUpdate(@NonNull ViewGroup container, @NonNull NoteEntity note) {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.sqlite_update, container, false);

        EditText _titleEditText = dialogView.findViewById(R.id.sqlite_update_title_edit_text);
        EditText _noteEditText = dialogView.findViewById(R.id.sqlite_update_note_edit_text);
        TextView _dateTextView = dialogView.findViewById(R.id.sqlite_update_date_text_view);

        _titleEditText.setText(note.getTitle());
        _noteEditText.setText(note.getNote());
        _dateTextView.setText(note.getDate());

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Edit Note")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    try {
                        long _key = note.getId();
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
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Error updating note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

        _dateTextView.setOnClickListener(v -> DatePicker.showDatePickerDialog(requireContext(), _dateTextView));
    }

    @Override
    public void onNoteDelete(@NonNull NoteEntity note) {
        long _id = note.getId();
        if (deleteNoteChecker(_id)) {
            boolean isDeleted = deleteNoteService(_id);
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