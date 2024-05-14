package com.example.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts_database.db";
    private static final int DATABASE_VERSION = 2; // Update the version number

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS contacts_table (" +
                "id INTEGER PRIMARY KEY," +
                "first_name TEXT," +
                "last_name TEXT," +
                "address TEXT," +
                "city TEXT," +
                "phone TEXT," +
                "email TEXT," +
                "image_uri TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade here
        if (oldVersion < 2) {
            // Perform necessary upgrade steps for version 2
            db.execSQL("ALTER TABLE contacts_table ADD COLUMN address TEXT");
        } else {
            // Handle other versions if needed
            db.execSQL("DROP TABLE IF EXISTS contacts_table");
            onCreate(db);
        }
    }
}