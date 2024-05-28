package com.example.savingsapp;

import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends BaseActivity {
    TextView helloMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();

        helloMessage.setText(getString(R.string.hello_message, "John Doe."));
    }

    void initViews(){
        helloMessage = findViewById(R.id.hello_message);
    }
}