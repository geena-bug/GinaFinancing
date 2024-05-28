package com.example.savingsapp.data;

public class SavingsData {
    private String description;
    private String amount;
    private String date;
    private boolean isCredit;

    public SavingsData(String description, String amount, String date, boolean isCredit) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.isCredit = isCredit;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public boolean getIsCredit() {
        return isCredit;
    }
}
