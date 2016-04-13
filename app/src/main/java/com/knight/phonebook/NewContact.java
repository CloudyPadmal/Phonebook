package com.knight.phonebook;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class NewContact extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        String id_of = collapsingToolbarLayout.getBackground().toString();
        Log.d("ID", id_of);

        // Todo : Change the tool bar title into the person's name using the following way
        collapsingToolbarLayout.setTitle("Collapsed?");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }
}
