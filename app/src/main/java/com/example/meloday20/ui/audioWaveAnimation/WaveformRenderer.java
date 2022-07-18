package com.example.meloday20.ui.audioWaveAnimation;

import android.graphics.Canvas;

public interface WaveformRenderer {
    void render(Canvas canvas, byte[] waveform);
}
