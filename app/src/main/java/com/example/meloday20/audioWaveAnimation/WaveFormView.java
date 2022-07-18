package com.example.meloday20.audioWaveAnimation;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class WaveFormView extends View {
    private static final int WAVE_MAX_POINTS = 54;
    private static final int WAVE_MIN_POINTS = 3;

    private byte[] waveform;
    private WaveformRenderer renderer;

    public WaveFormView(Context context) {
        super(context);
    }

    public WaveFormView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveFormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaveFormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setRenderer(WaveformRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (renderer != null) {
            renderer.render(canvas, waveform);
        }
    }

    public void setWaveform(byte[] waveform) {
        this.waveform = waveform;
        this.invalidate();
    }
}
