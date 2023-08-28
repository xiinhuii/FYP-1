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
    private static final String TIME_FIELD = "time";
    private static final String DURATION_FIELD = "duration";
    private static final String AMOUNT_FIELD = "amount";
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
                .append(TIME_FIELD)
                .append(" TEXT)")
                .append(DURATION_FIELD)
                .append(" DURATION")
                .append(AMOUNT_FIELD)
                .append(" AMOUNT)");

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

    public void addTransactionToDatabase(MyTransaction transaction)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID_FIELD, transaction.getId());
        contentValues.put(DATE_FIELD, transaction.getDate());
        contentValues.put(TIME_FIELD, transaction.getTime());
        contentValues.put(DURATION_FIELD, transaction.getDuration());
        contentValues.put(AMOUNT_FIELD, transaction.getTotalAmt());

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
                    String time = result.getString(3);
                    String duration = result.getString(4);
                    Double amount = result.getDouble(5);

                    MyTransaction transaction = new MyTransaction(id, date, time, duration, amount);
                    MyTransaction.transactionArrayList.add(transaction);

                }
            }
        }
    }

    public void updateTransactionInDB(MyTransaction transaction)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, transaction.getId());
        contentValues.put(DATE_FIELD, transaction.getDate());
        contentValues.put(TIME_FIELD, transaction.getTime());
        contentValues.put(DURATION_FIELD, transaction.getDuration());
        contentValues.put(AMOUNT_FIELD, transaction.getTotalAmt());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =?", new String[]{String.valueOf(transaction.getId())});

    }
}
