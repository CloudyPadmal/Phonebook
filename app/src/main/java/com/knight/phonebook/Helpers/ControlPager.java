package com.knight.phonebook.Helpers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;


public class ControlPager extends ViewPager {

    private boolean pagingEnabled;
    private boolean nextPagingEnabled;
    private float initialXValue;
    private int lockPage;
    protected OnPageChangeListener listener;
    private ScrollerDuration mScroller = null;

    public ControlPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        pagingEnabled = true;
        nextPagingEnabled = true;
        lockPage = 0;
        initViewPagerScroller();
    }
    
    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
        this.listener = listener;
    }

    @Override
    public void setCurrentItem(int item) {

        boolean invokeMeLater = false;

        if (super.getCurrentItem() == 0 && item == 0)
            invokeMeLater = true;

        super.setCurrentItem(item);

        if (invokeMeLater && listener != null)
            listener.onPageSelected(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !checkPagingState(event) && super.onInterceptTouchEvent(event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !checkPagingState(event) && super.onTouchEvent(event);

    }

    private boolean checkPagingState(MotionEvent event) {
        if (!pagingEnabled) {
            return true;
        }

        if (!nextPagingEnabled) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                initialXValue = event.getX();
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (detectSwipeToRight(event)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setNextPagingEnabled(boolean nextPagingEnabled) {
        this.nextPagingEnabled = nextPagingEnabled;
        if (!nextPagingEnabled) {
            lockPage = getCurrentItem();
        }
    }

    private void initViewPagerScroller() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerDuration(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    public boolean isNextPagingEnabled() {
        return nextPagingEnabled;
    }

    public boolean isPagingEnabled() {
        return pagingEnabled;
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        this.pagingEnabled = pagingEnabled;
    }

    public int getLockPage() {
        return lockPage;
    }

    public void setLockPage(int lockPage) {
        this.lockPage = lockPage;
    }

    private boolean detectSwipeToRight(MotionEvent event) {
        final int SWIPE_THRESHOLD = 0;
        boolean result = false;

        try {
            float diffX = event.getX() - initialXValue;
            if (Math.abs(diffX) > SWIPE_THRESHOLD) {
                if (diffX < 0) {
                    result = true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public class ScrollerDuration extends Scroller {

        private double mScrollFactor = 6;

        public ScrollerDuration(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public void setScrollDurationFactor(double scrollFactor) {
            mScrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
        }
    }
}
