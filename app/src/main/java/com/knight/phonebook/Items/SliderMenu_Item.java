package com.knight.phonebook.Items;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public class SliderMenu_Item {

    private Context mContext;
    private List<Slider_Item> mItems;
    private int mViewType;

    public SliderMenu_Item(Context context) {

        mContext = context;
        mItems = new ArrayList<>();
    }

    public Context getContext() {
        return mContext;
    }

    public void addMenuItem(Slider_Item item) {
        mItems.add(item);
    }

    public void removeMenuItem(Slider_Item item) {
        mItems.remove(item);
    }

    public List<Slider_Item> getMenuItems() {
        return mItems;
    }

    public Slider_Item getMenuItem(int index) {
        return mItems.get(index);
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }
}
