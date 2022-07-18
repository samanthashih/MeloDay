package com.example.meloday20.utils;

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