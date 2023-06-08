package com.example.qrsaver;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrsaver.databinding.ActivitySignInactivityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signUpactivity extends AppCompatActivity {
    private ActivitySignInactivityBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        binding.btnSignIn.setOnClickListener(v -> {
            validateForm();
        });
        binding.btnMoveToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(signUpactivity.this, loginActivity.class));
        });
    }

    public void validateForm() {
        ValidationUtils validationUtils = new ValidationUtils();
        if (validationUtils.validateInput(binding.etSigninUsername, binding.etSigninGmail, binding.etPassword, binding.etConfirmPassword)) {
            if (binding.etConfirmPassword.getText().toString().equals(binding.etPassword.getText().toString())) {
                signUp(binding.etSigninGmail.getText().toString(), binding.etPassword.getText().toString());
            }
        } else {
            Toast.makeText(this, "Please correct the form errors", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign-up successful, do further operations if needed
                        // For example, you can automatically sign in the user after successful sign-up:
                        // signIn(email, password);
                        Toast.makeText(this, "sign up successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(signUpactivity.this, loginActivity.class));
                        finish();
                    } else {
                        // Sign-up failed
                        Exception exception = task.getException();
                        if (exception != null) {
                            // Handle the error
                            Toast.makeText(this, "Your account have been successfully added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, proceed to the main screen or other actions
            // For example:
            Toast.makeText(this, "previous session is there so no need to login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(signUpactivity.this, MainActivity.class));
            finish();
        }
    }
}