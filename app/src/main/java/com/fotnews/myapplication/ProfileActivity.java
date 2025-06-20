package com.fotnews.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameText, emailText;
    private ImageView profileImage;
    private Button editButton, deleteAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        usernameText = findViewById(R.id.username_text);
        emailText = findViewById(R.id.email_text);
        profileImage = findViewById(R.id.profile_image);
        editButton = findViewById(R.id.edit_button);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);


        profileImage.setImageResource(R.drawable.ic_profile_placeholder);


        editButton.setOnClickListener(view -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });


        deleteAccountButton.setOnClickListener(view -> showDeleteAccountDialog());


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

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to permanently delete your account?")
                .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();


        db.collection("users").document(uid).delete()
                .addOnSuccessListener(aVoid -> {

                    currentUser.delete()
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Auth deletion failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Firestore deletion failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void updateUI(String username, String email) {
        usernameText.setText((username != null && !username.isEmpty()) ? username : "No username");
        emailText.setText((email != null && !email.isEmpty()) ? email : "No email");
    }
}
