package com.example.qrscanner;

import java.util.ArrayList;
import java.util.Date;

public class MyTransaction {

    public static ArrayList<MyTransaction> transactionArrayList = new ArrayList<>();
    private int id;
    private String date;
    private String time;
    private String duration;
    private Double totalAmt;

    public MyTransaction() {
        //public no-arg constructor needed
    };
    public MyTransaction(int id, String date, String time, String duration, Double totalAmt) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.totalAmt = totalAmt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }
}
