package com.example.firebase.note.sqlite.data.data_sources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.firebase.note.sqlite.domain.entities.NoteEntity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for managing SQLite database operations.
 */
public class Database extends SQLiteOpenHelper {
    // Tag for logging purposes
    public static final String TAG = Database.class.getSimpleName();
    // User ID from Firebase Auth
    private static final String CURRENT_USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    // Database name and version
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    public static final String TABLE_NOTE_TABLE = "notetable";
    public static final String COLUMN_NOTE_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_USER_REFERENCE = "user_id";

    /**
     * Constructor for creating a new instance of the SQLite database helper.
     *
     * @param context The application context.
     */
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement for creating the note table
        String createNoteTableStatement = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTE_TABLE + " (" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_NOTE + " TEXT NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_USER_REFERENCE + " TEXT NOT NULL)";

        // Execute the SQL statement
        db.execSQL(createNoteTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called if the database version is changed.
        // Handle database schema upgrades here.
    }


    public List<NoteEntity> getAllNotesForUser() {
        List<NoteEntity> notesList = new ArrayList<>();
        String[] projection = {
                COLUMN_NOTE_ID,
                COLUMN_TITLE,
                COLUMN_NOTE,
                COLUMN_DATE
        };

        String selection = COLUMN_USER_REFERENCE + "=?";
        String[] selectionArgs = {CURRENT_USER_ID};

        SQLiteDatabase databaseRef = null;
        Cursor cursor = null;

        try {
            // Get a readable database reference
            databaseRef = this.getReadableDatabase();
            // Query the database for notes associated with the user
            cursor = databaseRef.query(TABLE_NOTE_TABLE, projection, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Extract note data from the cursor and add to the list
                int idIndex = cursor.getColumnIndexOrThrow(COLUMN_NOTE_ID);
                int titleIndex = cursor.getColumnIndexOrThrow(COLUMN_TITLE);
                int messageIndex = cursor.getColumnIndexOrThrow(COLUMN_NOTE);
                int dateIndex = cursor.getColumnIndexOrThrow(COLUMN_DATE);

                do {
                    long id = cursor.getLong(idIndex);
                    String title = cursor.getString(titleIndex);
                    String message = cursor.getString(messageIndex);
                    String date = cursor.getString(dateIndex);

                    NoteEntity note = new NoteEntity(id, title, message, date);
                    notesList.add(note);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            // Log any errors that occur during database operation
            Log.e(TAG, "Error retrieving notes for user: " + e.getMessage());
        } finally {
            // Close cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            if (databaseRef != null && databaseRef.isOpen()) {
                databaseRef.close();
            }
        }

        return notesList;
    }

    /**
     * Inserts a new note into the database.
     *
     * @param title   The title of the note.
     * @param note    The content of the note.
     * @param date    The date of the note.
     * @return True if the insertion was successful, false otherwise.
     */
    public boolean insertNoteData(String title, String note, String date) {
        SQLiteDatabase databaseRef = null;
        boolean isSuccess = false;

        try {
            // Get a writable database reference
            databaseRef = this.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_NOTE, note);
            values.put(COLUMN_DATE, date);
            values.put(COLUMN_USER_REFERENCE, CURRENT_USER_ID);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = databaseRef.insert(TABLE_NOTE_TABLE, null, values);

            // Check if insertion was successful
            isSuccess = newRowId != -1;
        } catch (SQLException error) {
            // Handle any database errors and log them
            Log.e(TAG, error.getMessage());
        } finally {
            // Close the database connection
            if (databaseRef != null && databaseRef.isOpen()) {
                databaseRef.close();
            }
        }

        return isSuccess;
    }

    /**
     * Updates an existing note in the database.
     *
     * @param noteId  The ID of the note to be updated.
     * @param title   The new title for the note.
     * @param note    The new content for the note.
     * @param date    The new date for the note.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateNoteData(long noteId, String title, String note, String date) {
        SQLiteDatabase databaseRef = null;
        boolean isSuccess = false;

        try {
            // Get a writable database reference
            databaseRef = this.getWritableDatabase();

            // Create a ContentValues object to hold the values to be updated
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_NOTE, note);
            values.put(COLUMN_DATE, date);

            // Define the WHERE clause
            String selection = COLUMN_NOTE_ID + "=?";
            String[] selectionArgs = {String.valueOf(noteId)};

            // Update the record
            int rowsAffected = databaseRef.update(TABLE_NOTE_TABLE, values, selection, selectionArgs);

            // Check if the update was successful
            isSuccess = rowsAffected > 0;
        } catch (SQLException e) {
            // Log any errors that occur during database operation
            Log.e(TAG, "Error updating note data: " + e.getMessage());
        } finally {
            // Close the database connection
            if (databaseRef != null && databaseRef.isOpen()) {
                databaseRef.close();
            }
        }

        return isSuccess;
    }

    /**
     * Deletes a note from the database by its ID.
     *
     * @param noteId  The ID of the note to be deleted.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteNoteById(long noteId) {
        SQLiteDatabase databaseRef = null;
        boolean isSuccess = false;

        try {
            // Get a writable database reference
            databaseRef = this.getWritableDatabase();

            // Define the WHERE clause
            String selection = COLUMN_NOTE_ID + "=?";
            String[] selectionArgs = {String.valueOf(noteId)};

            // Delete the record
            int rowsDeleted = databaseRef.delete(TABLE_NOTE_TABLE, selection, selectionArgs);

            // Check if the deletion was successful
            isSuccess = rowsDeleted > 0;
        } catch (SQLException e) {
            // Log any errors that occur during database operation
            Log.e(TAG, "Error deleting note by ID: " + e.getMessage());
        } finally {
            // Close the database connection
            if (databaseRef != null && databaseRef.isOpen()) {
                databaseRef.close();
            }
        }

        return isSuccess;
    }
}