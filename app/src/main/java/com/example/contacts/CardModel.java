package com.example.contacts;
public class CardModel {

    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phoneNumber;
    private String imageUri;
    private boolean isDeleted;

    public CardModel(String firstName, String lastName, String imageUri,String email ,String address, String phoneNumber, boolean isDeleted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
        this.isDeleted = isDeleted;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getId() {
        return 0;
    }
}