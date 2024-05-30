package com.example.savingsapp.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.savingsapp.db.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    /**
     * Get all users
     * @return
     */
    @Query("SELECT * FROM users")
    List<User> getAll();

    /**
     * Get user by ID
     * @param id
     * @return
     */
    @Query("SELECT * FROM users WHERE id = :id")
    User getById(int id);

    /**
     * Insert a new user
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param photoUrl
     * @return
     */
    @Query("INSERT INTO users (first_name, last_name, email, password, photo_url) VALUES (:firstName, :lastName, :email, :password, :photoUrl)")
    long insert(String firstName, String lastName, String email, String password, String photoUrl);

    /**
     * Get user by email
     * @param email
     * @return
     */
    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    /**
     * Get user by email and password
     * @param email
     * @param password
     * @return
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    User getUserByEmailAndPassword(String email, String password);

    /**
     * Get user count
     * @return
     */
    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();

    /**
     * Update user photo
     * @param id
     * @param photoUrl
     */
    @Query("UPDATE users SET photo_url = :photoUrl WHERE id = :id")
    void updateUser(int id, String photoUrl);

    /**
     * Update user balance
     * @param id
     * @param balance
     */
    @Query("UPDATE users SET account_balance = :balance WHERE id = :id")
    void updateUserBalance(int id, double balance);

    /**
     * Update user balance by amount
     * @param id
     * @param amount
     */
    @Query("UPDATE users SET account_balance = account_balance + :amount WHERE id = :id")
    void updateUserBalanceByAmount(int id, double amount);

}
