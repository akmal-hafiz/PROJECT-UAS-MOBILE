package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class activityexplore extends AppCompatActivity {

    // Variabel untuk pencarian dan suggestion produk
    EditText editTextSearch;
    LinearLayout suggestionContainer;
    List<Product> productList = new ArrayList<>(); // List data produk

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityexplore); // Menentukan layout XML yang digunakan

        // Inisialisasi BottomNavigationView dari XML
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.navexplore); // Menandai menu "Explore" sebagai aktif

        // Event ketika item navigasi bawah diklik
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navhome) {
                // Jika klik "Home", pindah ke activity Home
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Animasi transisi
                finish();
                return true;
            } else if (id == R.id.navexplore) {
                // Jika sudah di halaman Explore, tidak lakukan apa-apa
                return true;
            } else if (id == R.id.navbag) {
                // Jika klik "Bag", pindah ke activity Cart
                startActivity(new Intent(getApplicationContext(), cart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.navprofile) {
                // Jika klik "Profile", pindah ke activity Profile
                startActivity(new Intent(getApplicationContext(), profile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        // Inisialisasi gambar produk dari layout
        ImageView productImage2 = findViewById(R.id.productImage2);
        productImage2.setOnClickListener(view -> {
            // Saat gambar diklik, buka activity product1
            Intent intent = new Intent(activityexplore.this, product1.class);
            startActivity(intent);
        });

        // Menghubungkan kolom pencarian dan wadah saran dari XML
        editTextSearch = findViewById(R.id.editTextSearch);
        suggestionContainer = findViewById(R.id.searchSuggestions);

        // Menambahkan data produk manual (bisa nanti diambil dari Firebase juga)
        productList.add(new Product("Leather Jacket", "Rp.750 rb", R.drawable.leatherjacket));
        productList.add(new Product("Volunteer Jacket", "Rp.600 rb", R.drawable.volunteer));
        productList.add(new Product("Blackaid Jacket", "Rp.550 rb", R.drawable.blackaid));
        productList.add(new Product("KarmaClub Jacket", "Rp.700 rb", R.drawable.karmaclub));

        // Menambahkan event saat pengguna mengetik di kolom search
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Menampilkan hasil pencarian yang cocok
                showSuggestions(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    // Fungsi untuk menampilkan hasil suggestion berdasarkan input keyword
    private void showSuggestions(String keyword) {
        suggestionContainer.removeAllViews(); // Menghapus saran lama
        if (keyword.isEmpty()) {
            // Jika kosong, sembunyikan container
            suggestionContainer.setVisibility(View.GONE);
            return;
        }

        // Looping untuk mencari produk yang mengandung keyword
        for (Product p : productList) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                // Buat TextView baru untuk setiap saran yang cocok
                TextView suggestion = new TextView(this);
                suggestion.setText(p.getName()); // Tampilkan nama produk
                suggestion.setPadding(24, 16, 24, 16); // Atur padding
                suggestion.setTextSize(16); // Atur ukuran teks
                suggestion.setOnClickListener(v -> {
                    // Saat diklik, isi kolom pencarian dan sembunyikan hasil
                    editTextSearch.setText(p.getName());
                    suggestionContainer.setVisibility(View.GONE);
                    if (p.getName().equals("Volunteer Jacket")) {
                        Intent intent = new Intent(activityexplore.this, product1.class);
                        startActivity(intent);
                    }
                });
                suggestionContainer.addView(suggestion); // Tambahkan saran ke container
            }
        }

        // Tampilkan container jika ada hasil, sembunyikan jika tidak
        if (suggestionContainer.getChildCount() > 0) {
            suggestionContainer.setVisibility(View.VISIBLE);
        } else {
            suggestionContainer.setVisibility(View.GONE);
        }
    }
}
