package com.example.qrscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccount extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Account.db";
    public static final String TABLE_NAME = "items";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "EMAIL";
    public static final String COL_3 = "USERNAME";
    public static final String COL_4 = "PASSWORD";

    public DatabaseAccount(Context context)
    {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase myDB)
    {
        myDB.execSQL("create Table users(email TEXT, username TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int i, int i1)
    {
        myDB.execSQL("drop Table if exists users");

        onCreate(myDB);

    }

    public Boolean insertData(String email, String username, String password)
    {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = myDB.insert("users", null, contentValues);

        if (result == -1) return false;
        else
            return true;
    }

    public Boolean checkUsername(String username)
    {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkUsernamePassword(String username, String password)
    {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("Select * from users where username = ? and password = ?",
                new String[] {username, password});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public void updatePassword(String currentUser, String oldPass, String newPass)
    {
        SQLiteDatabase myDB = this.getWritableDatabase();

        String myNewPass = newPass;

        ContentValues values = new ContentValues();
        values.put("username", currentUser);
        values.put("password", newPass);

        myDB.update("users", values, "password=?", new String[]{oldPass});

    }


}
