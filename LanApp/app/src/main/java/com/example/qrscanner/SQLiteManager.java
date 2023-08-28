package com.example.qrscanner;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteManager extends SQLiteOpenHelper
{
    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "DB12";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Balance";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";

    private static final String DATE_FIELD = "date";
    private static final String BALANCE_FIELD = "balance";
    public SQLiteManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context)
    {
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        StringBuilder sql;

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(DATE_FIELD)
                .append(" TEXT, ")
                .append(BALANCE_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        /*
        switch(oldVersion)
        {
            case 1:

        }*/
    }

    public void addTransactionToDatabase(Transaction transaction)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID_FIELD, transaction.getId());
        contentValues.put(DATE_FIELD, transaction.getTitle());
        contentValues.put(BALANCE_FIELD, transaction.getDescription());

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateTransactionListArray()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null))
        {
            if(result.getCount() != 0)
            {
                while(result.moveToNext())
                {
                    int id = result.getInt(1);
                    String date = result.getString(2);
                    String balance = result.getString(3);

                    Transaction transaction = new Transaction(id, date, balance);
                    Transaction.transactionArrayList.add(transaction);

                }
            }
        }
    }

    public void updateTransactionInDB(Transaction transaction)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, transaction.getId());
        contentValues.put(DATE_FIELD, transaction.getTitle());
        contentValues.put(BALANCE_FIELD, transaction.getDescription());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =?", new String[]{String.valueOf(transaction.getId())});

    }
}
