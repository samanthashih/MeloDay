package com.example.meloday20;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;

import androidx.annotation.ColorInt;

import java.util.Random;

public class SimpleWaveformRenderer implements WaveformRenderer {
    private static final String TAG = SimpleWaveformRenderer.class.getSimpleName();
    private static final float HALF_FACTOR = 0.5f;


    @ColorInt
    private final int backgroundColour;
    private final Paint foregroundPaint;
    private final Path waveformPath;

    static SimpleWaveformRenderer newInstance(@ColorInt int backgroundColour, @ColorInt int foregroundColour) {
        Paint paint = new Paint();
        paint.setColor(foregroundColour);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        Path waveformPath = new Path();
        return new SimpleWaveformRenderer(backgroundColour, paint, waveformPath);
    }

    SimpleWaveformRenderer(@ColorInt int backgroundColour, Paint foregroundPaint, Path waveformPath) {
        this.backgroundColour = backgroundColour;
        this.foregroundPaint = foregroundPaint;
        this.waveformPath = waveformPath;
    }

    private void renderWaveform(byte[] waveform, float width, float height, Canvas canvas) {
        calcBezierPoints(canvas, waveform);
    }

    private void calcBezierPoints(Canvas canvas, byte[] waveform) {

        final int WAVE_MAX_POINTS = 54;
        final int WAVE_MIN_POINTS = 3;
        final float mDensity = 0.25f;
        final int MAX_ANIM_BATCH_COUNT = 4;
        int nPoints = (int) (WAVE_MAX_POINTS * mDensity);
        int mWidthOffset = -1;
        int nBatchCount;
        int  mMaxBatchCount;
        float[] mSrcY;
        float[] mDestY;

        if (nPoints < WAVE_MIN_POINTS)
            nPoints = WAVE_MIN_POINTS;
        nBatchCount = 0;
        mMaxBatchCount = MAX_ANIM_BATCH_COUNT - 1;

        Random mRandom = new Random();
        Rect mClipBounds = new Rect();
        mSrcY = new float[nPoints + 1];
        mDestY = new float[nPoints + 1];
        PointF[] mBezierPoints, mBezierControlPoints1, mBezierControlPoints2;

        //initialize mBezierPoints
        mBezierPoints = new PointF[nPoints + 1];
        mBezierControlPoints1 = new PointF[nPoints + 1];
        mBezierControlPoints2 = new PointF[nPoints + 1];
        for (int i = 0; i < mBezierPoints.length; i++) {
            mBezierPoints[i] = new PointF();
            mBezierControlPoints1[i] = new PointF();
            mBezierControlPoints2[i] = new PointF();
        }


        if (mWidthOffset == -1) {
            canvas.getClipBounds(mClipBounds);
            mWidthOffset = canvas.getWidth() / nPoints;

            //initialize bezier points
            for (int i = 0; i < mBezierPoints.length; i++) {
                float posX = mClipBounds.left + (i * mWidthOffset);

                float posY;
                posY = mClipBounds.bottom;
                mSrcY[i] = posY;
                mDestY[i] = posY;
                mBezierPoints[i].set(posX, posY);
            }
        }

        //create the path and draw
        if ( waveform != null) {
            if (waveform.length == 0) {
                return;
            }
            waveformPath.rewind();
            //find the destination bezier point for a batch
            if (nBatchCount == 0) {

                float randPosY = mDestY[mRandom.nextInt(nPoints)];
                for (int i = 0; i < mBezierPoints.length-1; i++) {

                    int x = (int) Math.ceil((i + 1) * (waveform.length / nPoints));

                    int t = 0;
                    if (x < 1024)
                        t = canvas.getHeight() +
                                ((byte) (Math.abs(waveform[x]) + 128)) * canvas.getHeight() / 128;

                    float y;
                    y = mClipBounds.top + t;

                    //change the source and destination y
                    mSrcY[i] = mDestY[i];
                    mDestY[i] = y;
                }

                mDestY[mBezierPoints.length - 1] = randPosY;
            }

            //increment batch count
            nBatchCount++;

            //for smoothing animation
            for (int i = 0; i < mBezierPoints.length; i++) {
                mBezierPoints[i].y = mSrcY[i] + (((float) (nBatchCount) / mMaxBatchCount) * (mDestY[i] - mSrcY[i]));
            }

            //calculate the bezier curve control points
            for (int i = 1; i < mBezierPoints.length; i++) {
                mBezierControlPoints1[i].set((mBezierPoints[i].x + mBezierPoints[i - 1].x) / 2, mBezierPoints[i - 1].y);
                mBezierControlPoints2[i].set((mBezierPoints[i].x + mBezierPoints[i - 1].x) / 2, mBezierPoints[i].y);
            }

            //create the path
            waveformPath.moveTo(mBezierPoints[0].x, mBezierPoints[0].y);
            for (int i = 1; i < mBezierPoints.length; i++) {
                waveformPath.cubicTo(mBezierControlPoints1[i].x, mBezierControlPoints1[i].y,
                        mBezierControlPoints2[i].x, mBezierControlPoints2[i].y,
                        mBezierPoints[i].x, mBezierPoints[i].y);
            }

            waveformPath.lineTo(mClipBounds.right, mClipBounds.bottom);
            waveformPath.lineTo(mClipBounds.left, mClipBounds.bottom);
            waveformPath.close();
        }
    }

    private void renderBlank(float width, float height) {
        int y = (int) (height * HALF_FACTOR);
        waveformPath.moveTo(0, y);
        waveformPath.lineTo(width, y);
    }

    @Override
    public void render(Canvas canvas, byte[] waveform) {
        canvas.drawColor(backgroundColour);
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        waveformPath.reset();
        if (waveform != null) {
            renderWaveform(waveform, width, height, canvas);
        } else {
            renderBlank(width, height);
        }
        canvas.drawPath(waveformPath, foregroundPaint);
    }
}
