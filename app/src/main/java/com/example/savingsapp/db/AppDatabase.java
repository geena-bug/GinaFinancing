package com.example.savingsapp.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.savingsapp.db.dao.AccountDao;
import com.example.savingsapp.db.dao.TransactionsDao;
import com.example.savingsapp.db.dao.UserDao;
import com.example.savingsapp.db.entities.Account;
import com.example.savingsapp.db.entities.TransactionHistory;
import com.example.savingsapp.db.entities.User;

// Define the database entities and version
@Database(entities = {User.class, TransactionHistory.class, Account.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * The database instance
     */
    private static volatile AppDatabase INSTANCE;

    /**
     * The database name
     */
    private static final String dbName = "gina_fi_db";

    /**
     * Get the database instance
     * @param context The context
     * @return The database instance
     */
    public static AppDatabase getInstance(Context context){
        // If the instance is null, create a new instance
        if(INSTANCE == null){
            // Synchronize the instance creation
            synchronized (AppDatabase.class){
                // If the instance is still null, create a new instance
                if(INSTANCE == null){
                    // Create the database instance using Room database builder
                    INSTANCE =  Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.dbName).build();
                }
            }
        }
        // Return the existing or newly created instance
        return INSTANCE;
    }

    /**
     * Get the user DAO (Data Access Object)
     * @return The user DAO
     */
    public abstract UserDao userDao();

    /**
     * Get the transactions DAO (Data Access Object)
     * @return The transactions DAO
     */
    public abstract TransactionsDao transactionsDao();

    /**
     * Get the account DAO (Data Access Object)
     * @return The account DAO
     */
    public abstract AccountDao accountDao();
}
