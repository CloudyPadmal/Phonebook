package com.knight.phonebook.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.knight.phonebook.Adapters.Adapter_SwipeMenu;
import com.knight.phonebook.Items.SliderMenu_Item;
import com.knight.phonebook.Items.Creator_Item;


public class View_ContactList extends ListView {

    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;
    private int mDirection = 1;
    private int MAX_Y = 5;
    private int MAX_X = 3;

    private float mDownX;
    private float mDownY;
    private int mTouchState;
    private int mTouchPosition;

    private View_SwipeMenuLayout mTouchView;
    private OnSwipeListener mOnSwipeListener;
    private Creator_Item mMenuCreator;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private OnMenuStateChangeListener mOnMenuStateChangeListener;
    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;

    public View_ContactList(Context context) {

        super(context);
        init();
    }

    public View_ContactList(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        init();
    }

    public View_ContactList(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    private void init() {

        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        mTouchState = TOUCH_STATE_NONE;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {

        super.setAdapter(new Adapter_SwipeMenu(getContext(), adapter) {

            @Override
            public void createMenu(SliderMenu_Item menu) {
                if (mMenuCreator != null) {
                    mMenuCreator.create(menu);
                }
            }

            @Override
            public void onItemClick(View_SwipeMenu view, SliderMenu_Item menu,
                                    int index) {
                boolean flag = false;
                if (mOnMenuItemClickListener != null) {
                    flag = mOnMenuItemClickListener.onMenuItemClick(
                            view.getPosition(), menu, index);
                }
                if (mTouchView != null && !flag) {
                    mTouchView.smoothCloseMenu();
                }
            }
        });
    }

    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:

                mDownX = ev.getX();
                mDownY = ev.getY();
                boolean handled = super.onInterceptTouchEvent(ev);
                mTouchState = TOUCH_STATE_NONE;
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                if (view instanceof View_SwipeMenuLayout) {

                    if (mTouchView != null && mTouchView.isOpen() && !inRangeOfView(mTouchView.getMenuView(), ev)) {
                        return true;
                    }
                    mTouchView = (View_SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);
                }

                if (mTouchView != null && mTouchView.isOpen() && view != mTouchView) {
                    handled = true;
                }

                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                return handled;

            case MotionEvent.ACTION_MOVE:

                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (Math.abs(dy) > MAX_Y || Math.abs(dx) > MAX_X) {

                    if (mTouchState == TOUCH_STATE_NONE) {
                        if (Math.abs(dy) > MAX_Y) {
                            mTouchState = TOUCH_STATE_Y;
                        } else if (dx > MAX_X) {
                            mTouchState = TOUCH_STATE_X;
                            if (mOnSwipeListener != null) {
                                mOnSwipeListener.onSwipeStart(mTouchPosition);
                            }
                        }
                    }
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
            return super.onTouchEvent(ev);

        int action = ev.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:

                int oldPos = mTouchPosition;
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = TOUCH_STATE_NONE;

                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

                if (mTouchPosition == oldPos && mTouchView != null
                        && mTouchView.isOpen()) {
                    mTouchState = TOUCH_STATE_X;
                    mTouchView.onSwipe(ev);
                    return true;
                }

                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;

                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    onTouchEvent(cancelEvent);
                    if (mOnMenuStateChangeListener != null) {
                        mOnMenuStateChangeListener.onMenuClose(oldPos);
                    }
                    return true;
                }

                if (view instanceof View_SwipeMenuLayout) {
                    mTouchView = (View_SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);
                }

                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                break;

            case MotionEvent.ACTION_MOVE:

                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY()) - getHeaderViewsCount();

                if (mTouchPosition != mTouchView.getPosition()) {
                    break;
                }

                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));

                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }

                    getSelector().setState(new int[]{0});
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;

                } else if (mTouchState == TOUCH_STATE_NONE) {
                    if (Math.abs(dy) > MAX_Y) {
                        mTouchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) {
                        mTouchState = TOUCH_STATE_X;
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeStart(mTouchPosition);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:

                if (mTouchState == TOUCH_STATE_X) {

                    if (mTouchView != null) {
                        boolean isBeforeOpen = mTouchView.isOpen();
                        mTouchView.onSwipe(ev);
                        boolean isAfterOpen = mTouchView.isOpen();
                        if (isBeforeOpen != isAfterOpen && mOnMenuStateChangeListener != null) {
                            if (isAfterOpen) {
                                mOnMenuStateChangeListener.onMenuOpen(mTouchPosition);
                            } else {
                                mOnMenuStateChangeListener.onMenuClose(mTouchPosition);
                            }
                        }

                        if (!isAfterOpen) {
                            mTouchPosition = -1;
                            mTouchView = null;
                        }
                    }

                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void smoothOpenMenu(int position) {

        if (position >= getFirstVisiblePosition()
                && position <= getLastVisiblePosition()) {
            View view = getChildAt(position - getFirstVisiblePosition());
            if (view instanceof View_SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                }
                mTouchView = (View_SwipeMenuLayout) view;
                mTouchView.setSwipeDirection(mDirection);
                mTouchView.smoothOpenMenu();
            }
        }
    }

    public void smoothCloseMenu(){

        if (mTouchView != null && mTouchView.isOpen()) {
            mTouchView.smoothCloseMenu();
        }
    }

    private int dp2px(int dp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public void setMenuCreator(Creator_Item menuCreator) {
        this.mMenuCreator = menuCreator;
    }

    public void setOnMenuItemClickListener(

            OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {

        this.mOnSwipeListener = onSwipeListener;
    }

    public void setOnMenuStateChangeListener(OnMenuStateChangeListener onMenuStateChangeListener) {

        mOnMenuStateChangeListener = onMenuStateChangeListener;
    }

    public static interface OnMenuItemClickListener {

        boolean onMenuItemClick(int position, SliderMenu_Item menu, int index);
    }

    public static interface OnSwipeListener {

        void onSwipeStart(int position);

        void onSwipeEnd(int position);
    }

    public static interface OnMenuStateChangeListener {

        void onMenuOpen(int position);

        void onMenuClose(int position);
    }

    public static boolean inRangeOfView(View view, MotionEvent ev) {

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

}