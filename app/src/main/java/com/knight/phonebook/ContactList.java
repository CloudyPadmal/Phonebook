package com.knight.phonebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.knight.phonebook.Adapters.Adapter_ContactList;
import com.knight.phonebook.Items.Contact_Item;
import com.knight.phonebook.Items.SliderMenu_Item;
import com.knight.phonebook.Views.View_ContactList;

import java.util.ArrayList;


public class ContactList extends AppCompatActivity {

    private ArrayList<Contact_Item> list_of_contacts;

    private final int CALL = 0;
    private final int MSG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        list_of_contacts = new ArrayList<>();
        new make_contact_list().execute();

        View_ContactList view_contactList = (View_ContactList) findViewById(R.id.listView);

        Adapter_ContactList adapter_contactList = new Adapter_ContactList(getApplicationContext(), list_of_contacts);

        assert view_contactList != null;
        view_contactList.setDividerHeight(0);
        view_contactList.setAdapter(adapter_contactList);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Add new contact?", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), ScrollingActivity.class));
            }
        });

        view_contactList.setOnMenuItemClickListener(new View_ContactList.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SliderMenu_Item menu, int index) {

                String number = list_of_contacts.get(position).getMobile_Number();

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contactlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class make_contact_list extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            for (int i = 0; i < 15; i++) {

                Contact_Item contact = new Contact_Item();
                contact.setContact_Name("Padmal Knight");
                contact.setMobile_Number("071085231" + i);
                contact.setLand_Number("455545454" + i);
                contact.setEmail("padmal@email.com");
                list_of_contacts.add(contact);
            }
            return null;
        }
    }
}
