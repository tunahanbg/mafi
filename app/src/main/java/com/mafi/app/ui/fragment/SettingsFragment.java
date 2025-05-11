package com.mafi.app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.mafi.app.R;
import com.mafi.app.data.local.SharedPreferencesManager;

public class SettingsFragment extends Fragment {

    private SharedPreferencesManager preferencesManager;
    private RadioGroup radioGroupTheme;
    private SwitchMaterial switchNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // SharedPreferences yöneticisini al
        preferencesManager = SharedPreferencesManager.getInstance(requireContext());

        // View elemanlarını bağla
        radioGroupTheme = view.findViewById(R.id.radio_group_theme);
        switchNotification = view.findViewById(R.id.switch_notification);

        // Mevcut ayarları yükle
        loadCurrentSettings();

        // Tema değişikliği için listener
        radioGroupTheme.setOnCheckedChangeListener((group, checkedId) -> {
            String theme;
            if (checkedId == R.id.radio_button_light) {
                theme = "light";
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (checkedId == R.id.radio_button_dark) {
                theme = "dark";
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                theme = "system";
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }

            preferencesManager.setTheme(theme);
            Toast.makeText(getContext(), "Tema değiştirildi", Toast.LENGTH_SHORT).show();
        });

        // Bildirim ayarı için listener
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setNotificationEnabled(isChecked);
            Toast.makeText(getContext(),
                    isChecked ? "Bildirimler etkinleştirildi" : "Bildirimler devre dışı bırakıldı",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void loadCurrentSettings() {
        // Tema ayarını yükle
        String currentTheme = preferencesManager.getTheme();
        switch (currentTheme) {
            case "light":
                radioGroupTheme.check(R.id.radio_button_light);
                break;
            case "dark":
                radioGroupTheme.check(R.id.radio_button_dark);
                break;
            default:
                radioGroupTheme.check(R.id.radio_button_system);
                break;
        }

        // Bildirim ayarını yükle
        switchNotification.setChecked(preferencesManager.isNotificationEnabled());
    }
}