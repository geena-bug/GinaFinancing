package com.example.savingsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.savingsapp.db.AppDatabase;

public class BaseActivity extends AppCompatActivity {

    AppDatabase appDatabase;
    protected AppDatabase initAppDb(){
        return appDatabase = AppDatabase.getInstance(this);
    }

    protected void onDestroy(){
        AppDatabase.getInstance(this).close();
        super.onDestroy();
    }

    void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    void showLongToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    void gotoActivity(Context context, Class<?> toClass){
        Intent intent  =  new Intent(context, toClass);
        startActivity(intent);
    }

    void runInBackground(Runnable runnable){
        new Thread(runnable).start();
    }

    boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

}
