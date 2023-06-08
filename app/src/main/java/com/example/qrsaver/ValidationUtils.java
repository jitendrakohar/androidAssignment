package com.example.qrsaver;


import android.util.Patterns;
import android.widget.EditText;

public class ValidationUtils {

    public  boolean isNameValid(EditText nameEditText) {
        String name = nameEditText.getText().toString().trim();
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }
        return true;
    }

    public  boolean isEmailValid(EditText emailEditText) {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return false;
        }
        return true;
    }

    public  boolean isPasswordValid(EditText passwordEditText) {
        String password = passwordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        } else if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    public  boolean isConfirmPasswordValid(EditText passwordEditText, EditText confirmPasswordEditText) {
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    public  boolean validateInput(EditText nameEditText, EditText emailEditText, EditText passwordEditText, EditText confirmPasswordEditText) {
        boolean isNameValid = isNameValid(nameEditText);
        boolean isEmailValid = isEmailValid(emailEditText);
        boolean isPasswordValid = isPasswordValid(passwordEditText);
        boolean isConfirmPasswordValid = isConfirmPasswordValid(passwordEditText, confirmPasswordEditText);

        return isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid;
    }


}