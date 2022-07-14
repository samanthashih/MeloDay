package com.example.meloday20.utils;

import android.media.audiofx.Visualizer;

import com.example.meloday20.PreviewPlayer;

public class VisualizerSingleton {
    private static String TAG = VisualizerSingleton.class.getSimpleName();
    private static int currAudioSession;
    private static Visualizer visualizer = null;

    private VisualizerSingleton(int audioSession) {
        visualizer = new Visualizer(audioSession);
        currAudioSession = audioSession;
    }

    public synchronized static Visualizer getInstance(int audioSession) {
        if (visualizer == null || currAudioSession != audioSession) {
            new VisualizerSingleton(audioSession);
        }
        return visualizer;
    }
}