package com.example.savingsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.savingsapp.db.entities.User;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView signupTextView;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialize database
        initAppDb();
        //initialize views
        initViews();
    }

    private void initViews(){
        //init email edit text
        emailEditText = findViewById(R.id.email_input);
        //init password edit text
        passwordEditText = findViewById(R.id.password_input);
        //init login button
        loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(this);
        //init signup text view
        signupTextView = findViewById(R.id.signup_text);
        signupTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.login_btn) {
            handleLogin();
        }
        else if (viewId == R.id.signup_text) {
            gotoActivity(this, RegisterActivity.class);
        }
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //validate the user input
        if (email.isEmpty()) {
            showToast("Email is required");
            return;
        }
        if (password.isEmpty()) {
            showToast("Password is required");
            return;
        }

        //Run the login process in a background thread
        runInBackground(() -> {
            user = appDatabase.userDao().getUserByEmailAndPassword(email, password);
            if (user == null) {
                runOnUiThread(() -> showToast("Login invalid"));
            }else {
                runOnUiThread(() -> showToast("Login successful"));
                //go to home activity
                gotoActivity(LoginActivity.this, MainActivity.class);
            }
        });
    }
}