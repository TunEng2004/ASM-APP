package com.example.campusexpensemanager;

public class Expense {
    private int id;
    private String description;
    private double amount;
    private String category;
    private long timestamp;

    // Constructor, getter và setter

    public Expense(int id, String description, double amount, String category, long timestamp) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
