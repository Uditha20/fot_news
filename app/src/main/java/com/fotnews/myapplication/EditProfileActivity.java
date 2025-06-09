// EditProfileActivity.java
package com.fotnews.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView backButton;
    private EditText usernameEditText, emailEditText;
    private Button saveButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeViews();
        initializeFirebase();
        loadCurrentData();
        setupClickListeners();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        usernameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        saveButton = findViewById(R.id.save_button);
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void loadCurrentData() {
        // Get data passed from ProfileActivity
        String currentUsername = getIntent().getStringExtra("username");
        String currentEmail = getIntent().getStringExtra("email");

        if (currentUsername != null && !currentUsername.equals("No username")) {
            usernameEditText.setText(currentUsername);
        }

        if (currentEmail != null && !currentEmail.equals("No email")) {
            emailEditText.setText(currentEmail);
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String newUsername = usernameEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();

        if (newUsername.isEmpty()) {
            usernameEditText.setError("Username cannot be empty");
            return;
        }

        if (newEmail.isEmpty()) {
            emailEditText.setError("Email cannot be empty");
            return;
        }

        // Show loading state
        saveButton.setEnabled(false);
        saveButton.setText("Saving...");

        // Save to Firestore
        String userId = firebaseAuth.getCurrentUser().getUid();
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("username", newUsername);
        userProfile.put("email", newEmail);

        firestore.collection("users")
                .document(userId)
                .set(userProfile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating profile: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    saveButton.setEnabled(true);
                    saveButton.setText("Save Changes");
                });
    }
}