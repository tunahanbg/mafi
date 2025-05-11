package com.mafi.app.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mafi.app.R;
import com.mafi.app.data.local.SharedPreferencesManager;
import com.mafi.app.ui.fragment.HomeFragment;
import com.mafi.app.ui.fragment.LoginFragment;
import com.mafi.app.ui.fragment.ProfileFragment;
import com.mafi.app.ui.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigation;
    private SharedPreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        preferencesManager = SharedPreferencesManager.getInstance(this);

        // Kullanıcının giriş durumunu kontrol et
        if (preferencesManager.isLoggedIn()) {
            // Kullanıcı giriş yapmışsa ana ekranı göster
            loadFragment(new HomeFragment());
            // Varsayılan olarak ana ekran seçili olsun
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        } else {
            // Kullanıcı giriş yapmamışsa giriş ekranını göster
            loadFragment(new LoginFragment());
            bottomNavigation.setVisibility(View.GONE);
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_content) {
            fragment = new HomeFragment(); // İçerik sayfası yerine şimdilik HomeFragment kullanıyoruz
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        }

        return loadFragment(fragment);
    }
}