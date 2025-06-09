package com.fotnews.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameText, emailText;
    private ImageView profileImage;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        usernameText = findViewById(R.id.username_text);
        emailText = findViewById(R.id.email_text);
        profileImage = findViewById(R.id.profile_image);
        editButton = findViewById(R.id.edit_button);

        // Set default profile image
        profileImage.setImageResource(R.drawable.ic_profile_placeholder);

        // Edit button click listener
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        // Load user info from Firestore
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference userRef = db.collection("users").document(uid);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");
                    updateUI(username, email);
                } else {
                    updateUI(null, null);
                }
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                updateUI(null, null);
            });
        }
    }

    private void updateUI(String username, String email) {
        if (username != null && !username.isEmpty()) {
            usernameText.setText(username);
        } else {
            usernameText.setText("No username");
        }

        if (email != null && !email.isEmpty()) {
            emailText.setText(email);
        } else {
            emailText.setText("No email");
        }
    }
}
