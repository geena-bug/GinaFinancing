package com.example.savingsapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.savingsapp.db.entities.Account;
import com.example.savingsapp.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccountFragment extends BaseFragment implements View.OnClickListener {

    Context context;

    public AddAccountFragment() {
        // Required empty public constructor
    }
    String[] banks = {"CIBC", "BMO", "Scotia", "Bank of Canada", "Wise"};
    Button accAccountBtn;
    EditText accountNameInput;
    AutoCompleteTextView bankDropDown;
    EditText accountNumberInput;

    public static AddAccountFragment newInstance(Context context) {
        AddAccountFragment fragment = new AddAccountFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppDb(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_account, container, false);
        initViews(view);
        return view;
    }

    void initViews(View view){
        accAccountBtn = view.findViewById(R.id.add_account_btn);
        accAccountBtn.setOnClickListener(this);
        accountNameInput = view.findViewById(R.id.account_name_input);
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
        if(v.getId() == R.id.add_account_btn){
            String accountName = accountNameInput.getText().toString();
            String bankName = bankDropDown.getText().toString();
            String accountNumber = accountNumberInput.getText().toString();
            //validate the input
            if(accountName.isEmpty() || bankName.isEmpty() || accountNumber.isEmpty()){
                showToast(context,"All fields are required");
                return;
            }

            Account account = new Account();
            account.accountName = accountName;
            account.accountNumber = bankName;
            account.bankName = accountNumber;

            runInBackground(() -> {
                appDatabase.accountDao().insert(account);
            });

            navigateToFragment(WithdrawalFragment.newInstance(context));
        }
    }
}