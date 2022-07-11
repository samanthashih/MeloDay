package com.example.meloday20.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
// imported from https://gist.github.com/nesquena/b2f023bb04190b2653c7

/*
Usage:
  myView.setOnTouchListener(new OnDoubleTapListener(this) {
    @Override
    public void onDoubleTap(MotionEvent e) {
      Toast.makeText(MainActivity.this, "Double Tap", Toast.LENGTH_SHORT).show();
    }
  });
*/
public class OnDoubleTapListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;

    public OnDoubleTapListener(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            OnDoubleTapListener.this.onDoubleTap(e);
            return super.onDoubleTap(e);
        }
    }

    public void onDoubleTap(MotionEvent e) {
        // To be overridden when implementing listener
    }
}
