package com.example.meloday20.service;

import com.example.meloday20.models.PreviewPlayer;

public class PreviewPlayerSingleton {
    private static String TAG = PreviewPlayerSingleton.class.getSimpleName();
    private static PreviewPlayer previewPlayer = null;

    private PreviewPlayerSingleton() {
        previewPlayer = new PreviewPlayer();
    }

    public synchronized static PreviewPlayer getInstance() {
        if (previewPlayer == null) {
            new PreviewPlayerSingleton();
        }
        return previewPlayer;
    }
}