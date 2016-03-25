package com.knight.phonebook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knight.phonebook.Items.Contact_Item;
import com.knight.phonebook.R;

import java.util.ArrayList;


public class Adapter_ContactList extends BaseAdapter {

    private Context context;
    private ArrayList<Contact_Item> ContactListItems;

    public Adapter_ContactList(Context context, ArrayList<Contact_Item> ContactListItems) {

        this.context = context;
        this.ContactListItems = ContactListItems;
    }

    @Override
    public int getCount() {
        return ContactListItems.size();
    }

    @Override
    public Contact_Item getItem(int position) {
        return ContactListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            // TODO: Create a better UI for this list layout
            convertView = layoutInflater.inflate(R.layout.list_item_contact, null);
        }

        ImageView contact_Image = (ImageView) convertView.findViewById(R.id.contact_image);
        ImageView image_Land = (ImageView) convertView.findViewById(R.id.image_land);
        ImageView image_Mobile = (ImageView) convertView.findViewById(R.id.image_mobile);
        ImageView image_Email = (ImageView) convertView.findViewById(R.id.image_email);

        TextView contact_Name = (TextView) convertView.findViewById(R.id.contact_name);
        TextView contact_Number = (TextView) convertView.findViewById(R.id.contact_number);

        contact_Name.setText(ContactListItems.get(position).getContact_Name());
        contact_Number.setText(ContactListItems.get(position).getMobile_Number());

        image_Land.setVisibility(ContactListItems.get(position).Has_Land() ? View.VISIBLE : View.INVISIBLE);
        image_Mobile.setVisibility(ContactListItems.get(position).Has_Mobile() ? View.VISIBLE : View.INVISIBLE);
        image_Email.setVisibility(ContactListItems.get(position).Has_Email() ? View.VISIBLE : View.INVISIBLE);

        convertView.setTag(ContactListItems.get(position));

        return convertView;
    }
}
