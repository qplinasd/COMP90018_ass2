package com.example.recommend;

import android.os.Bundle;

import com.example.recommend.databinding.ActivityFavouriteBinding;
import com.example.recommend.databinding.ActivitySharelistBinding;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by nashiqiye on 2021/10/26.
 */
public class FavouriteActivity extends AppCompatActivity {
    private ActivityFavouriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
