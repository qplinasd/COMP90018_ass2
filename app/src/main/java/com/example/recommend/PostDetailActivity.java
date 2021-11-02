package com.example.recommend;

/**
 * Created by Haoran Lin on 2021/10/26.
 * * stuId:1019019
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PostDetailActivity extends AppCompatActivity {

    private TextView content;
    private TextView location;
    private TextView author;
    private TextView date;
    private TextView title;
    private ImageButton button_return_sharelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_postdetail);

        content = (TextView) findViewById(R.id.content);
        location = (TextView) findViewById(R.id.location);
        author = (TextView) findViewById(R.id.author);
        date = (TextView) findViewById(R.id.date);
        title = (TextView) findViewById(R.id.title);
        button_return_sharelist = (ImageButton) findViewById(R.id.button_return_sharelist);
        Intent intent = getIntent();


//        final String url = intent.getStringExtra("url");
        if (intent != null) {
            String text_title = intent.getStringExtra("title");
            String text_content = intent.getStringExtra("content");
            String text_location = intent.getStringExtra("location");
            String text_author = intent.getStringExtra("author");
            String text_date = intent.getStringExtra("date");

            title.setText(text_title);
            content.setText(text_content);
            location.setText(text_location);
            author.setText(text_author);
            date.setText(text_date);


        }
        button_return_sharelist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
