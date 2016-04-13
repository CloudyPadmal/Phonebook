package com.knight.phonebook.Items;


/*
byte[] outImage=picture._image;
ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
Bitmap theImage = BitmapFactory.decodeStream(imageStream);
holder.imgIcon.setImageBitmap(theImage);
*/

public class Contact_Item {

    private String First_Name, Middle_Name, Last_Name, Birthday;
    private String Mobile_number, Land_Number, Office_Number, Email;
    private String Address1, Address2, Street, Town, City;
    private byte[] Image;

    public Contact_Item() { }

    public Contact_Item(String First_Name, String Mobile_Number, byte[] Image) {
        this.First_Name = First_Name;
        this.Mobile_number = Mobile_Number;
        this.Image = Image;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getMiddle_Name() {
        return Middle_Name;
    }

    public void setMiddle_Name(String middle_Name) {
        Middle_Name = middle_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getMobile_number() {
        return Mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        Mobile_number = mobile_number;
    }

    public String getOffice_Number() {
        return Office_Number;
    }

    public void setOffice_Number(String office_Number) {
        Office_Number = office_Number;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getLand_Number() {
        return Land_Number;
    }

    public void setLand_Number(String land_Number) {
        Land_Number = land_Number;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] Image) {
        this.Image = Image;
    }

    public Boolean Has_Land() {
        return (Land_Number != null) ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean Has_Mobile() {
        return (Mobile_number != null) ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean Has_Email() {
        return (Email != null) ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean Has_Office() {
        return (Office_Number != null) ? Boolean.TRUE : Boolean.FALSE;
    }

}
