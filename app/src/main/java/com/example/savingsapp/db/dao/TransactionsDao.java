package com.example.savingsapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.savingsapp.db.entities.TransactionHistory;

import java.util.List;

@Dao
public interface TransactionsDao {
    /**
     * Insert a new transaction
     * @param userId The user ID
     * @param amount The amount
     * @param date The date
     * @param type The type
     * @param description The description
     * @return The ID of the inserted transaction
     * @param userId
     * @param amount
     * @param date
     * @param type
     * @param description
     * @return
     */
    @Query("INSERT INTO transaction_history (user_id, amount, date, type, description) VALUES (:userId, :amount, :date, :type, :description)")
    long insert(int userId, double amount, String date, String type, String description);

    /**
     * Insert a new transaction
     * @param transactionHistory
     * @return
     */
    @Insert
    long insertObject(TransactionHistory transactionHistory);

    /**
     * Get all transactions
     * @return
     */
    @Query("SELECT * FROM transaction_history")
    List<TransactionHistory> getAll();

    /**
     * Get all transactions by user ID
     * @param type
     * @return
     */
    @Query("SELECT COUNT(id) FROM transaction_history WHERE type = :type")
    int getTotalNoOfTransactionsByType(String type);
}
