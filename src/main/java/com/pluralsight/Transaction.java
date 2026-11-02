package com.pluralsight;

import java.nio.charset.StandardCharsets;

public class Transaction {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;


    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public void setvendor(String vendor) {
        this.vendor = vendor;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getdescription() {
        return description;
    }

    public String getvendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    public String toString() {
        return String.format(
                "%s|%s|%s|%s|%.2f",
                date,
                time,
                description,
                vendor,
                amount
        );
    }

}
