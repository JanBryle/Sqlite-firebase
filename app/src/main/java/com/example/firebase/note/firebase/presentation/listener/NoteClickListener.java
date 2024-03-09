package com.example.firebase.note.firebase.presentation.listener;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.example.firebase.note.firebase.domain.entities.NoteEntity;

/**
 * Interface defining a custom click listener to handle note-related actions.
 */
public interface NoteClickListener {

    /**
     * Handles the action of reading notes.
     */
    void onNoteRead();

    /**
     * Handles the action of adding a new note.
     *
     * @param inflater   The layout inflater to inflate the view.
     * @param container  The parent view group.
     */
    void onNoteAdd(@NonNull LayoutInflater inflater, @NonNull ViewGroup container);

    /**
     * Handles the action of updating a note.
     *
     * @param container  The parent view group.
     * @param position   The position of the note item in the list.
     * @param note       The note entity to be updated.
     */
    void onNoteUpdate(@NonNull ViewGroup container, final int position, @NonNull NoteEntity note);

    /**
     * Handles the action of deleting a note.
     *
     * @param note  The note entity to be deleted.
     */
    void onNoteDelete(@NonNull NoteEntity note);
}