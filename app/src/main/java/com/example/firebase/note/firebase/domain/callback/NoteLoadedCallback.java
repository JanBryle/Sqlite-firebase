package com.example.firebase.note.firebase.domain.callback;

import com.example.firebase.note.firebase.domain.entities.NoteEntity;

import java.util.List;

/**
 * Callback interface for notifying when notes are loaded.
 */
public interface NoteLoadedCallback {

    /**
     * Called when notes are successfully loaded.
     *
     * @param notes The list of NoteEntity objects representing the loaded notes.
     */
    void onNotesLoaded(final List<NoteEntity> notes);
}