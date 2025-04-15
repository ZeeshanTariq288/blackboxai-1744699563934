package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    public static final String CHANNEL_ID = "MusicPlayerChannel";
    private MediaPlayer mediaPlayer;
    private MediaSessionCompat mediaSession;

    private java.util.List<String> playlist = new java.util.ArrayList<>();
    private int currentIndex = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSessionCompat(this, "MusicPlayerService");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        String url = intent.getStringExtra("url");
        int index = intent.getIntExtra("index", -1);
        java.util.ArrayList<String> list = intent.getStringArrayListExtra("playlist");

        if (list != null) {
            playlist.clear();
            playlist.addAll(list);
        }

        if (index != -1) {
            currentIndex = index;
        }

        if (url != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (action != null) {
            switch (action) {
                case "PLAY":
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                    break;
                case "PAUSE":
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    break;
                case "STOP":
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    stopForeground(true);
                    stopSelf();
                    break;
                case "NEXT":
                    playNext();
                    break;
                case "PREVIOUS":
                    playPrevious();
                    break;
            }
        }

        startForeground(1, createNotification());
        return START_STICKY;
    }

    private void playNext() {
        if (playlist.isEmpty()) return;
        currentIndex = (currentIndex + 1) % playlist.size();
        playCurrent();
    }

    private void playPrevious() {
        if (playlist.isEmpty()) return;
        currentIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
        playCurrent();
    }

    private void playCurrent() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playlist.get(currentIndex));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Notification createNotification() {
        createNotificationChannel();

        Intent playIntent = new Intent(this, MusicPlayerService.class);
        playIntent.setAction("PLAY");
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, MusicPlayerService.class);
        pauseIntent.setAction("PAUSE");
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopIntent = new Intent(this, MusicPlayerService.class);
        stopIntent.setAction("STOP");
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 2, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, MusicPlayerService.class);
        nextIntent.setAction("NEXT");
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent previousIntent = new Intent(this, MusicPlayerService.class);
        previousIntent.setAction("PREVIOUS");
        PendingIntent previousPendingIntent = PendingIntent.getService(this, 4, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Player")
                .setContentText("Playing music")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .addAction(android.R.drawable.ic_media_previous, "Previous", previousPendingIntent)
                .addAction(android.R.drawable.ic_media_pause, "Pause", pausePendingIntent)
                .addAction(android.R.drawable.ic_media_stop, "Stop", stopPendingIntent)
                .addAction(android.R.drawable.ic_media_play, "Play", playPendingIntent)
                .addAction(android.R.drawable.ic_media_next, "Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(1, 3, 4))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOnlyAlertOnce(true)
                .setOngoing(true);

        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Music Player Channel", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        if (mediaSession != null) {
            mediaSession.release();
            mediaSession = null;
        }
    }
}
