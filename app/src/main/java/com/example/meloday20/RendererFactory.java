package com.example.meloday20;

import androidx.annotation.ColorInt;

public class RendererFactory {
    public WaveformRenderer createSimpleWaveformRenderer(@ColorInt int foreground, @ColorInt int background) {
        return BezierPointsWaveformRenderer.newInstance(background, foreground);
    }
}
