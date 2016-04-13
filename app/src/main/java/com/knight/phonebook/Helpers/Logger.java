package com.knight.phonebook.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


public class Logger {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREFERENCES_LABEL = "Phonebook";
    private static final String FIRST_USAGE = "First Usage";

    @SuppressLint("CommitPrefEdits")
    public Logger(Context context) {

        this.context = context;
        preferences = context.getSharedPreferences(PREFERENCES_LABEL, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public void setFirstUsage(boolean firstUsage) {

        editor.putBoolean(FIRST_USAGE, firstUsage);
        editor.commit();

    }

    public boolean isFirstUsage() {

        return preferences.getBoolean(FIRST_USAGE, true);

    }
}
