package com.pluralsight;

public class Transaction {
    private String date;
    private String time;
    private String memo;
    private String recipient;
    private double amount;


    public Transaction(String date, String time, String memo, String recipient, double amount) {
        this.date = date;
        this.time = time;
        this.memo = memo;
        this.recipient = recipient;
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    public String getMemo() {
        return memo;
    }

    public String getRecipient() {
        return recipient;
    }

    public double getAmount() {
        return amount;
    }
}
