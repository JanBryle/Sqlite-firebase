package com.example.firebase.note.sqlite.presentation.listener;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.example.firebase.note.sqlite.domain.entities.NoteEntity;

/**
 * Interface definition for a callback to be invoked when a note item is interacted with.
 */
public interface NoteClickListener {

    /**
     * Called when a note is requested to be read.
     */
    void onNoteRead();

    /**
     * Called when a new note is requested to be added.
     */
    void onNoteAdd();

    /**
     * Called when a note is requested to be updated.
     *
     * @param container The parent container ViewGroup where the note view resides.
     * @param note      The NoteEntity object representing the note to be updated.
     */
    void onNoteUpdate(@NonNull ViewGroup container, @NonNull NoteEntity note);

    /**
     * Called when a note is requested to be deleted.
     *
     * @param note The NoteEntity object representing the note to be deleted.
     */
    void onNoteDelete(@NonNull NoteEntity note);
}
