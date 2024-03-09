package com.example.firebase.note.sqlite.domain.repository;

import com.example.firebase.note.sqlite.domain.entities.NoteEntity;
import java.util.List;

/**
 * This interface defines the contract for performing Create, Read, Update, and Delete (CRUD)
 * operations on notes stored in a SQLite database.
 */
public interface NoteServiceRepository {

    /**
     * Retrieves a list of all notes from the SQLite database.
     *
     * @return A list of NoteEntity objects representing the notes stored in the database.
     */
    List<NoteEntity> readNoteService();

    /**
     * Adds a new note to the SQLite database.
     *
     * @param title The title of the note.
     * @param note  The content of the note.
     * @param date  The date of creation or last modification of the note.
     * @return True if the note was successfully added, false otherwise.
     */
    boolean addNoteService(final String title, final String note, final String date);

    /**
     * Updates an existing note in the SQLite database.
     *
     * @param key   The unique identifier of the note to be updated.
     * @param title The updated title of the note.
     * @param note  The updated content of the note.
     * @param date  The updated date of creation or last modification of the note.
     * @return True if the note was successfully updated, false otherwise.
     */
    boolean updateNoteService(final long key, final String title, final String note, final String date);

    /**
     * Deletes a note from the SQLite database.
     *
     * @param id The unique identifier of the note to be deleted.
     * @return True if the note was successfully deleted, false otherwise.
     */
    boolean deleteNoteService(final long id);
}