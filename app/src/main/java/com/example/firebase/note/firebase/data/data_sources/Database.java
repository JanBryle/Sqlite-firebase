package com.example.firebase.note.firebase.data.data_sources;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.firebase.note.firebase.domain.callback.NoteFetchCallback;
import com.example.firebase.note.firebase.domain.entities.NoteEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Database {
    public static final String TAG = Database.class.getSimpleName();
    private static final String ITEMS_REFERENCE = "notes";
    private static final String PHOTO_REFERENCE = "uploads";
    private static final String CURRENT_USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // Firebase User Information
    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private static final String currentUserId = currentUser.getUid();
    private static final String currentUserDisplayPhotoUrl = currentUser.getPhotoUrl().toString();

    // Firebase Database And Storage
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();

    /**
     * Get the ID of the current Firebase user.
     * @return The ID of the current Firebase user.
     */
    public static String getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Get the display photo URL of the current Firebase user.
     * @return The display photo URL of the current Firebase user.
     */
    public static String getCurrentUserDisplayPhotoUrl() { return currentUserDisplayPhotoUrl; }

    /**
     * Get the database reference for items in FirebaseDatabase.
     * @return The database reference for items.
     */
    public static DatabaseReference getItemsDatabaseReference() {
        return database.getReference(ITEMS_REFERENCE);
    }

    /**
     * Get the storage reference for photos in FirebaseStorage.
     * @return The storage reference for photos.
     */
    public static StorageReference getPhotoStorageReferences() {
        return storage.getReference().child(PHOTO_REFERENCE).child(currentUserId);
    }

    public void getAllNotesForUser(final NoteFetchCallback callback) {
        List<NoteEntity> noteEntity = new ArrayList<>();
        Query query = getItemsDatabaseReference().orderByChild("userId").equalTo(currentUserId);
        Log.d(TAG, "Querying for notes with userId: " + currentUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noteEntity.clear();
                Log.d(TAG, "Number of children: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NoteEntity note = snapshot.getValue(NoteEntity.class);
                    if (note != null) {
                        noteEntity.add(note);
                        Log.d(TAG, "Added note: " + note.toString());
                    } else {
                        Log.e(TAG, "Invalid note data encountered, skipping...");
                    }
                }
                Log.i(TAG, "Notes loaded successfully for user: " + currentUserId);
                callback.onNotesLoaded(noteEntity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error loading notes for user " + getCurrentUserId() + ": " + databaseError.getMessage());
                callback.onDataLoadFailed(databaseError);
            }
        });
    }
    public void addNoteData(final String title, final String note, final String date, final Uri imageUri, final String uniqueFileName) {

        StorageReference storageReference = getPhotoStorageReferences().child(uniqueFileName);

        UploadTask uploadTask = storageReference.putFile(imageUri);
        uploadTask.continueWithTask(task -> storageReference.getDownloadUrl())
                .addOnCompleteListener(task -> {
                    try {
                        // Generate a unique key for the new item
                        String _key = getItemsDatabaseReference().push().getKey();
                        String _imageUrl = task.getResult().toString();

                        // Create and store the upload data
                        NoteEntity upload = new NoteEntity(_key, title, note, date, uniqueFileName, _imageUrl, CURRENT_USER_ID);
                        getItemsDatabaseReference().child(_key).setValue(upload)
                                .addOnCompleteListener(databaseTask -> {
                                    if (databaseTask.isSuccessful()) {
                                        Log.i(TAG, "Note data upload successful");
                                    } else {
                                        Log.e(TAG, "Note data upload failed: " + databaseTask.getException().getMessage());
                                    }
                                });
                    } catch (Exception e) {
                        Log.e(TAG, "Upload failed: " + e.getMessage());
                    }
                }).addOnFailureListener(e -> Log.e(TAG, "Upload failed: " + e.getMessage()));
    }

    public boolean updateNoteData(final String key, final String title, final String note, final String date) {
        final boolean[] allTasksCompleted = {true}; // Assuming all tasks will be completed initially
        boolean a = false;
        getItemsDatabaseReference().child(key).child("title").setValue(title).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "Title update successful");
            } else {
                Log.e(TAG, "Title update failed: " + task.getException());
                allTasksCompleted[0] = false; // Set allTasksCompleted to false if any task fails
            }
        });

        getItemsDatabaseReference().child(key).child("note").setValue(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "Note update successful");
            } else {
                Log.e(TAG, "Note update failed: " + task.getException());
                allTasksCompleted[0] = false; // Set allTasksCompleted to false if any task fails
            }
        });

        getItemsDatabaseReference().child(key).child("date").setValue(date).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "Date update successful");
            } else {
                Log.e(TAG, "Date update failed: " + task.getException());
                allTasksCompleted[0] = false; // Set allTasksCompleted to false if any task fails
            }
        });

        return allTasksCompleted[0]; // Return the final value of allTasksCompleted
    }

    public boolean deleteNoteData(final String key, final String fileName) {
        final boolean[] taskCompleted = {true}; // Assuming tasks will be completed initially

        // Delete data from Firebase Realtime Database
        getItemsDatabaseReference().child(key).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "Item data deleted successfully from database");
            } else {
                Log.e(TAG, "Error deleting item data from database: " + task.getException().getMessage());
                taskCompleted[0] = false; // Set taskCompleted to false if the task fails
            }
        });

        // Delete file from Firebase Storage
       getPhotoStorageReferences()
                .child(fileName)
                .delete()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "File deleted successfully: " + fileName))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting file: " + fileName, e);
                    taskCompleted[0] = false; // Set taskCompleted to false if the task fails
                });

        return taskCompleted[0]; // Return the final value of taskCompleted
    }
}