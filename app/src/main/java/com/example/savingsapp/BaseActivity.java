package com.example.savingsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.savingsapp.db.AppDatabase;

public class BaseActivity extends AppCompatActivity {

    // The app database
    AppDatabase appDatabase;
    // The shared preferences
    protected AppDatabase initAppDb(){
        return appDatabase = AppDatabase.getInstance(this);
    }

    /**
     * Called when the activity is destroyed
     */
    protected void onDestroy(){
        AppDatabase.getInstance(this).close();
        super.onDestroy();
    }

    /**
     * Show a toast message
     * @param message The message to show
     */
    void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show a long toast message
     * @param message The message to show
     */
    void showLongToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    void gotoActivity(Context context, Class<?> toClass){
        Intent intent  =  new Intent(context, toClass);
        startActivity(intent);
    }

    /**
     * Run a task in the background
     * @param runnable The task to run
     */
    void runInBackground(Runnable runnable){
        new Thread(runnable).start();
    }

    /**
     * Check if a permission is granted
     * @param permission The permission to check
     * @return True if the permission is granted, false otherwise
     */
    boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Get the shared preferences
     * @return The shared preferences
     */
    protected SharedPreferences getSharedPreferences(){
        return getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

}
