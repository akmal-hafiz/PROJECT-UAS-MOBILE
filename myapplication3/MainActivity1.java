package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.*;

public class MainActivity1 extends AppCompatActivity {

    Button pindahactivty;
    private EditText etUsername, etPassword; // Tambahkan untuk input login
    private DatabaseReference databaseRef;    // Tambahkan referensi Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


        etUsername = findViewById(R.id.usernamelog);
        etPassword = findViewById(R.id.password);


        databaseRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://my-application1-f1f68-default-rtdb.firebaseio.com/users");


        pindahactivty = findViewById(R.id.Enter);
        pindahactivty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Panggil fungsi login ketika tombol Enter diklik
                handleLogin();
            }
        });


        View signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keSignup = new Intent(MainActivity1.this, signup.class);
                startActivity(keSignup);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        if (username.isEmpty()) {
            etUsername.setError("Masukkan username");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Masukkan password");
            return;
        }


        databaseRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String dbPassword = snapshot.child("password").getValue(String.class);

                    if (password.equals(dbPassword)) {

                        Toast.makeText(MainActivity1.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                        Intent pindahkeHome = new Intent(MainActivity1.this, Home.class);
                        startActivity(pindahkeHome);
                    } else {
                        etPassword.setError("Password salah");
                    }
                } else {
                    etUsername.setError("Username tidak ditemukan");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity1.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}