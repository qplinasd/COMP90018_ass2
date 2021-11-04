package com.example.recommend;
/**
 * Created by Haoran Lin on 2021/10/26.
 * * stuId:1019019
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.recommend.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        {

    private ActivityMainBinding binding;
    //Navigation
    private BottomNavigationView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initData();

    }

    //init
    private void initData() {
        mNavigation.setOnNavigationItemSelectedListener(this);
        mNavigation.setSelectedItemId(R.id.action_home);

    }

    //inti View
    private void initView() {

        mNavigation = findViewById(R.id.navigation);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        switch (item.getItemId()){
            case R.id.action_home:
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, homeFragment).commit();
                return true;
            case R.id.action_share:
                CountryFragment countryFragment = new CountryFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,countryFragment).commit();
                return true;
            case R.id.action_my:
                MyFragment myFragment = new MyFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,myFragment).commit();
                return true;

        }
        return true;
    }
}