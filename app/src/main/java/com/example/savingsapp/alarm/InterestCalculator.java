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
    AppDatabase appDatabase;
    double interestRatePerAnnum = 10.0;
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
                    double balance = user.accountBalance;
                    double interest = calculateInterestToday(balance);
                    appDatabase.userDao().updateUserBalanceByAmount(1, interest);
                    //log transaction
                    TransactionHistory transactionHistory = new TransactionHistory();
                    transactionHistory.amount = interest;
                    transactionHistory.type = TransactionHistory.TYPE_INTEREST;
                    transactionHistory.description = String.format(Locale.getDefault(), "Interest of %.2f", interest);
                    transactionHistory.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    appDatabase.transactionsDao().insertObject(transactionHistory);
                }
            }
        }).start();
    }

    double calculateInterestToday(double balance){
        return balance * interestRatePerAnnum / 365;
    }
}
