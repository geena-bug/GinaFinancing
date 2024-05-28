package com.example.savingsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.savingsapp.R;
import com.example.savingsapp.adapters.SavingsActivityAdapter;
import com.example.savingsapp.db.entities.TransactionHistory;
import com.example.savingsapp.db.entities.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends BaseFragment {

    TextView helloMessage;
    RecyclerView activitiesRecyclerView;
    View view;

    Context context;

    User user;

    CircleImageView profileImage;
    TextView textViewTotalWithdrawals;
    TextView totalSavings;

    int totalNoWithdrawals = 0;

    ArrayList<TransactionHistory> activitiesList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Context context) {
        HomeFragment fragment = new HomeFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppDb(context);
        getDataFromDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        initViews();
        loadProfileImage();
        initRecyclerView();
        return view;
    }

    void initViews(){
        helloMessage = view.findViewById(R.id.hello_message);
        textViewTotalWithdrawals = view.findViewById(R.id.total_withdrawals);
        totalSavings = view.findViewById(R.id.total_savings);

        //set the user name
        helloMessage.setText(getString(R.string.hello_message, user.firstName));
        //set the total savings
        totalSavings.setText(getString(R.string.amount, user.accountBalance));
        //set the total withdrawals
        textViewTotalWithdrawals.setText(getString(R.string.total_withdrawal, totalNoWithdrawals));
    }

    void initRecyclerView(){
        activitiesRecyclerView = view.findViewById(R.id.activities);
        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activitiesRecyclerView.setAdapter(new SavingsActivityAdapter(activitiesList));
    }

    void getDataFromDB(){
        runInBackground(() -> {
            activitiesList = (ArrayList<TransactionHistory>) appDatabase.transactionsDao().getAll();
            user = appDatabase.userDao().getById(1);
            totalNoWithdrawals = appDatabase.transactionsDao().getTotalNoOfTransactionsByType(TransactionHistory.TYPE_WITHDRAW);
        });
    }

    void loadProfileImage(){
        if(user.photoUrl == null || user.photoUrl.isEmpty()){
            return;
        }
        profileImage = view.findViewById(R.id.photo);
        Log.d("userPhotoPath", user.photoUrl);
        Glide.with(getActivity()).load(user.photoUrl).into(profileImage);
    }
}