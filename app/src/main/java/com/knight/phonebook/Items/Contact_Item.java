package com.knight.phonebook.Items;


import android.util.TypedValue;

public class Contact_Item {

    private String contact_Name, mobile_Number, land_Number, email, image_ID;

    public Contact_Item() {

    }

    public String getContact_Name() {
        return this.contact_Name;
    }

    public String getMobile_Number() {
        return this.mobile_Number;
    }

    public String getLand_Number() {
        return this.land_Number;
    }

    public String getEmail() {
        return this.email;
    }

    public String getImage_ID() {
        return image_ID;
    }

    public Boolean Has_Land() {
        return (land_Number != null) ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean Has_Mobile() {
        return (mobile_Number != null) ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean Has_Email() {
        return (email != null) ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setContact_Name(String contact_Name) {
        this.contact_Name = contact_Name;
    }

    public void setMobile_Number(String mobile_Number) {
        this.mobile_Number = mobile_Number;
    }

    public void setLand_Number(String land_Number) {
        this.land_Number = land_Number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage_ID(String image_ID) {
        this.image_ID = image_ID;
    }
}
