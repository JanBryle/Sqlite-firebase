package com.example.firebase.note.sqlite.domain.repository;

/**
 * This interface defines the contract for checking the validity of note data
 * before performing Create, Read, Update, and Delete (CRUD) operations on notes
 * stored in a SQLite database.
 */
public interface NoteCheckerRepository {

    /**
     * Checks the validity of the provided data before adding a new note to the SQLite database.
     *
     * @param title The title of the note.
     * @param note  The content of the note.
     * @param date  The date of creation or last modification of the note.
     * @return True if the provided data is valid, false otherwise.
     */
    boolean addNoteChecker(final String title, final String note, final String date);

    /**
     * Checks the validity of the provided data before updating an existing note in the SQLite database.
     *
     * @param key   The unique identifier of the note to be updated.
     * @param title The updated title of the note.
     * @param note  The updated content of the note.
     * @param date  The updated date of creation or last modification of the note.
     * @return True if the provided data is valid, false otherwise.
     */
    boolean updateNoteChecker(final long key, final String title, final String note, final String date);

    /**
     * Checks the validity of the provided note ID before deleting a note from the SQLite database.
     *
     * @param id The unique identifier of the note to be deleted.
     * @return True if the provided note ID is valid, false otherwise.
     */
    boolean deleteNoteChecker(final long id);
}