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

    // Context to be used within the fragment
    Context context;

    // TextView for displaying user's first name
    TextView firstName;

    // TextView for displaying user's last name
    TextView lastName;

    // TextView for displaying user's email
    TextView email;

    // ImageView for displaying user's profile image
    ImageView profileImage;

    // User object to hold user information
    User user;

    // Factory method to create a new instance of ProfileFragment
    public static ProfileFragment newInstance(Context context) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the app database with the provided context
        initAppDb(context);
        // Fetch the user data in the background
        runInBackground(() -> {
            user = appDatabase.userDao().getById(1);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Initialize views with the inflated layout
        initViews(view);
        return view;
    }

    // Method to initialize views and populate user data
    void initViews(View view){
        // Find TextViews by their IDs and set user data
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        email = view.findViewById(R.id.email);

        firstName.setText(user.firstName);
        lastName.setText(user.lastName);
        email.setText(user.email);

        // Check if user has a profile photo URL
        if(user.photoUrl == null || user.photoUrl.isEmpty()){
            return;
        }
        // Find ImageView by its ID and load user photo using Glide
        profileImage = view.findViewById(R.id.photo);
        Log.d("userPhotoPath", user.photoUrl);
        Glide.with(getActivity()).load(user.photoUrl).into(profileImage);
    }
}
