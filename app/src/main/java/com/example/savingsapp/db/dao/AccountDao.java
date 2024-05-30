package com.example.savingsapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.savingsapp.db.entities.Account;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM accounts")
    List<Account> getAll();

    @Insert
    long insert(Account account);

}
