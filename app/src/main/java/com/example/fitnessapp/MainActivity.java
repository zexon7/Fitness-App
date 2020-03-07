package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.example.fitnessapp.HomeFragment;
import com.example.fitnessapp.WeightFragment;
import com.example.fitnessapp.ProfileFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_nav;
    FrameLayout frameLayout;
    private HomeFragment HomeFragment;
    private ProfileFragment ProfileFragment;
    private WeightFragment WeightFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_nav = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.fragment_container);

        //Initialize
        HomeFragment = new HomeFragment();
        WeightFragment = new WeightFragment();
        ProfileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, HomeFragment).commit();

        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.diary:
                        InitializeFragment(HomeFragment);
                        return true;
                    case R.id.profile:
                        InitializeFragment(ProfileFragment);
                        return true;
                    case R.id.weight:
                        InitializeFragment(WeightFragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void InitializeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
