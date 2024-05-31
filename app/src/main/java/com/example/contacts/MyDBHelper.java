package com.example.contacts;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE " + "Contacts" + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FirstName TEXT, " +
                "LastName TEXT, " +
                "Address TEXT, " +
                "PhoneNumber TEXT, " +
                "ImageUri TEXT, " +
                "Deleted INTEGER DEFAULT 0)";

        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "Contacts");
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public List<CardModel> getAllEntriesFromDB() {
        List<CardModel> cardModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("Contacts", null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex("FirstName"));
                    @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex("LastName"));
                    @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("Address"));
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"));
                    @SuppressLint("Range") String imageUri = cursor.getString(cursor.getColumnIndex("ImageUri"));
                    @SuppressLint("Range") boolean deleted = cursor.getInt(cursor.getColumnIndex("Deleted")) == 1;

                    CardModel cardModel = new CardModel(firstName, lastName, imageUri, address, phoneNumber, deleted);
                    cardModels.add(cardModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("MyDBHelper", "Error retrieving data from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return cardModels;
    }

    public void addEntry(CardModel cardModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FirstName", cardModel.getFirstName());
        values.put("LastName", cardModel.getLastName());
        values.put("Address", cardModel.getAddress());
        values.put("PhoneNumber", cardModel.getPhoneNumber());
        values.put("ImageUri", cardModel.getImageUri());
        values.put("Deleted", cardModel.isDeleted() ? 1 : 0);

        db.insert("Contacts", null, values);
        db.close();
    }

    public void deleteEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Contacts", "_id = ? AND _id >= 0", new String[]{String.valueOf(id)});
        db.close();
    }
}
