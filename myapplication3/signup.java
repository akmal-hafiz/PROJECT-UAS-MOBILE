package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import com.google.firebase.FirebaseApp;

public class signup extends AppCompatActivity {

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://my-application1-f1f68-default-rtdb.firebaseio.com/")
                .child("users");

        findViewById(R.id.Signup).setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = ((EditText)findViewById(R.id.emailup)).getText().toString().trim();
        String username = ((EditText)findViewById(R.id.Usernameup)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.passwordup)).getText().toString().trim();


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ((EditText)findViewById(R.id.emailup)).setError("Email tidak valid");
            return;
        }

        if (username.isEmpty() || username.length() < 4) {
            ((EditText)findViewById(R.id.Usernameup)).setError("Username minimal 4 karakter");
            return;
        }

        if (password.length() < 6) {
            ((EditText)findViewById(R.id.passwordup)).setError("Password minimal 6 karakter");
            return;
        }


        database.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ((EditText)findViewById(R.id.Usernameup)).setError("Username sudah digunakan");
                } else {
                    saveUser(email, username, password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Error database: " + error.getMessage());
            }
        });
    }

    private void saveUser(String email, String username, String password) {
        User user = new User(email, password);

        database.child(username).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    showToast("Sign-up berhasil!");
                    startActivity(new Intent(this, MainActivity1.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Gagal registrasi: " + e.getMessage()));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private static class User {
        public String email;
        public String password;

        public User() {}

        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}