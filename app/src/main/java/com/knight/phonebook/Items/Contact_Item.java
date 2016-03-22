package com.knight.phonebook.Items;

/**
 * Created by Knight on 3/20/2016.
 **/

public class Contact_Item {

    private String contact_Name, mobile_Number, land_Number, email_Number, image_ID;
    private Boolean has_Land, has_Mobile, has_Email;

    public Contact_Item(String contact_Name, String mobile_Number, String land_Number, String email_Number, String image_ID) {

        this.contact_Name = contact_Name;
        this.mobile_Number = mobile_Number;
        this.land_Number = land_Number;
        this.email_Number = email_Number;
        this.image_ID = image_ID;

        this.has_Email = (email_Number != null) ? Boolean.TRUE : Boolean.FALSE;
        this.has_Land = (land_Number != null) ? Boolean.TRUE : Boolean.FALSE;
        this.has_Mobile = (mobile_Number != null) ? Boolean.TRUE : Boolean.FALSE;

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

    public String getEmail_Number() {
        return this.email_Number;
    }

    public String getImage_ID() {
        return image_ID;
    }

    public Boolean Has_Land() {
        return this.has_Land;
    }

    public Boolean Has_Mobile() {
        return this.has_Mobile;
    }

    public Boolean Has_Email() {
        return this.has_Email;
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

    public void setEmail_Number(String email_Number) {
        this.email_Number = email_Number;
    }

    public void setImage_ID(String image_ID) {
        this.image_ID = image_ID;
    }

}
