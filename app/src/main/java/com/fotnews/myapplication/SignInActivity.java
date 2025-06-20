package com.fotnews.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText etEmailUsername, etPassword;
    private Button btnSignIn;
    private TextView tvForgotPassword, tvSignUp;
    private CheckBox cbRememberMe;
    private CardView btnGoogleSignIn, btnFacebookSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        mAuth = FirebaseAuth.getInstance();


        initViews();


        setClickListeners();
    }

    private void initViews() {
        etEmailUsername = findViewById(R.id.etEmailUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        cbRememberMe = findViewById(R.id.cbRememberMe);
    }

    private void setClickListeners() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignIn();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    private void handleSignIn() {
        String email = etEmailUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmailUsername.setError("Email is required");
            etEmailUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        // Show loading state
        btnSignIn.setEnabled(false);
        btnSignIn.setText("Signing In...");

        // Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    btnSignIn.setEnabled(true);
                    btnSignIn.setText("Sign In");

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (cbRememberMe.isChecked()) {
                            saveRememberMePreference(email);
                        }

                        Toast.makeText(SignInActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveRememberMePreference(String emailUsername) {
        getSharedPreferences("FOTNowPrefs", MODE_PRIVATE)
                .edit()
                .putString("remembered_user", emailUsername)
                .putBoolean("remember_me", true)
                .apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRememberMePreference();
    }

    private void checkRememberMePreference() {
        String rememberedUser = getSharedPreferences("FOTNowPrefs", MODE_PRIVATE)
                .getString("remembered_user", "");
        boolean rememberMe = getSharedPreferences("FOTNowPrefs", MODE_PRIVATE)
                .getBoolean("remember_me", false);

        if (rememberMe && !TextUtils.isEmpty(rememberedUser)) {
            etEmailUsername.setText(rememberedUser);
            cbRememberMe.setChecked(true);
        }
    }
}
