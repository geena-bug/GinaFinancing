package com.example.savingsapp.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.savingsapp.db.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id = :id")
    User getById(int id);

    @Query("INSERT INTO users (first_name, last_name, email, password, photo_url) VALUES (:firstName, :lastName, :email, :password, :photoUrl)")
    long insert(String firstName, String lastName, String email, String password, String photoUrl);

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    User getUserByEmailAndPassword(String email, String password);

    //implement get user count
    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();

    //implement update user
    @Query("UPDATE users SET photo_url = :photoUrl WHERE id = :id")
    void updateUser(int id, String photoUrl);

    //implement update user balance
    @Query("UPDATE users SET account_balance = :balance WHERE id = :id")
    void updateUserBalance(int id, double balance);

    //implement get updated user balance
    @Query("UPDATE users SET account_balance = account_balance + :amount WHERE id = :id")
    void updateUserBalanceByAmount(int id, double amount);

}
