package com.knight.phonebook.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.knight.phonebook.Items.Contact_Item;

import java.util.ArrayList;


public class Databaser extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Phonebook.db";
    private static final int VERSION_NUMBER = 1;

    private static final String TABLE_CONTACTS = "Contacts";

    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_FIRST_NAME = "First_Name";
    private static final String COLUMN_MIDDLE_NAME = "Second_Name";
    private static final String COLUMN_LAST_NAME = "Last_Name";
    private static final String COLUMN_BIRTHDAY = "Birthday";
    private static final String COLUMN_LAND_NUMBER = "Land_Number";
    private static final String COLUMN_MOBILE_NUMBER = "Mobile_Number";
    private static final String COLUMN_OFFICE_NUMBER = "Office_Number";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_ADDRESS1 = "Address1";
    private static final String COLUMN_ADDRESS2 = "Address2";
    private static final String COLUMN_STREET = "Street";
    private static final String COLUMN_TOWN = "Twon";
    private static final String COLUMN_CITY = "City";
    private static final String COLUMN_PIC = "Picture";


    public Databaser(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_ID + " INT PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                COLUMN_MIDDLE_NAME + " TEXT, " +
                COLUMN_LAST_NAME + "TEXT, " +
                COLUMN_BIRTHDAY + "DATE, " +
                COLUMN_MOBILE_NUMBER + "TEXT, " +
                COLUMN_LAND_NUMBER + "TEXT, " +
                COLUMN_OFFICE_NUMBER + "TEXT, " +
                COLUMN_EMAIL + "TEXT, " +
                COLUMN_ADDRESS1 + "TEXT, " +
                COLUMN_ADDRESS2 + "TEXT, " +
                COLUMN_STREET + "TEXT, " +
                COLUMN_TOWN + "TEXT, " +
                COLUMN_CITY + "TEXT, " +
                COLUMN_PIC + "BLOB" + " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public boolean insertNewContact(Contact_Item c) {

        if (c.getFirst_Name() == null && (!c.Has_Mobile() || !c.Has_Land() || !c.Has_Office())) {

            return false;

        } else {

            long error_code;

            // Create a database object
            SQLiteDatabase DB = this.getWritableDatabase();
            // Create a content value
            ContentValues Entry_Cache = new ContentValues();

            // Generate cache line
            Entry_Cache.put(COLUMN_FIRST_NAME, c.getFirst_Name());
            Entry_Cache.put(COLUMN_MIDDLE_NAME, c.getMiddle_Name());
            Entry_Cache.put(COLUMN_LAST_NAME, c.getLast_Name());
            Entry_Cache.put(COLUMN_BIRTHDAY, c.getBirthday());
            Entry_Cache.put(COLUMN_MOBILE_NUMBER, c.getMobile_number());
            Entry_Cache.put(COLUMN_LAND_NUMBER, c.getLand_Number());
            Entry_Cache.put(COLUMN_OFFICE_NUMBER, c.getOffice_Number());
            Entry_Cache.put(COLUMN_EMAIL, c.getEmail());
            Entry_Cache.put(COLUMN_ADDRESS1, c.getAddress1());
            Entry_Cache.put(COLUMN_ADDRESS2, c.getAddress2());
            Entry_Cache.put(COLUMN_STREET, c.getStreet());
            Entry_Cache.put(COLUMN_TOWN, c.getTown());
            Entry_Cache.put(COLUMN_CITY, c.getCity());
            Entry_Cache.put(COLUMN_PIC, c.getImage());

            DB.beginTransaction();

            try {
                // Insert the cache line to the table
                error_code = DB.insert(TABLE_CONTACTS, null, Entry_Cache);
                DB.setTransactionSuccessful();
            } finally {
                DB.endTransaction();
            }
            // .insert method returns number of entries it inserted, so if it's -1 there's an error
            return error_code != -1;
       }
    }

    public ArrayList<Contact_Item> getContactList() {

        // Create an array list
        ArrayList<Contact_Item> ContactList = new ArrayList<>();
        // Create a database object
        SQLiteDatabase DB = this.getReadableDatabase();

        // Create a cursor file we get from executing this above command
        Cursor crsr = DB.query(
                TABLE_CONTACTS,
                new String[] {COLUMN_FIRST_NAME, COLUMN_MOBILE_NUMBER, COLUMN_PIC},
                null, null, null, null, COLUMN_FIRST_NAME);

        crsr.moveToFirst();

        while (!crsr.isAfterLast()) {

            // Add that to the array list
            ContactList.add(new Contact_Item(
                    crsr.getString(crsr.getColumnIndex(COLUMN_FIRST_NAME)),
                    crsr.getString(crsr.getColumnIndex(COLUMN_MOBILE_NUMBER)),
                    crsr.getBlob(crsr.getColumnIndex(COLUMN_PIC))));

            // Go to the next row
            crsr.moveToNext();
        }

        // Closes database and cursor and return the list
        crsr.close(); DB.close();
        return ContactList;
    }

    public Integer deleteAll() {

        // Create a database object
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.delete(TABLE_CONTACTS, null, null);
    }
}
