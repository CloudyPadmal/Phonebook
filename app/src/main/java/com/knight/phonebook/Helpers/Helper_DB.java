package com.knight.phonebook.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Knight on 3/20/2016.
 **/

public class Helper_DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Accounts.db";
    private static final int VERSION_NUMBER = 1;


    public Helper_DB(Context context) {

        super(context, DATABASE_NAME, null, VERSION_NUMBER);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
