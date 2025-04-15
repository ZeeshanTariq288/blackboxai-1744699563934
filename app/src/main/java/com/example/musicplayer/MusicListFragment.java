package com.example.musicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MusicListFragment extends Fragment implements MusicAdapter.OnItemClickListener {

    private RecyclerView rvMusicList;
    private MusicAdapter musicAdapter;
    private List<ItemsModel> items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        rvMusicList = view.findViewById(R.id.rvMusicList);
        rvMusicList.setLayoutManager(new LinearLayoutManager(getContext()));

        items = new ArrayList<>();
        items.add(new ItemsModel("Al-Fatihah (The Opening)", "1", "7", "Makkah", "http://www.bigrockmusic.com/mp3/track_07.mp3", "سُوْرَۃُ الفَاتِحَة"));
        items.add(new ItemsModel("Al-Baqarah (The Cow)", "2", "286", "Madinah", "http://www.bigrockmusic.com/mp3/track_08.mp3", "سُوْرَۃُ البَقَرَة"));

        musicAdapter = new MusicAdapter(items, this);
        rvMusicList.setAdapter(musicAdapter);

        return view;
    }

    @Override
    public void onPlayClick(int position) {
        // Open PlayerFragment with selected track
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openPlayerFragment(items, position);
        }
    }
}
