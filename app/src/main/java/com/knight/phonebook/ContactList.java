package com.knight.phonebook;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.knight.phonebook.Adapters.Adapter_ContactList;
import com.knight.phonebook.Helpers.Logger;
import com.knight.phonebook.Items.Contact_Item;
import com.knight.phonebook.Items.SliderMenu_Item;
import com.knight.phonebook.Views.View_ContactList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class ContactList extends AppCompatActivity {

    private ArrayList<Contact_Item> list_of_contacts;
    private Adapter_ContactList adapter_contactList;
    private View_ContactList view_contactList;

    private ProgressDialog progressDialog;

    private final int CALL = 0;
    private final int MSG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading contacts...");
        progressDialog.setCancelable(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        list_of_contacts = new ArrayList<>();

        progressDialog.show();
        new get_phone_contacts().execute();

        view_contactList = (View_ContactList) findViewById(R.id.listView);
        view_contactList.setDividerHeight(-1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewContact.class));
                Logger logger = new Logger(getApplicationContext());
                logger.setFirstUsage(true);
            }
        });

        view_contactList.setOnMenuItemClickListener(new View_ContactList.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SliderMenu_Item menu, int index) {

                String number = list_of_contacts.get(position).getMobile_number();

                switch (index) {

                    case CALL:

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getApplicationContext(), "You didn't allow the app to make calls :(", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
                        }
                        break;

                    case MSG:

                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setData(Uri.parse("smsto:"));
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", number);

                        try {
                            startActivity(smsIntent);
                        }
                        catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(ContactList.this, "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });

        view_contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Contact_Item item = list_of_contacts.get(position);
                Toast.makeText(getApplicationContext(), item.getLand_Number(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contactlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private class get_phone_contacts extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            Log.d("Padmal", "Came here");

            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
            String image_uri = "";

            Log.d("Padmal", String.valueOf(cur.getCount()));
            Log.d("Padmal2", String.valueOf(cur.getColumnCount()));

            if (cur.getCount() > 0) {

                cur.moveToFirst();

                while (!cur.isAfterLast()) {

                    Contact_Item contact_item = new Contact_Item();

                    image_uri = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

                    String[] PHONES_PROJECTION = new String[] { "_id","display_name","data1","data3"};//
                    String contactId = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup._ID));
                    Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);


                    while(phone.moveToNext()) {

                        contact_item.setMobile_number("");
                        contact_item.setMobile_number(phone.getString(2));
                        break;
                    }
                    phone.close();

                    contact_item.setFirst_Name(name);
                    if (image_uri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image_uri));

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                            byte imageInByte[] = stream.toByteArray();
                            contact_item.setImage(imageInByte);
                            Log.d("Padmla", "Here Image");

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("PadmErr", "Err");
                        }
                    } else {
                        contact_item.setImage(null);
                        Log.d("EXTRA", "Came here");
                    }
                    list_of_contacts.add(contact_item);

                   cur.moveToNext();

                }
            }

            cur.close();
            adapter_contactList = new Adapter_ContactList(getApplicationContext(), list_of_contacts);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("PadmalDDD", String.valueOf(list_of_contacts.size()));
            view_contactList.setAdapter(adapter_contactList);
            progressDialog.dismiss();
        }


    }
}