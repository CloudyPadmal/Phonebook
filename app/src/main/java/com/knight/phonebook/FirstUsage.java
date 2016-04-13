package com.knight.phonebook;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.knight.phonebook.Adapters.Adapter_Fragments;
import com.knight.phonebook.Helpers.Databaser;
import com.knight.phonebook.Items.Contact_Item;
import com.knight.phonebook.Helpers.ControlPager;
import com.knight.phonebook.Helpers.Indicator;
import com.knight.phonebook.Views.SampleSlide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class FirstUsage extends AppCompatActivity {

    public final static int DEFAULT_COLOR = 1;
    private static final int DEFAULT_SCROLL_DURATION_FACTOR = 1;

    protected Adapter_Fragments mPagerAdapter;
    protected ControlPager pager;
    protected List<Fragment> fragments = new Vector<>();
    protected int slidesNumber;
    protected Vibrator mVibrator;
    protected Indicator mController;
    protected int vibrateIntensity = 20;
    protected boolean baseProgressButtonEnabled = true;
    protected boolean progressButtonEnabled = true;
    protected View nextButton;
    protected View doneButton;
    protected FrameLayout backgroundFrame;
    protected int savedCurrentItem;
    protected ProgressDialog progressDialog;
    protected Databaser databaser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_usage);

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Importing phone contacts to Phonebook... :)");
        progressDialog.setCancelable(false);

        databaser = new Databaser(getApplicationContext());

        nextButton = findViewById(R.id.next);
        doneButton = findViewById(R.id.done);
        backgroundFrame = (FrameLayout) findViewById(R.id.background);
        mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        mPagerAdapter = new Adapter_Fragments(getSupportFragmentManager(), fragments);
        pager = (ControlPager) findViewById(R.id.view_pager);
        pager.setAdapter(mPagerAdapter);

        if (savedInstanceState != null) {
            restoreLockingState(savedInstanceState);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                mVibrator.vibrate(vibrateIntensity);
                pager.setCurrentItem(pager.getCurrentItem() + 1);

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                mVibrator.vibrate(vibrateIntensity * 2);
                onDonePressed();
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (slidesNumber > 1)
                    mController.selectPosition(position);

                if (!pager.isNextPagingEnabled()) {
                    if (pager.getCurrentItem() != pager.getLockPage()) {
                        setProgressButtonEnabled(baseProgressButtonEnabled);
                        pager.setNextPagingEnabled(true);
                    } else {
                        setProgressButtonEnabled(progressButtonEnabled);
                    }
                } else {
                    setProgressButtonEnabled(progressButtonEnabled);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setScrollDurationFactor(DEFAULT_SCROLL_DURATION_FACTOR);

        pager.setCurrentItem(savedCurrentItem);

        init(savedInstanceState);
        slidesNumber = fragments.size();

        if (slidesNumber == 1) {
            setProgressButtonEnabled(progressButtonEnabled);
        } else {
            initController();
        }
    }

    protected void setScrollDurationFactor(int factor) {
        pager.setScrollDurationFactor(factor);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("baseProgressButtonEnabled", baseProgressButtonEnabled);
        outState.putBoolean("progressButtonEnabled", progressButtonEnabled);
        outState.putBoolean("nextEnabled", pager.isPagingEnabled());
        outState.putBoolean("nextPagingEnabled", pager.isNextPagingEnabled());
        outState.putInt("lockPage", pager.getLockPage());
        outState.putInt("currentItem", pager.getCurrentItem());
    }


    protected void restoreLockingState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.baseProgressButtonEnabled = savedInstanceState.getBoolean("baseProgressButtonEnabled");
        this.progressButtonEnabled = savedInstanceState.getBoolean("progressButtonEnabled");
        this.savedCurrentItem = savedInstanceState.getInt("currentItem");
        pager.setPagingEnabled(savedInstanceState.getBoolean("nextEnabled"));
        pager.setNextPagingEnabled(savedInstanceState.getBoolean("nextPagingEnabled"));
        pager.setLockPage(savedInstanceState.getInt("lockPage"));
    }

    private void initController() {
        if (mController == null)
            mController = new Indicator();

        FrameLayout indicatorContainer = (FrameLayout) findViewById(R.id.indicator_container);
        assert indicatorContainer != null;
        indicatorContainer.addView(mController.newInstance(this));

        mController.initialize(slidesNumber);

    }

    public void addSlide(@NonNull Fragment fragment) {
        fragments.add(fragment);
        mPagerAdapter.notifyDataSetChanged();
    }

    public void setProgressButtonEnabled(boolean progressButtonEnabled) {
        this.progressButtonEnabled = progressButtonEnabled;
        if (progressButtonEnabled) {
            if (pager.getCurrentItem() == slidesNumber - 1) {
                setButtonState(nextButton, false);
                setButtonState(doneButton, true);
            } else {
                setButtonState(nextButton, true);
                setButtonState(doneButton, false);
            }
        } else {
            setButtonState(nextButton, false);
            setButtonState(doneButton, false);
        }
    }

    private void setButtonState(View button, boolean show) {
        if (show) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }


    public void init(@Nullable Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));

        pager.setPageTransformer(true, new PageTransformer());
    }

    public void onDonePressed() {

        Intent intent = new Intent(this, ContactList.class);
        startActivity(intent);
    }


    @Override
    public boolean onKeyDown(int code, KeyEvent kevent) {
        if (code == KeyEvent.KEYCODE_ENTER || code == KeyEvent.KEYCODE_BUTTON_A || code == KeyEvent.KEYCODE_DPAD_CENTER) {
            ViewPager vp = (ViewPager) this.findViewById(R.id.view_pager);
            if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1) {
                onDonePressed();
            } else {
                vp.setCurrentItem(vp.getCurrentItem() + 1);
            }
            return false;
        }
        return super.onKeyDown(code, kevent);
    }

    public void test (View view) {
        new get_phone_contacts().execute();
    }

    private class get_phone_contacts extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
            String image_uri = "";

            assert cur != null;
            if (cur.getCount() > 0) {

                cur.moveToFirst();

                while (!cur.isAfterLast()) {

                    Contact_Item contact_item = new Contact_Item();

                    image_uri = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

                    String[] PHONES_PROJECTION = new String[] { "_id","display_name","data1","data3"};
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

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        contact_item.setImage(null);
                    }
                    databaser.insertNewContact(contact_item);
                    cur.moveToNext();
                }
            }

            cur.close();
           // adapter_contactList = new Adapter_ContactList(getApplicationContext(), list_of_contacts);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

          //  view_contactList.setAdapter(adapter_contactList);
          progressDialog.dismiss();
        }
    }

    public class PageTransformer implements ViewPager.PageTransformer {

        public PageTransformer() {}

        public void transformPage(View page, float position) {
            page.setRotationY(position * -30f);
        }
    }
}
