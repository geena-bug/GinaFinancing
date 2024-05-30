package com.example.savingsapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.savingsapp.db.entities.Account;
import com.example.savingsapp.db.entities.User;
import com.example.savingsapp.fragments.BaseFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {

    Context context;

    TextView firstName;

    TextView lastName;

    TextView email;
    ImageView profileImage;

    User user;
    public static ProfileFragment newInstance(Context context) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        return view;
    }

    void initViews(View view){
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        email = view.findViewById(R.id.email);

        firstName.setText(user.firstName);
        lastName.setText(user.lastName);
        email.setText(user.email);

        if(user.photoUrl == null || user.photoUrl.isEmpty()){
            return;
        }
        profileImage = view.findViewById(R.id.photo);
        Log.d("userPhotoPath", user.photoUrl);
        Glide.with(getActivity()).load(user.photoUrl).into(profileImage);
    }
}