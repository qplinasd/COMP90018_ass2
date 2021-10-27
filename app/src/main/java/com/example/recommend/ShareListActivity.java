package com.example.recommend;

import android.os.Bundle;

import com.example.recommend.databinding.ActivityMainBinding;
import com.example.recommend.databinding.ActivitySharelistBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by nashiqiye on 2021/10/26.
 */
public class ShareListActivity extends AppCompatActivity {
    private ActivitySharelistBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySharelistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
