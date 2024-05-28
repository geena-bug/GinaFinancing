package com.example.savingsapp.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    /**
     * The user id with auto increment
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * The user first name. Column name is first_name
     */
    @ColumnInfo(name = "first_name")
    public String firstName;
    /**
     * The user last name. Column name is last_name
     */
    @ColumnInfo(name = "last_name")
    public String lastName;

    /**
     * The user email. Column name is email
     */
    @ColumnInfo(name = "email")
    public String email;

    /**
     * The user password. Column name is password
     */
    @ColumnInfo(name = "password")
    public String password;

    /**
     * The user photo url. Column name is photo_url
     */
    @ColumnInfo(name = "photo_url")
    public String photoUrl;

    @ColumnInfo(name = "account_balance", defaultValue = "0.00")
    public double accountBalance;
}
