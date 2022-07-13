package com.example.meloday20;

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
