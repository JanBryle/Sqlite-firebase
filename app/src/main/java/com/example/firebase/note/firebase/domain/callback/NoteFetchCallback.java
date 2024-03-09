package com.example.firebase.note.firebase.domain.callback;

import com.example.firebase.note.firebase.domain.entities.NoteEntity;
import com.google.firebase.database.DatabaseError;

import java.util.List;

/**
 * Callback interface for fetching notes from the database.
 */
public interface NoteFetchCallback {

    /**
     * Called when notes are successfully loaded from the database.
     *
     * @param notes List of NoteEntity objects representing the loaded notes.
     */
    void onNotesLoaded(final List<NoteEntity> notes);

    /**
     * Called when an error occurs while loading notes from the database.
     *
     * @param databaseError The DatabaseError object containing information about the error.
     */
    void onDataLoadFailed(final DatabaseError databaseError);
}