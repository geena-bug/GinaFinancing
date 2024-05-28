package com.example.savingsapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.savingsapp.db.entities.TransactionHistory;

import java.util.List;

@Dao
public interface TransactionsDao {
    @Query("INSERT INTO transaction_history (user_id, amount, date, type, description) VALUES (:userId, :amount, :date, :type, :description)")
    long insert(int userId, double amount, String date, String type, String description);

    @Insert
    long insertObject(TransactionHistory transactionHistory);

    @Query("SELECT * FROM transaction_history")
    List<TransactionHistory> getAll();

    @Query("SELECT COUNT(id) FROM transaction_history WHERE type = :type")
    int getTotalNoOfTransactionsByType(String type);
}
