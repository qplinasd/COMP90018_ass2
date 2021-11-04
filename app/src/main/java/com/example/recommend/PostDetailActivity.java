package com.example.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView content;
    private TextView location;
    private TextView author;
    private TextView date;
    private TextView title;
    private ImageButton button_return_sharelist;
    private ImageView img;

    private ImageButton button_left_image;
    private ImageButton button_right_image;
    private ImageView post_image;
    private List<StorageReference> imageList = new ArrayList<>();
    private int currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_postdetail);

        // binding XML components
        content = (TextView) findViewById(R.id.content);
        location = (TextView) findViewById(R.id.location);
        author = (TextView) findViewById(R.id.author);
        date = (TextView) findViewById(R.id.date);
        title = (TextView) findViewById(R.id.title);
        button_return_sharelist = (ImageButton) findViewById(R.id.button_return_sharelist);
        img = (ImageView) findViewById(R.id.img);

        button_left_image = (ImageButton) findViewById(R.id.button_left_image);
        button_right_image = (ImageButton) findViewById(R.id.button_right_image);
        post_image = (ImageView) findViewById(R.id.post_image);

        currentImage = 0;

        initView();
    }

    private void initView(){
        Intent intent = getIntent();

        if (intent != null) {

            // get post content
            String text_title = intent.getStringExtra("title");
            String text_content = intent.getStringExtra("content");
            String text_location = intent.getStringExtra("location");
            String text_author = intent.getStringExtra("author");
            String text_date = intent.getStringExtra("date");

            String key = intent.getStringExtra("key");

            // set post content
            title.setText(text_title);
            content.setText(text_content);
            location.setText(text_location);
            author.setText(text_author);
            date.setText(text_date);

            // get user image
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("profileImages/"+text_author+".jpg");
            GlideApp.with(this)
                    .load(storageRef)
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .error(getResources().getDrawable(R.drawable.profile_image_test))
                    .into(img);

            // get post images
            StorageReference listRef = storage.getReference().child("posts/"+key);
            listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    createImageList(listResult);
                }
            });



        }

        // return to previous page
        button_return_sharelist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // return to previous page
                finish();
            }
        });

    }

    private void createImageList(ListResult listResult){
        // All the items under listRef.
        imageList.addAll(listResult.getItems());

        if(imageList.size()>0){
            // set post images
            GlideApp.with(this)
                    .load(imageList.get(currentImage))
                    .error(getResources().getDrawable(R.drawable.background_share))
                    .into(post_image);

            if(imageList.size()>1){
                // set left and right image button
                button_left_image.setOnClickListener(this);
                button_right_image.setOnClickListener(this);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_left_image:
                // left image button click event
                if(currentImage>0){
                    currentImage -= 1;
                    // show previous image
                    GlideApp.with(this)
                            .load(imageList.get(currentImage))
                            .error(getResources().getDrawable(R.drawable.background_share))
                            .into(post_image);
                }
                break;
            case R.id.button_right_image:
                // right image button click event
                if(currentImage<imageList.size()-1){
                    currentImage += 1;
                    // show next image
                    GlideApp.with(this)
                            .load(imageList.get(currentImage))
                            .error(getResources().getDrawable(R.drawable.background_share))
                            .into(post_image);
                }
                break;
        }
    }
}