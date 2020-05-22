package com.se68.rraptor.futurlarm.Templates;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.se68.rraptor.futurlarm.Class.FunctionSwitcher;
import com.se68.rraptor.futurlarm.R;

public class TemplateActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private final int SWIPE_THRESHOLD = 100;
    private final int VELOCITY_THRESHOLD = 100;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        boolean result = false;
        int current = FunctionSwitcher.getCurrent();
        float diffX = moveEvent.getX() - downEvent.getX();
        float diffY = moveEvent.getY() - downEvent.getY();

        if (Math.abs(diffX) < Math.abs(diffY)) return false;

        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > VELOCITY_THRESHOLD){
            result = true;
            if (diffX > 0 && current > 0) FunctionSwitcher.setCurrent(--current);
            else if (diffX < 0 && current < 3) FunctionSwitcher.setCurrent(++current);
            else return false;
        }

        FunctionSwitchFragment fragment = (FunctionSwitchFragment) getSupportFragmentManager().findFragmentById(R.id.FragmentTabSwitcher);
        fragment.switchTab(current);

        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
