package com.example.savingsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StarterActivity extends BaseActivity implements View.OnClickListener {

    Button createAccountBtn;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        initViews();
        //initialize database. This will create the database if it does not exist and will also create the tables
        initAppDb();
        //redirect to login if user exists
        redirectToLoginIfUserExists();
    }

    private void initViews(){
        //init button view
        createAccountBtn = findViewById(R.id.get_started);
        createAccountBtn.setOnClickListener(this);
        //init login text view
        loginBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.get_started) {
            gotoActivity(this, RegisterActivity.class);
        }
        else if (viewId == R.id.login) {
            gotoActivity(this, LoginActivity.class);
        }
    }

    void redirectToLoginIfUserExists(){
        //check if user exists
        runInBackground(() -> {
            if(appDatabase.userDao().getUserCount() > 0){
                gotoActivity(this, MainActivity.class);
            }
        });
    }
}