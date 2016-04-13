package com.knight.phonebook.Views;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knight.phonebook.Items.SliderMenu_Item;
import com.knight.phonebook.Items.Slider_Item;

import java.util.List;

public class View_SwipeMenu extends LinearLayout implements View.OnClickListener {

    private View_ContactList mListView;
    private View_SwipeMenuLayout mLayout;
    private SliderMenu_Item mMenu;
    private OnSwipeItemClickListener onItemClickListener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public View_SwipeMenu(SliderMenu_Item menu, View_ContactList listView) {
        super(menu.getContext());
        mListView = listView;
        mMenu = menu;
        List<Slider_Item> items = menu.getMenuItems();
        int id = 0;
        for (Slider_Item item : items) {
            addItem(item, id++);
        }
    }

    private void addItem(Slider_Item item, int id) {
        LayoutParams params = new LayoutParams(item.getWidth(), LayoutParams.MATCH_PARENT);
        LinearLayout parent = new LinearLayout(getContext());
        parent.setId(id);
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        parent.setBackground(item.getBackground());
        parent.setOnClickListener(this);
        addView(parent);

        if (item.getIcon() != null) {
            parent.addView(createIcon(item));
        }
        if (!TextUtils.isEmpty(item.getTitle())) {
            parent.addView(createTitle(item));
        }

    }

    private ImageView createIcon(Slider_Item item) {
        ImageView iv = new ImageView(getContext());
        iv.setImageDrawable(item.getIcon());
        return iv;
    }

    private TextView createTitle(Slider_Item item) {
        TextView tv = new TextView(getContext());
        tv.setText(item.getTitle());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(item.getTitleSize());
        tv.setTextColor(item.getTitleColor());
        return tv;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null && mLayout.isOpen()) {
            onItemClickListener.onItemClick(this, mMenu, v.getId());
        }
    }

    public void setOnSwipeItemClickListener(OnSwipeItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setLayout(View_SwipeMenuLayout mLayout) {
        this.mLayout = mLayout;
    }

    public interface OnSwipeItemClickListener {
        void onItemClick(View_SwipeMenu view, SliderMenu_Item menu, int index);
    }
}
