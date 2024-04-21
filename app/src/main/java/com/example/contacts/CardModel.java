package com.example.contacts;

public class CardModel {

    String text;
    String text2;
    int image;

    public CardModel(String text, int image, String text2)
    {
        this.text=text;
        this.text2=text2;
        this.image=image;
    }
    public String GetText()
    {
        return text;
    }
    public String GetText2()
    {
        return text2;
    }
    public int GetImage()
    {
        return image;
    }

}
