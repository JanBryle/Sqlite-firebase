package com.example.firebase.note.sqlite.domain.entities;

/**
 * This class represents a note entity stored in a SQLite database.
 * It encapsulates the properties of a note, such as its unique identifier,
 * title, content, and the date of creation or last modification.
 */
public class NoteEntity {
    // Fields
    private long id;        // Unique identifier for the note in the SQLite database
    private String title;   // Title of the note
    private String note;    // Content of the note
    private String date;    // Date of creation or last modification of the note

    /**
     * Constructor for creating a new NoteEntity object.
     *
     * @param id    The unique identifier for the note in the SQLite database.
     * @param title The title of the note.
     * @param note  The content of the note.
     * @param date  The date of creation or last modification of the note.
     */
    public NoteEntity(long id, String title, String note, String date) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.date = date;
    }

    /**
     * Getter method for the unique identifier of the note.
     *
     * @return The unique identifier of the note in the SQLite database.
     */
    public long getId() {
        return id;
    }

    /**
     * Getter method for the title of the note.
     *
     * @return The title of the note.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter method for the content of the note.
     *
     * @return The content of the note.
     */
    public String getNote() {
        return note;
    }

    /**
     * Getter method for the date of creation or last modification of the note.
     *
     * @return The date of creation or last modification of the note.
     */
    public String getDate() {
        return date;
    }
}