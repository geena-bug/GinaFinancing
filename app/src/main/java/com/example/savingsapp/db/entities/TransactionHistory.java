package com.example.savingsapp.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "transaction_history")
public class TransactionHistory {
    public static final String TYPE_DEPOSIT = "deposit";
    public static final String TYPE_WITHDRAW = "withdraw";
    public static final String TYPE_INTEREST = "interest";

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "description")
    public String description;
}
