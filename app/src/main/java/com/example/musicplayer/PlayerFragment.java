package com.example.musicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerFragment extends Fragment {

    private TextView tvTitle, tvArtist, tvProgress;
    private SeekBar seekBar;
    private Button btnPlayPause, btnStop, btnNext, btnPrevious;

    private MediaPlayer mediaPlayer;
    private ItemsModel currentItem;

    private Timer timer;

    public static PlayerFragment newInstance(ItemsModel item) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvArtist = view.findViewById(R.id.tvArtist);
        tvProgress = view.findViewById(R.id.tvProgress);
        seekBar = view.findViewById(R.id.seekBar);
        btnPlayPause = view.findViewById(R.id.btnPlayPause);
        btnStop = view.findViewById(R.id.btnStop);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);

        if (getArguments() != null) {
            currentItem = (ItemsModel) getArguments().getSerializable("item");
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(currentItem.getUrl());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(mp -> {
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            btnPlayPause.setText("Pause");
            startSeekBarUpdate();
        });

        tvTitle.setText(currentItem.getSurahNameEnglish());
        tvArtist.setText(currentItem.getPlaceOfRevelation());

        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setText("Play");
            } else {
                mediaPlayer.start();
                btnPlayPause.setText("Pause");
            }
        });

        btnStop.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                btnPlayPause.setText("Play");
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                updateProgressText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // no-op
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // no-op
            }
        });

        return view;
    }

    private void startSeekBarUpdate() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            int currentPosition = mediaPlayer.getCurrentPosition();
                            seekBar.setProgress(currentPosition);
                            updateProgressText(currentPosition);
                        });
                    }
                }
            }
        }, 0, 1000);
    }

    private void updateProgressText(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / 1000) / 60;
        tvProgress.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
