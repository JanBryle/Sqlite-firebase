package com.example.firebase.note.firebase.domain.entities;

/**
 * Represents a note entity in the Firebase database.
 */
public class NoteEntity {
    private String key;
    private String title;
    private String note;
    private String date;
    private String fileName;
    private String imageUrl;
    private String userId;

    /**
     * Default constructor.
     */
    public NoteEntity() {}

    /**
     * Constructor to initialize a NoteEntity object.
     *
     * @param key       The unique key of the note.
     * @param title     The title of the note.
     * @param note      The content of the note.
     * @param date      The date when the note was created or modified.
     * @param fileName  The file name of any associated image.
     * @param imageUrl  The URL of the image associated with the note.
     * @param userId    The ID of the user who created the note.
     */
    public NoteEntity(String key, String title, String note, String date, String fileName, String imageUrl, String userId) {
        this.key = key;
        this.title = title;
        this.note = note;
        this.date = date;
        this.fileName = fileName;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    /**
     * Get the key of the note.
     *
     * @return The key of the note.
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the title of the note.
     *
     * @return The title of the note.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the content of the note.
     *
     * @return The content of the note.
     */
    public String getNote() {
        return note;
    }

    /**
     * Get the date when the note was created or modified.
     *
     * @return The date of the note.
     */
    public String getDate() {
        return date;
    }

    /**
     * Get the file name of any associated image.
     *
     * @return The file name of the associated image.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the URL of the image associated with the note.
     *
     * @return The URL of the image.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Get the ID of the user who created the note.
     *
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }
}