package com.example.recommend;

/**
 * Created by Haoran Lin on 2021/10/26.
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PostDetailActivity extends AppCompatActivity {

    private TextView content;
    private TextView location;
    private TextView author;
    private TextView date;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_postdetail);

        content = (TextView) findViewById(R.id.content);
        location = (TextView) findViewById(R.id.location);
        author = (TextView) findViewById(R.id.author);
        date = (TextView) findViewById(R.id.date);
        title = (TextView) findViewById(R.id.title);

        Intent intent = getIntent();


//        final String url = intent.getStringExtra("url");
        if (intent != null) {
            String t = intent.getStringExtra("title");
            String c = intent.getStringExtra("content");
            String l = intent.getStringExtra("location");
            String a = intent.getStringExtra("author");
            String d = intent.getStringExtra("date");
            title.setText(t);
            content.setText(c);
            location.setText(l);
            author.setText(a);
            date.setText(d);


        }


    }


}
