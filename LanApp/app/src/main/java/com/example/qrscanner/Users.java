package com.example.qrscanner;

public class Users {
    String email, username;
    float balance;


    public Users()
    {

    }

    public Users(String email, String username, float balance) {
        this.email = email;
        this.username = username;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
