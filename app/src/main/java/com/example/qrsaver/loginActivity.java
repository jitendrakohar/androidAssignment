package com.example.qrsaver;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrsaver.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private ValidationUtils validationUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v->{
            validateGmailAndPassword();
        });
        binding.tvSignin.setOnClickListener(v->{
            startActivity(new Intent(loginActivity.this,signUpactivity.class));
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, proceed to the main screen or other actions
            // For example:
            Toast.makeText(this, "previous session is there so no need to login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void validateGmailAndPassword() {
        validationUtils = new ValidationUtils();
        if (validationUtils.isEmailValid(binding.etLoginGmail) && validationUtils.isPasswordValid(binding.etLoginPassword)) {
            signIn(binding.etLoginGmail.getText().toString(),binding.etLoginPassword.getText().toString());
        }

    }

    public void signIn(String email,String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign-in successful, proceed to the main screen or other actions
                        // For example:
                         startActivity(new Intent(loginActivity.this, MainActivity.class));
                         finish();
                    } else {
                        // Sign-in failed
                        // Handle the error
                        Exception exception = task.getException();
                        if (exception != null) {
                            // Handle the error
                        }
                    }
                });
    }
}