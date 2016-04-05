package com.knight.phonebook.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.knight.phonebook.Items.SliderMenu_Item;
import com.knight.phonebook.Items.Slider_Item;
import com.knight.phonebook.R;
import com.knight.phonebook.Views.View_ContactList;
import com.knight.phonebook.Views.View_SwipeMenu;
import com.knight.phonebook.Views.View_SwipeMenuLayout;


public class Adapter_Swipe implements WrapperListAdapter, View_SwipeMenu.OnSwipeItemClickListener {

    private ListAdapter mAdapter;
    private Context mContext;
    private View_ContactList.OnMenuItemClickListener onMenuItemClickListener;

    public Adapter_Swipe(Context context, ListAdapter adapter) {

        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View_SwipeMenuLayout layout = null;

        if (convertView == null) {

            View contentView = mAdapter.getView(position, null, parent);

            SliderMenu_Item menu = new SliderMenu_Item(mContext);
            menu.setViewType(getItemViewType(position));
            createMenu(menu);

            View_SwipeMenu menuView = new View_SwipeMenu(menu, (View_ContactList) parent);
            menuView.setOnSwipeItemClickListener(this);

            View_ContactList listView = (View_ContactList) parent;

            layout = new View_SwipeMenuLayout(contentView, menuView,
                    listView.getCloseInterpolator(),
                    listView.getOpenInterpolator());

            layout.setPosition(position);

        } else {

            layout = (View_SwipeMenuLayout) convertView;
            layout.closeMenu();
            layout.setPosition(position);
            View view = mAdapter.getView(position, layout.getContentView(), parent);
        }

        return layout;
    }

    public void createMenu(SliderMenu_Item menu) {

        Slider_Item call_item = new Slider_Item(mContext);
        call_item.setBackground(new ColorDrawable(Color.rgb(191, 54, 12)));
        call_item.setIcon(R.drawable.pb_call_icon);
        call_item.setWidth(dp2px(80));
        menu.addMenuItem(call_item);

        Slider_Item msg_item = new Slider_Item(mContext);
        msg_item.setBackground(new ColorDrawable(Color.rgb(0, 0, 0)));
        msg_item.setIcon(R.drawable.pb_msg_icon);
        msg_item.setWidth(dp2px(80));
        menu.addMenuItem(msg_item);
    }

    private int dp2px(int dp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                mContext.getResources().getDisplayMetrics());
    }

    @Override
    public void onItemClick(View_SwipeMenu view, SliderMenu_Item menu, int index) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu, index);
        }
    }

    public void setOnSwipeItemClickListener(
            View_ContactList.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return mAdapter;
    }

}
