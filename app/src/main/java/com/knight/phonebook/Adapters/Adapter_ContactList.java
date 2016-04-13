package com.knight.phonebook.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knight.phonebook.Items.Contact_Item;
import com.knight.phonebook.R;

import java.io.ByteArrayInputStream;
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_contact, null);
        }

        Bitmap icon = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.pb_defaultcontact_icon);

        ImageView contact_Image = (ImageView) convertView.findViewById(R.id.profile_pic);
        ImageView image_Land = (ImageView) convertView.findViewById(R.id.image_land);
        ImageView image_Mobile = (ImageView) convertView.findViewById(R.id.image_mobile);
        ImageView image_Email = (ImageView) convertView.findViewById(R.id.image_email);

        TextView contact_Name = (TextView) convertView.findViewById(R.id.contact_name);
        TextView contact_Number = (TextView) convertView.findViewById(R.id.contact_number);

        if (ContactListItems.get(position).getImage() != null) {

            byte[] outImage = ContactListItems.get(position).getImage();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            contact_Image.setImageBitmap(theImage);

        } else {

            contact_Image.setImageBitmap(icon);
        }
        contact_Name.setText(ContactListItems.get(position).getFirst_Name());
        contact_Number.setText(ContactListItems.get(position).getMobile_number());

        image_Land.setVisibility(ContactListItems.get(position).Has_Land() ? View.VISIBLE : View.INVISIBLE);
        image_Mobile.setVisibility(ContactListItems.get(position).Has_Mobile() ? View.VISIBLE : View.INVISIBLE);
        image_Email.setVisibility(ContactListItems.get(position).Has_Email() ? View.VISIBLE : View.INVISIBLE);

        convertView.setTag(ContactListItems.get(position));

        return convertView;
    }
}
