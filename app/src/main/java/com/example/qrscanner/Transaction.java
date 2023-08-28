package com.example.qrscanner;

import java.util.ArrayList;
import java.util.Date;

public class Transaction
{
    public static ArrayList<Transaction> transactionArrayList = new ArrayList<>();
    private int id;
    private String title;
    private String description;
    private Date deleted;

    public Transaction(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        //this.deleted = deleted;
    }


    /*
    public Transaction(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        //deleted = null;
    }

    */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
    */

}
