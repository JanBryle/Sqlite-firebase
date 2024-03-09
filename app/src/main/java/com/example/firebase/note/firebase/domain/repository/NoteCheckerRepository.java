package com.example.firebase.note.firebase.domain.repository;

import android.net.Uri;

/**
 * Repository interface for checking note-related operations.
 */
public interface NoteCheckerRepository {
    /**
     * Checks if the data for adding a note is valid.
     *
     * @param title          The title of the note.
     * @param note           The content of the note.
     * @param date           The date of the note.
     * @param imageUri       The URI of the image associated with the note.
     * @param uniqueFileName The unique file name of the image.
     * @return True if the data is valid, false otherwise.
     */
    boolean addNoteChecker(final String title, final String note, final String date, final Uri imageUri, final String uniqueFileName);

    /**
     * Checks if the data for updating a note is valid.
     *
     * @param key    The key of the note to be updated.
     * @param title  The new title of the note.
     * @param note   The new content of the note.
     * @param date   The new date of the note.
     * @return True if the data is valid, false otherwise.
     */
    boolean updateNoteChecker(final String key, final String title, final String note, final String date);

    /**
     * Checks if the data for deleting a note is valid.
     *
     * @param key      The key of the note to be deleted.
     * @param fileName The file name of the associated image to be deleted.
     * @return True if the data is valid, false otherwise.
     */
    boolean deleteNoteChecker(final String key, final String fileName);
}