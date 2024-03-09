package com.example.firebase.note.firebase.domain.repository;

import android.net.Uri;
import com.example.firebase.note.firebase.domain.callback.NoteLoadedCallback;

/**
 * Repository interface for performing note-related service operations.
 */
public interface NoteServiceRepository {

    /**
     * Reads notes from the service.
     *
     * @param callback The callback to be invoked when notes are loaded or loading fails.
     */
    void readNoteService(final NoteLoadedCallback callback);

    /**
     * Adds a new note to the service.
     *
     * @param title          The title of the note.
     * @param note           The content of the note.
     * @param date           The date of the note.
     * @param imageUri       The URI of the image associated with the note.
     * @param uniqueFileName The unique file name of the image.
     * @return True if the note is added successfully, false otherwise.
     */
    boolean addNoteService(final String title, final String note, final String date, final Uri imageUri, final String uniqueFileName);

    /**
     * Updates an existing note in the service.
     *
     * @param key    The key of the note to be updated.
     * @param title  The new title of the note.
     * @param note   The new content of the note.
     * @param date   The new date of the note.
     * @return True if the note is updated successfully, false otherwise.
     */
    boolean updateNoteService(final String key, final String title, final String note, final String date);

    /**
     * Deletes a note from the service.
     *
     * @param key      The key of the note to be deleted.
     * @param fileName The file name of the associated image to be deleted.
     * @return True if the note is deleted successfully, false otherwise.
     */
    boolean deleteNoteService(final String key, final String fileName);
}