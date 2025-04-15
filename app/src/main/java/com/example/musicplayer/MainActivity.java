package com.example.musicplayer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private List<ItemsModel> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load theme from preferences
        boolean isDarkTheme = getSharedPreferences("settings", MODE_PRIVATE).getBoolean("dark_theme", false);
        if (isDarkTheme) {
            setTheme(android.R.style.ThemeOverlay_Material_Dark);
        } else {
            setTheme(android.R.style.ThemeOverlay_Material_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        items = new ArrayList<>();
        items.add(new ItemsModel("Al-Fatihah (The Opening)", "1", "7", "Makkah", "http://www.bigrockmusic.com/mp3/track_07.mp3", "سُوْرَۃُ الفَاتِحَة"));
        items.add(new ItemsModel("Al-Baqarah (The Cow)", "2", "286", "Madinah", "http://www.bigrockmusic.com/mp3/track_08.mp3", "سُوْرَۃُ البَقَرَة"));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_music_list:
                    openMusicListFragment();
                    return true;
                case R.id.menu_player:
                    openPlayerFragment(items, 0);
                    return true;
                case R.id.menu_settings:
                    openSettingsFragment();
                    return true;
            }
            return false;
        });

        // Default fragment
        openMusicListFragment();
    }

    private void openMusicListFragment() {
        MusicListFragment fragment = new MusicListFragment();
        replaceFragment(fragment);
    }

    public void openPlayerFragment(List<ItemsModel> items, int position) {
        PlayerFragment fragment = PlayerFragment.newInstance(items.get(position));
        replaceFragment(fragment);
    }

    private void openSettingsFragment() {
        SettingsFragment fragment = new SettingsFragment();
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
