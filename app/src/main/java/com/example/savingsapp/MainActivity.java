package com.example.savingsapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.savingsapp.alarm.InterestCalculator;
import com.example.savingsapp.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        sharedPreferences = getSharedPreferences();
        boolean isLoggedIn = sharedPreferences.getBoolean(getString(R.string.is_logged_in), false);
        if(!isLoggedIn){
            finish();
            gotoActivity(this, LoginActivity.class);
        }
        setAlarm();
        replaceFragment(HomeFragment.newInstance(this));
    }

    void initViews(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    private  void replaceFragment (Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container, fragment, null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save){
            Log.d("MainActivity", "Save clicked");
            replaceFragment(SaveFragment.newInstance(this));
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menuItemId =  menuItem.getItemId();
        if(menuItemId == R.id.save){
            replaceFragment(SaveFragment.newInstance(this));
        }
        else if(menuItemId == R.id.home){
            replaceFragment(HomeFragment.newInstance(this));
        }
        else if(menuItemId == R.id.withdraw){
            replaceFragment(WithdrawalFragment.newInstance(this));
        }
        else if(menuItemId == R.id.logout){
            finish();
            sharedPreferences.edit()
                    .putBoolean(getString(R.string.is_logged_in), false)
                    .apply();
            gotoActivity(this, LoginActivity.class);
        } else if (menuItemId == R.id.profile) {
            replaceFragment(ProfileFragment.newInstance(this));
        }
        return true;
    }

    private void setAlarm() {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent intent = new Intent(this, InterestCalculator.class);

        //creating a pending intent using the intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //setting the repeating alarm that will be fired every day
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);

        //Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }
}