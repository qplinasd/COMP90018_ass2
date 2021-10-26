package com.example.recommend;
/**
 * Created by Haoran Lin on 2021/10/26.
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

    //初始化数据
    private void initData() {
        mNavigation.setOnNavigationItemSelectedListener(this);
        mNavigation.setSelectedItemId(R.id.action_home);

    }

    //初始化View
    private void initView() {

        mNavigation = findViewById(R.id.navigation);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()){
            case R.id.action_home:
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new HomeFragment()).commit();
                return true;
            case R.id.action_share:
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,new CountryFragment()).commit();
                return true;
            case R.id.action_my:
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,new MyFragment()).commit();
                return true;

        }
        return true;
    }
}