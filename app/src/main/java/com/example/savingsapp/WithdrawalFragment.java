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

import com.example.savingsapp.db.entities.TransactionHistory;
import com.example.savingsapp.db.entities.User;
import com.example.savingsapp.fragments.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class WithdrawalFragment extends BaseFragment implements View.OnClickListener {
    Context context;

    String[] banks = {"CIBC", "BMO", "Scotia", "Bank of Canada", "Wise"};

    Button withdrawalBtn;
    EditText amountInput;
    AutoCompleteTextView bankDropDown;
    EditText accountNumberInput;

    User user;

    public WithdrawalFragment() {
        // Required empty public constructor
    }

    public static WithdrawalFragment newInstance(Context context) {
        WithdrawalFragment fragment = new WithdrawalFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppDb(context);
        // Get the user
        runInBackground(() -> {
            user = appDatabase.userDao().getById(1);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdrawal, container, false);
        initViews(view);
        return view;
    }

    void initViews(View view){
        withdrawalBtn = view.findViewById(R.id.withdrawal_btn);
        withdrawalBtn.setOnClickListener(this);
        amountInput = view.findViewById(R.id.amount_input);
        bankDropDown = view.findViewById(R.id.bank_drop_down);
        accountNumberInput = view.findViewById(R.id.account_number_input);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (context, android.R.layout.select_dialog_item, banks);
        //Getting the instance of AutoCompleteTextView
        bankDropDown = view.findViewById(R.id.bank_drop_down);
        bankDropDown.setThreshold(1);//will start working from first character
        bankDropDown.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.withdrawal_btn){
            // Get the amount
            String amount = amountInput.getText().toString();
            // Get the bank
            String bank = bankDropDown.getText().toString();
            // Get the account number
            String accountNumber = accountNumberInput.getText().toString();

            if(Double.parseDouble(amount) > user.accountBalance){
                showToast(context,"Insufficient balance");
                return;
            }

            // Check if any of the fields is empty
            if(amount.isEmpty() || bank.isEmpty() || accountNumber.isEmpty()){
                showToast(context,"All fields are required");
                return;
            }

            // Save the withdrawal
            runInBackground(() -> {
                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.amount = Double.parseDouble(amount);
                transactionHistory.type = TransactionHistory.TYPE_WITHDRAW;
                transactionHistory.description = String.format(Locale.getDefault(), "Withdrawal to %s", amount, bank);
                transactionHistory.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                appDatabase.transactionsDao().insertObject(transactionHistory);
            });

            // Update the user balance
            runInBackground(() -> {
                appDatabase.userDao().updateUserBalanceByAmount(1, -Double.parseDouble(amount));
            });

            showToast(context,"Withdrawal was successful");
            // Clear input fields
            amountInput.setText("");
            bankDropDown.setText("");
            accountNumberInput.setText("");
        }
    }
}