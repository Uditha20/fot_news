package com.fotnews.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView backButton, profileImage, changeImageButton;
    private EditText usernameEditText, emailEditText;
    private Button saveButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeViews();
        initializeFirebase();
        loadUserData();
        setupClickListeners();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        profileImage = findViewById(R.id.profile_image);
        changeImageButton = findViewById(R.id.change_image_button);
        usernameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        saveButton = findViewById(R.id.save_button);
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }

    private void loadUserData() {
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        DocumentReference userRef = firestore.collection("users").document(currentUser.getUid());
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {

                    if (document.contains("username")) {
                        usernameEditText.setText(document.getString("username"));
                    }


                    emailEditText.setText(currentUser.getEmail());

                }
            } else {
                Toast.makeText(EditProfileActivity.this,
                        "Failed to load profile data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        changeImageButton.setOnClickListener(v -> {

            Toast.makeText(this, "Change profile image clicked", Toast.LENGTH_SHORT).show();
        });

        saveButton.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();


        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            return;
        }

        saveButton.setEnabled(false);
        saveButton.setText("Saving...");


        Map<String, Object> updates = new HashMap<>();
        updates.put("username", username);


        firestore.collection("users")
                .document(currentUser.getUid())
                .update(updates)
                .addOnCompleteListener(firestoreTask -> {
                    if (firestoreTask.isSuccessful()) {
                        // Check if email needs to be updated
                        if (!email.equals(currentUser.getEmail())) {
                            updateEmail(email);
                        } else {
                            onUpdateSuccess();
                        }
                    } else {
                        onUpdateFailure(Objects.requireNonNull(firestoreTask.getException()).getMessage());
                    }
                });
    }

    private void updateEmail(String newEmail) {
        currentUser.updateEmail(newEmail)
                .addOnCompleteListener(emailTask -> {
                    if (emailTask.isSuccessful()) {

                        firestore.collection("users")
                                .document(currentUser.getUid())
                                .update("email", newEmail)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        onUpdateSuccess();
                                    } else {
                                        onUpdateFailure("Profile updated but email sync failed");
                                    }
                                });
                    } else {
                        onUpdateFailure("Failed to update email: " +
                                Objects.requireNonNull(emailTask.getException()).getMessage());
                    }
                });
    }

    private void onUpdateSuccess() {
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void onUpdateFailure(String error) {
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
        saveButton.setEnabled(true);
        saveButton.setText("Save Changes");
    }
}