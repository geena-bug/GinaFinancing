package com.example.savingsapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.savingsapp.db.entities.Account;
import com.example.savingsapp.db.entities.TransactionHistory;
import com.example.savingsapp.db.entities.User;
import com.example.savingsapp.fragments.BaseFragment;
import com.example.savingsapp.fragments.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class WithdrawalFragment extends BaseFragment implements View.OnClickListener {
    Context context;
    Button withdrawalBtn;
    EditText amountInput;
    AutoCompleteTextView accountDropDown;
    EditText accountNumberInput;
    TextView addAccount;
    ArrayList<Account> accounts = new ArrayList<>();
    User user;

    // Withdrawal fragment
    public WithdrawalFragment() {
        // Required empty public constructor
    }

    public static WithdrawalFragment newInstance(Context context) {
        // Create a new instance of the withdrawal fragment
        WithdrawalFragment fragment = new WithdrawalFragment();
        // Set the context
        fragment.context = context;
        // Return the fragment
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppDb(context);
        // Get the user and accounts
        runInBackground(() -> {
            user = appDatabase.userDao().getById(1);
            accounts = (ArrayList<Account>) appDatabase.accountDao().getAll();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdrawal, container, false);
        // Initialize the views
        initViews(view);
        // Return the view
        return view;
    }

    void initViews(View view){
        // Initialize the views
        withdrawalBtn = view.findViewById(R.id.withdrawal_btn);
        withdrawalBtn.setOnClickListener(this);
        amountInput = view.findViewById(R.id.amount_input);
        accountDropDown = view.findViewById(R.id.account_drop_down);
        addAccount = view.findViewById(R.id.add_account);
        addAccount.setOnClickListener(this);

        // Set the account dropdown from the accounts
        String[] accountNames = new String[accounts.size()];
        for(Account account : accounts){
            accountNames[accounts.indexOf(account)] = account.accountName;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (context, android.R.layout.select_dialog_item, accountNames);
        //Getting the instance of AutoCompleteTextView
        accountDropDown = view.findViewById(R.id.account_drop_down);
        if(!accounts.isEmpty()){
            accountDropDown.setThreshold(1);//will start working from first character
        }
        accountDropDown.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.withdrawal_btn){
            // Get the amount
            String amount = amountInput.getText().toString();
            // Get the bank
            String account = accountDropDown.getText().toString();
            // Get the account number

            if(Double.parseDouble(amount) > user.accountBalance){
                showToast(context,"Insufficient balance");
                return;
            }

            // Check if any of the fields is empty
            if(amount.isEmpty() || account.isEmpty()){
                showToast(context,"All fields are required");
                return;
            }

            //prevent 0 input
            if (amount.equals("0")) {
                showToast(context,"Amount cannot be 0");
                return;
            }

            // Save the withdrawal
            runInBackground(() -> {
                // Instantiate the transaction history
                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.amount = Double.parseDouble(amount);
                transactionHistory.type = TransactionHistory.TYPE_WITHDRAW;
                transactionHistory.description = String.format(Locale.getDefault(), "Withdrawal to %s account", account);
                transactionHistory.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                // Insert the transaction history
                appDatabase.transactionsDao().insertObject(transactionHistory);
            });

            // Update the user balance
            runInBackground(() -> {
                // Update the user balance
                appDatabase.userDao().updateUserBalanceByAmount(1, -Double.parseDouble(amount));
            });
            // Show a success message
            showToast(context,"Withdrawal was successful");
            // Clear input fields
            amountInput.setText("");
            accountDropDown.setText("");
            navigateToFragment(HomeFragment.newInstance(context));
        } else if (v.getId() == R.id.add_account){
            // Navigate to the add account fragment
            navigateToFragment(AddAccountFragment.newInstance(context));
        }
    }


}