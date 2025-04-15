package com.example.musicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MusicListFragment extends Fragment implements MusicAdapter.OnItemClickListener {

    private RecyclerView rvRecentlyPlayed;
    private MusicAdapter recentlyPlayedAdapter;
    private List<ItemsModel> recentlyPlayedItems;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        rvRecentlyPlayed = view.findViewById(R.id.rvRecentlyPlayed);
        rvRecentlyPlayed.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recentlyPlayedItems = new ArrayList<>();
        // Add sample recently played items
        recentlyPlayedItems.add(new ItemsModel("Al-Fatihah (The Opening)", "1", "7", "Makkah", "http://www.bigrockmusic.com/mp3/track_07.mp3", "سُوْرَۃُ الفَاتِحَة"));
        recentlyPlayedItems.add(new ItemsModel("Al-Baqarah (The Cow)", "2", "286", "Madinah", "http://www.bigrockmusic.com/mp3/track_08.mp3", "سُوْرَۃُ البَقَرَة"));
        recentlyPlayedAdapter = new MusicAdapter(recentlyPlayedItems, this);
        rvRecentlyPlayed.setAdapter(recentlyPlayedAdapter);

        return view;
    }

    @Override
    public void onPlayClick(int position) {
        // Handle play click, open player fragment or activity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openPlayerFragment(recentlyPlayedItems, position);
        }
    }
}
