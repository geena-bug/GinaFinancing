package com.example.savingsapp.fragments;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.savingsapp.R;
import com.example.savingsapp.db.AppDatabase;

public class BaseFragment extends Fragment {
    protected AppDatabase appDatabase;

    protected AppDatabase initAppDb(Context context){
        return appDatabase = AppDatabase.getInstance(context);
    }

    protected void runInBackground(Runnable runnable){
        new Thread(runnable).start();
    }

   protected void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    protected void navigateToFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .detach(fragment)
                .attach(fragment)
                .commit();
    }

}
