package com.example.meloday20.models;

import androidx.annotation.Nullable;

public interface MusicPlayer {
    void play(String url);

    void pause();

    void resume();

    boolean isPlaying();

    @Nullable
    String getCurrentTrack();

    void release();
}
