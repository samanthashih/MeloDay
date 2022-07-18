package com.example.meloday20.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

public class PreviewPlayer implements MusicPlayer, MediaPlayer.OnCompletionListener {
    private static final String TAG = PreviewPlayer.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private String currentTrack;

    private class OnPreparedListener implements MediaPlayer.OnPreparedListener {

        private final String mUrl;

        public OnPreparedListener(String url) {
            mUrl = url;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            currentTrack = mUrl;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        release();
    }

    @Override
    public void play(String previewUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        try {
            createMediaPlayer(previewUrl);
            currentTrack = previewUrl;
        } catch (IOException e) {
            Log.e(TAG, "Error with playing preview", e);
        }
    }

//    @Override
//    public void stop() {
//        if (mediaPlayer != null) {
//            mediaPlayer.pause();
//        }
//    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        currentTrack = null;
    }

    @Override
    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    @Nullable
    public String getCurrentTrack() {
        return currentTrack;
    }

    public int getAudioSessionId() {
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getAudioSessionId();
    }


    private void createMediaPlayer(String url) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.setOnPreparedListener(new OnPreparedListener(url));
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.prepareAsync();
    }
}
