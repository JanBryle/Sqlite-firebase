package com.example.firebase.core.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


// Android core components
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

// AndroidX components
import androidx.annotation.NonNull;

// Firebase components for authentication
import com.example.firebase.core.route.NavigationActivity;
import com.example.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

// Google Sign-In components
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

// Google API client and connection-related components
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class AuthenticationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {


    // Constants
    private static final String TAG = AuthenticationActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    // Authentication
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Check if the user is already authenticated
        if (currentUser != null) {
            startSwipeActivity(); // User is authenticated, move to SwipeActivity
        } else {
            initializeGoogleSignIn(); // User not authenticated, set up Google Sign-In

            // Set click listener for Google Sign-In button
            Button signInButton = findViewById(R.id.google_login_button);
            signInButton.setOnClickListener(v -> signIn());
        }
    }



    // Navigate to SwipeActivity
    private void startSwipeActivity() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    // Initialize Google Sign-In options
    private void initializeGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Request ID token for authentication
                .requestEmail() // Request user's email
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso); // Initialize Google Sign-In client
    }

    // Initiate the Google Sign-In process
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // Start the Google Sign-In activity
    }

    // Handle the result of Google Sign-In activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        } else {
            Log.w(TAG, "Unknown Request Code: " + requestCode);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Log.e(TAG, "Google Sign-In Failed: " + e.getStatusCode(), e);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        handleAuthenticationSuccess();
                    } else {
                        handleAuthenticationFailure(task.getException());
                    }
                });
    }

    private void handleAuthenticationSuccess() {
        Log.d(TAG, "Authentication successful.");
        Toast.makeText(this, "Authentication Successful", Toast.LENGTH_SHORT).show();
        startSwipeActivity(); // Start SwipeActivity after successful authentication
    }

    private void handleAuthenticationFailure(Exception exception) {
        Log.e(TAG, "Authentication Failed: " + exception.getMessage(), exception);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Google Play Services Connection Failed: " + connectionResult.getErrorMessage());
    }
}