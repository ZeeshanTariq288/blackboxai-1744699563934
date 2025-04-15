package com.example.musicplayer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends Fragment {

    private Switch themeSwitch;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        themeSwitch = view.findViewById(R.id.switchTheme);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean isDarkTheme = sharedPreferences.getBoolean("dark_theme", false);
        themeSwitch.setChecked(isDarkTheme);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("dark_theme", isChecked).apply();
            if (getActivity() != null) {
                getActivity().recreate();
            }
        });

        return view;
    }
}
