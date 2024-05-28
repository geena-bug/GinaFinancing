package com.example.savingsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    Button signupButton;
    TextView loginTextView;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText passwordEditText;
    EditText emailEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //init views
        initViews();
        //initialize database
        this.initAppDb();
    }

    private void initViews(){
        //init button view
        signupButton = findViewById(R.id.login_btn);
        signupButton.setOnClickListener(this);
        //init login text view
        loginTextView = findViewById(R.id.signup_text);
        loginTextView.setOnClickListener(this);

        //init first name edit text
        firstNameEditText = findViewById(R.id.first_name_input);
        //init last name edit text
        lastNameEditText = findViewById(R.id.last_name_input);
        //init email edit text
        emailEditText = findViewById(R.id.email_input);
        //init password edit text
        passwordEditText = findViewById(R.id.password_input);
    }

    private void handleSignup(){
        //get the user input
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        //validate the user input
        if(firstName.isEmpty()){
            showToast("First name is required");
            return;
        }
        if(lastName.isEmpty()){
            showToast("Last name is required");
            return;
        }
        if(email.isEmpty()){
            showToast("Email is required");
            return;
        }
        if(password.isEmpty()){
            showToast("Password is required");
            return;
        }
        //run the database operation in the background
        runInBackground(() -> {
            //insert the user into the database
            long id = appDatabase.userDao().insert(firstName, lastName, email, password, "");
            //show toast
            runOnUiThread(() ->showToast("User created successfully"));
            //go to the photo activity
            Intent intent  =  new Intent(RegisterActivity.this, PhotoActivity.class);
            intent.putExtra("userId", id);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.login_btn){
            handleSignup();
        } else if (id == R.id.signup_text) {
            gotoActivity(this, LoginActivity.class);
        }
    }
}