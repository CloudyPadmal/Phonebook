package com.knight.phonebook.Helpers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.knight.phonebook.R;

import java.util.ArrayList;
import java.util.List;


public class Indicator {

    public final static int DEFAULT_COLOR = 1;

    private Context mContext;
    private LinearLayout mDotLayout;
    private List<ImageView> mDots;
    private int mSlideCount;
    int selectedDotColor = DEFAULT_COLOR;
    int unselectedDotColor = DEFAULT_COLOR;
    int mCurrentposition;


    public View newInstance(@NonNull Context context) {
        mContext = context;
        mDotLayout = (LinearLayout) View.inflate(context, R.layout.view_indicator, null);
        return mDotLayout;
    }

    public void initialize(int slideCount) {

        mDots = new ArrayList<>();
        mSlideCount = slideCount;
        selectedDotColor = -1;
        unselectedDotColor = -1;

        for (int i = 0; i < slideCount; i++) {
            ImageView dot = new ImageView(mContext);
            dot.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pb_gray_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            mDotLayout.addView(dot, params);
            mDots.add(dot);
        }

        selectPosition(0);
    }

    public void selectPosition(int index) {

        mCurrentposition = index;

        for (int i = 0; i < mSlideCount; i++) {

            int drawableId = (i == index) ? (R.drawable.pb_white_dot) : (R.drawable.pb_gray_dot);
            Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);

            if (selectedDotColor != DEFAULT_COLOR && i == index)
                drawable.mutate().setColorFilter(selectedDotColor, PorterDuff.Mode.SRC_IN);
            if (unselectedDotColor != DEFAULT_COLOR && i != index)
                drawable.mutate().setColorFilter(unselectedDotColor, PorterDuff.Mode.SRC_IN);
            mDots.get(i).setImageDrawable(drawable);
        }
    }
}
