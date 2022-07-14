package com.example.meloday20;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;

public class TrackVisualizerActivity extends AppCompatActivity implements Visualizer.OnDataCaptureListener {
    private static final int REQUEST_CODE = 0;
    static final String[] PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};

    private static final int CAPTURE_SIZE = 256;
    private static final String TAG = TrackVisualizerActivity.class.getSimpleName();
    private int audioSession;


    private Visualizer visualizer;
    private WaveFormView waveformView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_visualizer);

        waveformView = findViewById(R.id.waveform_view);
        RendererFactory rendererFactory = new RendererFactory();
        waveformView.setRenderer(rendererFactory.createSimpleWaveformRenderer(Color.GREEN, Color.DKGRAY));
        audioSession = getIntent().getIntExtra("audioSession", 0);
    }

    private void startVisualiser() {
        Log.i(TAG, "Audio session: " + audioSession);
        visualizer = new Visualizer(audioSession);
        visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, false);
        visualizer.setCaptureSize(CAPTURE_SIZE);
        visualizer.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PermissionsChecker checker = new PermissionsChecker(this);

        if (checker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            startVisualiser();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        if (visualizer != null) {
            visualizer.setEnabled(false);
            visualizer.release();
            visualizer.setDataCaptureListener(null, 0, false, false);
        }
        super.onPause();
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        if (waveformView != null) {
            waveformView.setWaveform(waveform);
        }
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        // Fast Fournier Transform data - don't need for this
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}