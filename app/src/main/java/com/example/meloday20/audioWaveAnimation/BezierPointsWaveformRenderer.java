package com.example.meloday20.audioWaveAnimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;

import androidx.annotation.ColorInt;

import java.util.Random;

public class BezierPointsWaveformRenderer implements WaveformRenderer {
    private static final String TAG = BezierPointsWaveformRenderer.class.getSimpleName();

    @ColorInt
    private final int backgroundColour;
    private final Paint foregroundPaint;
    private final Path waveformPath;

    static BezierPointsWaveformRenderer newInstance(@ColorInt int backgroundColour, @ColorInt int foregroundColour) {
        Paint paint = new Paint();
        paint.setColor(foregroundColour);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        Path waveformPath = new Path();
        return new BezierPointsWaveformRenderer(backgroundColour, paint, waveformPath);
    }

    BezierPointsWaveformRenderer(@ColorInt int backgroundColour, Paint foregroundPaint, Path waveformPath) {
        this.backgroundColour = backgroundColour;
        this.foregroundPaint = foregroundPaint;
        this.waveformPath = waveformPath;
    }

    private void renderWaveform(byte[] waveform, Canvas canvas) {
        calcBezierPoints(canvas, waveform);
    }

    private void calcBezierPoints(Canvas canvas, byte[] waveform) {
        final float DENSITY = 0.18f;
        final int WAVE_MAX_POINTS = 60;
        final int MAX_ANIM_BATCH_COUNT = 4;

        int numPoints = (int) (WAVE_MAX_POINTS * DENSITY);
        int numBatchCount = 0;
        int maxBatchCount = MAX_ANIM_BATCH_COUNT - 2;
        int widthOffset = canvas.getWidth() / numPoints;

        Random random = new Random();
        Rect canvasClipBounds = new Rect();
        canvas.getClipBounds(canvasClipBounds);

        float[] yStart = new float[numPoints + 1];
        float[] yEnd = new float[numPoints + 1];
        PointF[] bezierPoints = new PointF[numPoints + 1];
        PointF[] bezierControlPoints1 = new PointF[numPoints + 1];
        PointF[] bezierControlPoints2 = new PointF[numPoints + 1];

        // Initialize bezier points with PointF
        for (int i = 0; i < bezierPoints.length; i++) {
            bezierPoints[i] = new PointF();
            bezierControlPoints1[i] = new PointF();
            bezierControlPoints2[i] = new PointF();
        }

        // Initialize bezier points with canvas bounds
        for (int i = 0; i < bezierPoints.length; i++) {
            float x = canvasClipBounds.left + (i * widthOffset);
            float y = canvasClipBounds.bottom;
            yStart[i] = y;
            yEnd[i] = y;
            bezierPoints[i].set(x, y);
        }

        // Calculate bezier points and control points
        if ( waveform != null) {
            if (waveform.length == 0) {
                return;
            }
            waveformPath.rewind();
            float yRandom = yEnd[random.nextInt(numPoints)];

            // Calculate the start and end y-coordinates between each point
            for (int i = 0; i < bezierPoints.length-1; i++) {
                int x = (int) Math.ceil((i + 1) * (waveform.length / numPoints));
                int t = 0;

                // Calculate y-coordinate of next point
                if (x < 1024)
                    t = canvas.getHeight() + ((byte) (Math.abs(waveform[x]) + 128)) * canvas.getHeight() / 128;
                float y = canvasClipBounds.top + t;

                // Change the start and end y-coordinate of wave to new points
                yStart[i] = yEnd[i];
                yEnd[i] = y;
            }
            yEnd[bezierPoints.length - 1] = yRandom;
            numBatchCount++;

            // Calculate and set y coordinate of bezier points - determines the height of wavw
            for (int i = 0; i < bezierPoints.length; i++) {
                bezierPoints[i].y = yStart[i] + (((float) (numBatchCount) / maxBatchCount) * (yEnd[i] - yStart[i]));
            }

            // Calculate and set bezier control points - control points determine the shape of the curve
            // Control points are calculated by finding the midpoint between the start and end points,
            for (int i = 1; i < bezierPoints.length; i++) {
                bezierControlPoints1[i].set((bezierPoints[i].x + bezierPoints[i - 1].x) / 2, bezierPoints[i - 1].y);
                bezierControlPoints2[i].set((bezierPoints[i].x + bezierPoints[i - 1].x) / 2, bezierPoints[i].y);
            }

            // Create path from all the points within the canvas bounds
            createCurvePath(canvasClipBounds, bezierPoints, bezierControlPoints1, bezierControlPoints2);
        }
    }

    private void createCurvePath(Rect canvasClipBounds, PointF[] bezierPoints, PointF[] bezierControlPoints1, PointF[] bezierControlPoints2) {
        // Start point
        waveformPath.moveTo(bezierPoints[0].x, bezierPoints[0].y);
        // We are creating a cubic Bezier curve because we have 4 points (start, control point 1, control point 2, end)
        for (int i = 1; i < bezierPoints.length; i++) {
            // Creates path from last point of waveformPath (start) to control point 1, control point 2, end
            waveformPath.cubicTo(bezierControlPoints1[i].x, bezierControlPoints1[i].y,
                    bezierControlPoints2[i].x, bezierControlPoints2[i].y,
                    bezierPoints[i].x, bezierPoints[i].y);
        }

        waveformPath.lineTo(canvasClipBounds.right, canvasClipBounds.bottom);
        waveformPath.lineTo(canvasClipBounds.left, canvasClipBounds.bottom);
        waveformPath.close();
    }

    private void renderBlank(Canvas canvas) {
        //renders a flat line at bottom of canvas
        float width = canvas.getWidth();
        waveformPath.moveTo(0, 0);
        waveformPath.lineTo(width, 0);
    }

    @Override
    public void render(Canvas canvas, byte[] waveform) {
        canvas.drawColor(backgroundColour);
        waveformPath.reset();
        if (waveform != null) {
            renderWaveform(waveform, canvas);
        } else {
            renderBlank(canvas);
        }
        canvas.drawPath(waveformPath, foregroundPaint);
    }
}
