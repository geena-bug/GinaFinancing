package com.example.savingsapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.savingsapp.db.entities.TransactionHistory;
import com.example.savingsapp.fragments.BaseFragment;
import com.example.savingsapp.fragments.HomeFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFragment extends BaseFragment implements View.OnClickListener {
    Context context;

    Button saveBtn;
    TextInputEditText amountInput;
    TextInputEditText cardNumberInput;
    TextInputEditText nameInput;
    TextInputEditText cvvInput;
    TextInputEditText expiryInput;

    public SaveFragment() {
        // Required empty public constructor
    }
    /** Create a new instance of the SaveFragment */
    public static SaveFragment newInstance(Context context) {
        SaveFragment fragment = new SaveFragment();
        fragment.context = context;
        return fragment;
    }

    /** Called when the fragment is created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppDb(context);
    }

    /** Create the view of the fragment */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        initViews(view);
        return view;
    }

    /** Initialize the views */
    void initViews(View view){
        // Initialize the views
        saveBtn = view.findViewById(R.id.save_btn);
        // Initialize the input fields
        amountInput = view.findViewById(R.id.amount_input);
        // Initialize the card input fields
        cardNumberInput = view.findViewById(R.id.card_number_input);
        // Initialize the name input field
        nameInput = view.findViewById(R.id.card_name_input);
        // Initialize the cvv input field
        cvvInput = view.findViewById(R.id.card_cvv_input);
        // Initialize the expiry input field
        expiryInput = view.findViewById(R.id.card_exp_input);
        // Set the click listener
        saveBtn.setOnClickListener(this);
    }

    void saveData(){
        // Get the input values
        String amount = amountInput.getText().toString();
        // Get the card number
        String cardNumber = cardNumberInput.getText().toString();
        // Get the name
        String name = nameInput.getText().toString();
        // Get the cvv
        String cvv = cvvInput.getText().toString();
        // Get the expiry date
        String expiry = expiryInput.getText().toString();

        // Validate the input values
        if(amount.isEmpty() || cardNumber.isEmpty() || name.isEmpty() || cvv.isEmpty() || expiry.isEmpty()){
            showToast(context,"All fields are required");
            return;
        }

        // Validate the card number
        if(!isValidExpiryDate(expiry)){
            showToast(context,"Invalid Expiry date");
            return;
        }

        //prevent 0 input
        if (amount.equals("0")) {
            showToast(context,"Amount cannot be 0");
            return;
        }

        //get current date
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //transaction description
        String description = "Deposit into savings account";
        //save data to db
        runInBackground(() -> {
            appDatabase.transactionsDao().insert(1, Double.parseDouble(amount),date, TransactionHistory.TYPE_DEPOSIT, description );
            appDatabase.userDao().updateUserBalanceByAmount(1, Double.parseDouble(amount));
            Log.d("SaveFragmentData", "Data saved");
        });

        //show success message
        showToast(context,"Savings was successful");

        //clear input fields
        amountInput.setText("");
        // Clear the card input fields
        cardNumberInput.setText("");
        // Clear the name input field
        nameInput.setText("");
        // Clear the cvv input field
        cvvInput.setText("");
        // Clear the expiry input field
        expiryInput.setText("");
        // Navigate to the home fragment
        navigateToFragment(HomeFragment.newInstance(context));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.save_btn){
            saveData();
        }
    }

    private boolean isValidExpiryDate(String expiryDate) {
        // Regex to validate expiry date with a forward slash (e.g., "MM/YY")
        String expiryPattern = "^(0[1-9]|1[0-2])/(\\d{2})$";
        if (TextUtils.isEmpty(expiryDate) || !Pattern.matches(expiryPattern, expiryDate)) {
            return false;
        }

        // Additional logic to check if the expiry date is in the future
        Matcher matcher = Pattern.compile(expiryPattern).matcher(expiryDate);
        if (matcher.matches()) {
            int month = Integer.parseInt(matcher.group(1));
            int year = Integer.parseInt(matcher.group(2)) + 2000;

            // Get the current month and year
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            int currentMonth = calendar.get(java.util.Calendar.MONTH) + 1;
            int currentYear = calendar.get(java.util.Calendar.YEAR);

            // Validate if the expiry date is in the future
            if (year > currentYear || (year == currentYear && month >= currentMonth)) {
                return true;
            }
        }
        return false;
    }
}