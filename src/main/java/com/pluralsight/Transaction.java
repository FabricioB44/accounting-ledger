package com.pluralsight;

public class Transaction {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;


    // used to load transaction info from transactions.csv into the ledger array list
    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Individual setters aren't used in this code as of now, but I defined them just in case
    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    // All getters are used to display each value when user opens the ledger
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }


    // Used to parse new transactions back to the transactions.csv file
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
