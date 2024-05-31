package com.example.contacts;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Contacts";

    private static final String FIRSTNAME_FIELD = "FirstName";
    private static final String LASTNAME_FIELD = "LastName";
    private static final String ADDRESS_FIELD = "Address";
    private static final String EMAIL_FIELD = "Email";
    private static final String PHONENUMBER_FIELD = "PhoneNumber";
    private static final String IMAGEURI_FIELD = "ImageUri";
    private static final String DELETED_FIELD = "Deleted";

    private static SQLiteManager instance;

    private SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized SQLiteManager getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteManager(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                FIRSTNAME_FIELD + " TEXT, " +
                LASTNAME_FIELD + " TEXT, " +
                ADDRESS_FIELD + " TEXT, " +
                EMAIL_FIELD + " TEXT, " +
                PHONENUMBER_FIELD + " TEXT, " +
                IMAGEURI_FIELD + " TEXT, " +
                DELETED_FIELD + " INTEGER DEFAULT 0)";

        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addContact(CardModel cardModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIRSTNAME_FIELD, cardModel.getFirstName());
        values.put(LASTNAME_FIELD, cardModel.getLastName());
        values.put(ADDRESS_FIELD, cardModel.getAddress());
        values.put(EMAIL_FIELD, cardModel.getEmail());
        values.put(PHONENUMBER_FIELD, cardModel.getPhoneNumber());
        values.put(IMAGEURI_FIELD, cardModel.getImageUri());
        values.put(DELETED_FIELD, cardModel.isDeleted() ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<CardModel> getAllContacts() {
        List<CardModel> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(FIRSTNAME_FIELD));
                @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(LASTNAME_FIELD));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(ADDRESS_FIELD));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(EMAIL_FIELD));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(PHONENUMBER_FIELD));
                @SuppressLint("Range") String imageUri = cursor.getString(cursor.getColumnIndex(IMAGEURI_FIELD));
                @SuppressLint("Range") boolean deleted = cursor.getInt(cursor.getColumnIndex(DELETED_FIELD)) == 1;

                CardModel contact = new CardModel(firstName, lastName, address,email, phoneNumber, imageUri, deleted);
                contactList.add(contact);
            }
            cursor.close();
        }
        db.close();
        return contactList;
    }

    public void deleteContact(CardModel cardModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, FIRSTNAME_FIELD + " = ? AND " + LASTNAME_FIELD + " = ?",
                new String[]{cardModel.getFirstName(), cardModel.getLastName()});
        db.close();
    }
}
