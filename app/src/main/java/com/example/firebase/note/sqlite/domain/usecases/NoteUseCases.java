package com.example.firebase.note.sqlite.domain.usecases;

import android.content.Context;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.firebase.note.sqlite.domain.entities.NoteEntity;
import com.example.firebase.note.sqlite.data.data_sources.Database;
import com.example.firebase.note.sqlite.domain.repository.NoteCheckerRepository;
import com.example.firebase.note.sqlite.domain.repository.NoteServiceRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * The NoteUseCases class is responsible for managing the business logic related to CRUD (Create, Read, Update, Delete)
 * operations on notes stored in a SQLite database. It provides an abstraction layer between the presentation layer
 * and the data access layer, ensuring that the data is properly validated and processed before performing any database
 * operations.
 *
 * This class extends the Fragment class and implements the NoteCheckerRepository and NoteServiceRepository interfaces,
 * which define the contracts for checking the validity of note data and performing CRUD operations on notes,
 * respectively.
 *
 * Key Responsibilities:
 *
 * 1. Input Validation:
 *    - The class provides methods to validate the input data before performing any CRUD operation on notes. It checks
 *      for empty or invalid values, such as empty titles, note content, dates, or invalid note IDs.
 *
 * 2. CRUD Operations:
 *    - The class implements the methods defined in the NoteServiceRepository interface to perform CRUD operations
 *      on notes stored in the SQLite database.
 *    - It provides methods to read all notes, add a new note, update an existing note, and delete a note based on
 *      its unique identifier.
 *
 * 3. Error Handling:
 *    - The class handles exceptions that may occur during database operations, such as SQL exceptions or other
 *      runtime exceptions.
 *    - It logs appropriate error messages using the Android logging system (Log class) to aid in debugging and
 *      troubleshooting.
 *
 * Usage:
 *
 * The NoteUseCases class is designed to be used in the presentation layer of an Android application, typically in
 * activities or fragments that deal with note management. It abstracts away the complexities of database operations
 * and input validation, allowing the presentation layer to focus on user interaction and data display.
 *
 * Example usage:
 *
 * NoteUseCases noteUseCases = new NoteUseCases(context);
 *
 * // Read all notes
 * List<NoteEntity> notes = noteUseCases.readNoteService();
 *
 * // Add a new note
 * String title = "New Note";
 * String content = "This is a new note.";
 * String date = "2023-05-01";
 * if (noteUseCases.addNoteChecker(title, content, date)) {
 *     noteUseCases.addNoteService(title, content, date);
 * }
 *
 * // Update an existing note
 * long noteId = 1;
 * String updatedTitle = "Updated Note";
 * String updatedContent = "This note has been updated.";
 * String updatedDate = "2023-05-02";
 * if (noteUseCases.updateNoteChecker(noteId, updatedTitle, updatedContent, updatedDate)) {
 *     noteUseCases.updateNoteService(noteId, updatedTitle, updatedContent, updatedDate);
 * }
 *
 * // Delete a note
 * long noteIdToDelete = 2;
 * if (noteUseCases.deleteNoteChecker(noteIdToDelete)) {
 *     noteUseCases.deleteNoteService(noteIdToDelete);
 * }
 */
public abstract class NoteUseCases extends Fragment implements NoteCheckerRepository, NoteServiceRepository {

    private final Context context;
    private final static String TAG = NoteUseCases.class.getSimpleName();

    /**
     * Constructor for NoteServiceManager.
     *
     * @param context The context of the application.
     */
    public NoteUseCases(Context context) {
        this.context = context;
    }

    /**
     * Checks the input parameters before adding a new note.
     *
     * @param setTitle      The title of the new note.
     * @param setNote       The content of the new note.
     * @param selectedDate  The selected date for the new note.
     * @return A boolean indicating whether the parameters are valid.
     */
    // Method to check if the input parameters are valid before adding a new note.
    @Override
    public boolean addNoteChecker(final String setTitle, final String setNote, final String selectedDate) {
        if (TextUtils.isEmpty(setTitle)) {
            Log.e(TAG, "Failed to add note: Title is required.");
            return false;
        }

        if (TextUtils.isEmpty(setNote)) {
            Log.e(TAG, "Failed to add note: Note content is required.");
            return false;
        }

        if (TextUtils.isEmpty(selectedDate)) {
            Log.e(TAG, "Failed to add note: Date is not selected.");
            return false;
        }

        return true;
    }

    /**
     * Checks the input parameters before updating a note.
     *
     * @param noteKey       The unique identifier of the note to be updated.
     * @param updatedTitle  The updated title of the note.
     * @param updatedNote   The updated content of the note.
     * @param updatedDate   The updated date of the note.
     * @return A boolean indicating whether the parameters are valid.
     */
    // Method to check if the input parameters are valid before updating a note.
    @Override
    public boolean updateNoteChecker(final long noteKey, final String updatedTitle, final String updatedNote, final String updatedDate) {
        if (noteKey < 0) {
            Log.e(TAG, "Failed to update note: Invalid note key.");
            return false;
        }

        if (TextUtils.isEmpty(updatedTitle)) {
            Log.e(TAG, "Failed to update note: Title is required.");
            return false;
        }

        if (TextUtils.isEmpty(updatedNote)) {
            Log.e(TAG, "Failed to update note: Note content is required.");
            return false;
        }

        if (TextUtils.isEmpty(updatedDate)) {
            Log.e(TAG, "Failed to update note: Date is required.");
            return false;
        }

        return true;
    }

    /**
     * Checks the note ID before deleting a note.
     *
     * @param noteId The unique identifier of the note to be deleted.
     * @return A boolean indicating whether the note ID is valid.
     */
    // Method to check if the note ID is valid before deleting a note.
    @Override
    public boolean deleteNoteChecker(final long noteId) {
        if (noteId < 0) {
            Log.e(TAG, "Failed to delete note: Invalid note ID.");
            return false;
        }
        return true;
    }

    /**
     * Reads notes from the SQLite database.
     *
     * @return A list of notes.
     */
    // Method to read notes from the SQLite database.
    @Override
    public List<NoteEntity> readNoteService() {
        List<NoteEntity> notes = new ArrayList<>();
        try (Database database = new Database(context)) {
            notes.addAll(database.getAllNotesForUser());
        } catch (SQLException e) {
            Log.e(TAG, "SQL error retrieving notes: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving notes: " + e.getMessage(), e);
        }
        return notes;
    }

    /**
     * Adds a new note to the SQLite database.
     *
     * @param setTitle      The title of the new note.
     * @param setNote       The content of the new note.
     * @param selectedDate  The selected date for the new note.
     */
    // Method to add a new note to the SQLite database.
    @Override
    public boolean addNoteService(final String setTitle, final String setNote, final String selectedDate) {
        try (Database database = new Database(context)) {
            boolean isNoteInserted = database.insertNoteData(setTitle, setNote, selectedDate);
            if (isNoteInserted) {
                Log.i(TAG, "Note added successfully.");
                return true;
            } else {
                Log.e(TAG, "Failed to add the note. Please try again.");
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQL error adding note: " + e.getMessage(), e);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error adding note: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates an existing note in the SQLite database.
     *
     * @param key         The unique identifier of the note to be updated.
     * @param title       The updated title of the note.
     * @param note        The updated content of the note.
     * @param date        The updated date of the note.
     */
    // Method to update an existing note in the SQLite database.
    @Override
    public boolean updateNoteService(final long key, final String title, final String note, final String date) {
        try (Database database = new Database(context)){
            boolean success = database.updateNoteData(key, title, note, date);
            if (success) {
                Log.i(TAG, "Note updated successfully.");
                return true;
            } else {
                Log.e(TAG, "Failed to update note.");
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQL error updating note: " + e.getMessage(), e);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error updating note: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deletes a note from the SQLite database.
     *
     * @param id The unique identifier of the note to be deleted.
     */
    // Method to delete a note from the SQLite database.
    @Override
    public boolean deleteNoteService(final long id) {
        try (Database database = new Database(context)) {
            boolean isDeleted = database.deleteNoteById(id);
            if (isDeleted) {
                Log.i(TAG, "Note deleted successfully.");
                return true;
            } else {
                Log.e(TAG, "Failed to delete note.");
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQL error deleting note: " + e.getMessage(), e);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting note: " + e.getMessage(), e);
            return false;
        }
    }
}