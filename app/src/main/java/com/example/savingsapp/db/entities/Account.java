package com.example.savingsapp.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accounts")
public class Account {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "account_name")
    public String accountName;

    @ColumnInfo(name = "account_number")
    public String accountNumber;

    @ColumnInfo(name = "bank_name")
    public String bankName;
}
