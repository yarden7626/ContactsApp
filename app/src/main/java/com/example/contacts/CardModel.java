package com.example.contacts;

public class CardModel {

    String firstName;
    String lastName;
    String imageUri;


public CardModel(String firstName,String lastName, String imageUri)
{
    this.firstName=firstName;
    this.lastName=lastName;
    this.imageUri=imageUri;

}


    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public String getImageUri() {
        return imageUri;
    }


}
