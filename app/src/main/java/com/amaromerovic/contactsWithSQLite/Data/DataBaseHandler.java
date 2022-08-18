package com.amaromerovic.contactsWithSQLite.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.amaromerovic.contactsWithSQLite.model.Contact;
import com.amaromerovic.contactsWithSQLite.util.Util;

import java.util.ArrayList;

public class DataBaseHandler extends SQLiteOpenHelper {

    public DataBaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + Util.TABLE_NAME + " (" + Util.ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                                        + Util.FIRST_NAME_KEY + " TEXT NOT NULL, "
                                                                        + Util.LAST_NAME_KEY + " TEXT NOT NULL, "
                                                                        + Util.PHONE_NUMBER_KEY + " TEXT NOT NULL)";
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + Util.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }



    public void addContact(Contact contact) {
        try(SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()){
            ContentValues values = new ContentValues();
            values.put(Util.FIRST_NAME_KEY, contact.getFirstName());
            values.put(Util.LAST_NAME_KEY, contact.getLastName());
            values.put(Util.PHONE_NUMBER_KEY, contact.getPhoneNumber());

            sqLiteDatabase.insert(Util.TABLE_NAME, null , values);
        }
    }



    public Contact getContact(int id) {
        try(SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Util.TABLE_NAME, new String[]{Util.ID_KEY, Util.FIRST_NAME_KEY, Util.LAST_NAME_KEY, Util.PHONE_NUMBER_KEY},
                                          Util.ID_KEY + "=?", new String[]{String.valueOf(id)}, null, null, null)) {
            cursor.moveToFirst();

            return new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }
    }



    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        String SELECT_ALL = "SELECT * FROM " + Util.TABLE_NAME;

        try(SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL,  null)){
            if (cursor.moveToFirst()){
                do {
                    contacts.add(new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                } while (cursor.moveToNext());
            }
        }
        return contacts;
    }



    public void updateContact(Contact contact) {
        try(SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(Util.FIRST_NAME_KEY, contact.getFirstName());
            values.put(Util.LAST_NAME_KEY, contact.getLastName());
            values.put(Util.PHONE_NUMBER_KEY, contact.getPhoneNumber());

            sqLiteDatabase.update(Util.TABLE_NAME, values, Util.ID_KEY + "=?", new String[]{String.valueOf(contact.getId())});
        }
    }


    public void deleteContact(Contact contact) {
        try(SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            sqLiteDatabase.delete(Util.TABLE_NAME, Util.ID_KEY + "=?", new String[]{String.valueOf(contact.getId())});
        }
    }







}
