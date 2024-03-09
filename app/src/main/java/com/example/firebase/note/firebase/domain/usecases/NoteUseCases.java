package com.example.firebase.note.firebase.domain.usecases;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.firebase.note.firebase.data.data_sources.Database;
import com.example.firebase.note.firebase.domain.callback.NoteFetchCallback;
import com.example.firebase.note.firebase.domain.callback.NoteLoadedCallback;
import com.example.firebase.note.firebase.domain.entities.NoteEntity;
import com.example.firebase.note.firebase.domain.repository.NoteCheckerRepository;
import com.example.firebase.note.firebase.domain.repository.NoteServiceRepository;
import com.google.firebase.database.DatabaseError;

import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * The NoteUseCases class is responsible for managing the business logic related to CRUD (Create, Read, Update, Delete)
 * operations on notes stored in a Firebase Realtime Database. It provides an abstraction layer between the presentation
 * layer and the data access layer, ensuring that the data is properly validated and processed before performing any
 * database operations.
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
 *      on notes stored in the Firebase Realtime Database.
 *    - It provides methods to read all notes, add a new note, update an existing note, and delete a note based on
 *      its unique identifier.
 *
 * 3. Error Handling:
 *    - The class handles exceptions that may occur during database operations, such as Firebase Database errors or other
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
    private final static String TAG = NoteUseCases.class.getSimpleName();

    /**
     * Validates input data before adding a new note.
     *
     * @param title          The title of the note.
     * @param note           The content of the note.
     * @param date           The date of the note.
     * @param imageUri       The URI of the image associated with the note.
     * @param uniqueFileName The unique file name of the image.
     * @return True if the input data is valid, false otherwise.
     */
    @Override
    public boolean addNoteChecker(final String title, final String note, final String date, final Uri imageUri, final String uniqueFileName) {
        if (TextUtils.isEmpty(title)) {
            Log.e(TAG, "Title is empty");
            return false;
        }

        if (TextUtils.isEmpty(note)) {
            Log.e(TAG, "Note is empty");
            return false;
        }

        if (TextUtils.isEmpty(date)) {
            Log.e(TAG, "Date is empty");
            return false;
        }

        if (imageUri == null) {
            Log.e(TAG, "Image URI is null");
            return false;
        }

        if (TextUtils.isEmpty(uniqueFileName)) {
            Log.e(TAG, "Unique file name is empty");
            return false;
        }

        return true;
    }

    /**
     * Validates input data before updating a note.
     *
     * @param key    The key of the note to be updated.
     * @param title  The new title of the note.
     * @param note   The new content of the note.
     * @param date   The new date of the note.
     * @return True if the input data is valid, false otherwise.
     */
    @Override
    public boolean updateNoteChecker(final String key, final String title, final String note, final String date) {
        if (TextUtils.isEmpty(key)) {
            Log.e(TAG, "Key is empty");
            return false;
        }

        if (TextUtils.isEmpty(title)) {
            Log.e(TAG, "Title is empty");
            return false;
        }

        if (TextUtils.isEmpty(note)) {
            Log.e(TAG, "Note is empty");
            return false;
        }

        if (TextUtils.isEmpty(date)) {
            Log.e(TAG, "Date is empty");
            return false;
        }

        return true;
    }

    /**
     * Validates input data before deleting a note.
     *
     * @param key      The key of the note to be deleted.
     * @param fileName The file name of the associated image.
     * @return True if the input data is valid, false otherwise.
     */
    @Override
    public boolean deleteNoteChecker(final String key, final String fileName) {
        if (TextUtils.isEmpty(key)) {
            Log.e(TAG, "Key is empty");
            return false;
        }

        if (TextUtils.isEmpty(fileName)) {
            Log.e(TAG, "File name is empty");
            return false;
        }

        return true;
    }

    /**
     * Reads notes from the service.
     *
     * @param callback The callback to be invoked when notes are loaded or loading fails.
     */
    @Override
    public void readNoteService(final NoteLoadedCallback callback) {
        Database database = new Database();
        database.getAllNotesForUser(new NoteFetchCallback() {
            @Override
            public void onNotesLoaded(List<NoteEntity> notesFromDB) {
                Log.d(TAG, "Notes loaded: " + notesFromDB.size());
                callback.onNotesLoaded(notesFromDB);
            }

            @Override
            public void onDataLoadFailed(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load notes: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }

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
    @Override
    public boolean addNoteService(final String title, final String note, final String date, final Uri imageUri, final String uniqueFileName) {
        Database database = new Database();
        try {
            database.addNoteData(title, note, date, imageUri, uniqueFileName);
            Log.i(TAG, "Note added successfully.");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to add note: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates an existing note in the service.
     *
     * @param key    The key of the note to be updated.
     * @param title  The new title of the note.
     * @param note   The new content of the note.
     * @param date   The new date of the note.
     * @return True if the note is updated successfully, false otherwise.
     */
    @Override
    public boolean updateNoteService(final String key, final String title, final String note, final String date) {
        Database database = new Database();
        try {
            boolean success = database.updateNoteData(key, title, note, date);
            if (success) {
                Log.i(TAG, "Note updated successfully.");
                return true;
            } else {
                Log.e(TAG, "Failed to update note.");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to update note: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deletes a note from the service.
     *
     * @param key      The key of the note to be deleted.
     * @param fileName The file name of the associated image.
     * @return True if the note is deleted successfully, false otherwise.
     */
    @Override
    public boolean deleteNoteService(final String key, final String fileName) {
        Database database = new Database();
        try {
            boolean isDeleted = database.deleteNoteData(key, fileName);
            if (isDeleted) {
                Log.i(TAG, "Note deleted successfully.");
                return true;
            } else {
                Log.e(TAG, "Failed to delete note.");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to delete note: " + e.getMessage(), e);
            return false;
        }
    }
}