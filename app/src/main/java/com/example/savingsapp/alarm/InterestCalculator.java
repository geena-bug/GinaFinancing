package com.example.savingsapp.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.savingsapp.db.AppDatabase;
import com.example.savingsapp.db.entities.TransactionHistory;
import com.example.savingsapp.db.entities.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InterestCalculator extends BroadcastReceiver {
    // The app database
    AppDatabase appDatabase;

    // The interest rate per annum
    double interestRatePerAnnum = 10.0;

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    public void onReceive(Context context, Intent intent) {
        appDatabase = AppDatabase.getInstance(context);
        calculateInterest();
    }

    void calculateInterest(){
        //run db transaction in background
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = appDatabase.userDao().getById(1);
                if(user != null){
                    //calculate interest
                    double balance = user.accountBalance;
                    //calculate interest
                    double interest = calculateInterestToday(balance);
                    //update user balance
                    appDatabase.userDao().updateUserBalanceByAmount(1, interest);
                    //log transaction
                    TransactionHistory transactionHistory = new TransactionHistory();
                    transactionHistory.amount = interest;
                    transactionHistory.type = TransactionHistory.TYPE_INTEREST;
                    transactionHistory.description = String.format(Locale.getDefault(), "Interest of %.2f", interest);
                    transactionHistory.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    //insert transaction
                    appDatabase.transactionsDao().insertObject(transactionHistory);
                }
            }
        }).start();
    }

    /**
     * Calculate the interest for today
     * @param balance The balance
     * @return The interest
     */
    double calculateInterestToday(double balance){
        return balance * interestRatePerAnnum / 365;
    }
}
