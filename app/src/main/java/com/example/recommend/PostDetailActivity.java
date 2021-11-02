package com.example.recommend;

/**
 * Created by Haoran Lin on 2021/10/26.
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.recommend.databinding.ActivitySharelistBinding;

import androidx.appcompat.app.AppCompatActivity;


public class PostDetailActivity extends AppCompatActivity {
    private ActivitySharelistBinding binding;
    //进度
    private ProgressBar mProgressBar;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySharelistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String location = intent.getStringExtra("location");
//        final String url = intent.getStringExtra("url");

        //set title
        getSupportActionBar().setTitle(title);
    }


}
