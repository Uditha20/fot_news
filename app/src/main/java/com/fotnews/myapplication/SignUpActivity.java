package com.fotnews.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private TextInputEditText etUserName, etPassword, etConfirmPassword, etEmail;
    private Button btnSignUp;
    private TextView tvSignIn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setClickListeners();
    }

    private void initViews() {
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etEmail = findViewById(R.id.etEmail);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);
    }

    private void setClickListeners() {
        btnSignUp.setOnClickListener(v -> handleSignUp());
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });
    }

    private void handleSignUp() {
        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (!validateInputs(userName, password, confirmPassword, email)) return;

        setLoadingState(true);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    setLoadingState(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName).build();
                            user.updateProfile(profileUpdates);

                            saveUserDetailsToFirestore(user.getUid(), userName, email);
                            sendEmailVerification(user);
                        }

                        Toast.makeText(this, "Account created successfully! Please sign in.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class).putExtra("email", email));
                        finish();
                    } else {
                        String errorMessage = getErrorMessage(task.getException());
                        Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    }
                });
    }

    private void saveUserDetailsToFirestore(String uid, String userName, String email) {
        User user = new User(userName, email);
        db.collection("users").document(uid).set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User saved to Firestore"))
                .addOnFailureListener(e -> Log.w(TAG, "Failed to save user to Firestore", e));
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "sendEmailVerification failed", task.getException());
            }
        });
    }

    private void setLoadingState(boolean isLoading) {
        btnSignUp.setEnabled(!isLoading);
        btnSignUp.setText(isLoading ? "Creating Account..." : "Sign Up");
        etUserName.setEnabled(!isLoading);
        etEmail.setEnabled(!isLoading);
        etPassword.setEnabled(!isLoading);
        etConfirmPassword.setEnabled(!isLoading);
    }

    private boolean validateInputs(String userName, String password, String confirmPassword, String email) {
        etUserName.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etConfirmPassword.setError(null);

        if (TextUtils.isEmpty(userName)) {
            etUserName.setError("Username is required");
            etUserName.requestFocus();
            return false;
        }
        if (userName.length() < 3 || userName.length() > 20) {
            etUserName.setError("Username must be 3–20 characters");
            etUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }
        if (password.length() < 6 || !isPasswordStrong(password)) {
            etPassword.setError("Password must contain uppercase, lowercase, and number");
            etPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordStrong(String password) {
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasLower = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        return hasUpper && hasLower && hasDigit;
    }

    private String getErrorMessage(Exception e) {
        if (e == null) return "Authentication failed";
        String msg = e.getMessage();
        if (msg == null) return "Authentication error";
        if (msg.contains("email address is already in use")) return "This email is already registered";
        if (msg.contains("weak password")) return "Password is too weak";
        if (msg.contains("malformed")) return "Enter a valid email address";
        return msg;
    }


    public static class User {
        public String username;
        public String email;

        public User() {
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }
}
