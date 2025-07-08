package com.example.myapplication3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class product1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ToggleButton toggleSizeM = findViewById(R.id.toggle_size_m);
        ToggleButton toggleSizeL = findViewById(R.id.toggle_size_l);
        ToggleButton toggleSizeXL = findViewById(R.id.toggle_size_xl);

        toggleSizeM.setOnClickListener(view -> {
            if (toggleSizeM.isChecked()) {
                toggleSizeL.setChecked(false);
                toggleSizeXL.setChecked(false);
            }
        });

        toggleSizeL.setOnClickListener(view -> {
            if (toggleSizeL.isChecked()) {
                toggleSizeM.setChecked(false);
                toggleSizeXL.setChecked(false);
            }
        });

        toggleSizeXL.setOnClickListener(view -> {
            if (toggleSizeXL.isChecked()) {
                toggleSizeM.setChecked(false);
                toggleSizeL.setChecked(false);
            }
        });

        Button addToBagButton = findViewById(R.id.addtobag);
        addToBagButton.setOnClickListener(view -> {
            Intent intent = new Intent(product1.this, cart.class);
            startActivity(intent);
        });

        Button buyNowButton = findViewById(R.id.buynow);
        buyNowButton.setOnClickListener(view -> {
            // Link Midtrans Payment Link kamu
            String snapUrl = "https://app.sandbox.midtrans.com/payment-links/1751949016615";

            // Ambil ukuran yang dipilih
            String size = "Belum dipilih";
            if (toggleSizeM.isChecked()) {
                size = "M";
            } else if (toggleSizeL.isChecked()) {
                size = "L";
            } else if (toggleSizeXL.isChecked()) {
                size = "XL";
            }

            // Tampilkan ukuran yang dipilih
            Toast.makeText(product1.this, "Ukuran yang kamu pilih: " + size, Toast.LENGTH_SHORT).show();

            // Buka halaman pembayaran Midtrans
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(snapUrl));
            startActivity(intent);
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.navhome);

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
                startActivity(new Intent(getApplicationContext(), profile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }
}
