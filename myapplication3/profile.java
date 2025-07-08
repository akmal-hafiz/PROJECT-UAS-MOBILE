package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // Inisialisasi ImageView dan set OnClickListener
        TextView chatbot = findViewById(R.id.chatbot);

        // Menambahkan onClickListener pada ImageView
        chatbot.setOnClickListener(view -> {
            // Beralih ke Activity product1 saat gambar diklik
            Intent intent = new Intent(profile.this, ChatbotActivity .class);
            startActivity(intent);
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.navprofile); // Set active item

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navhome) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.navexplore) {
                startActivity(new Intent(getApplicationContext(), activityexplore.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.navbag) {
                startActivity(new Intent(getApplicationContext(), cart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.navprofile) {
                return true; // Already on this activity
            }
            return false;
        });
    }
}
