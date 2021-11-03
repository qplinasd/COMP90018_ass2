package com.example.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.recommend.adapter.PostAdapter;
import com.example.recommend.application.MyApplication;
import com.example.recommend.data.FavouritePost;
import com.example.recommend.data.Post;
import com.example.recommend.databinding.ActivityFavouriteBinding;
import com.example.recommend.databinding.ActivitySharelistBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nashiqiye on 2021/10/26.
 */
public class FavouriteActivity extends AppCompatActivity implements ChildEventListener, ValueEventListener {

    private ActivityFavouriteBinding binding;
    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private ListView mListView;
    private List<FavouritePost> mList = new ArrayList<>();
    private MyApplication app;
    private ImageButton button_return_my;
    private List<Post> postsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findView();
        getPostInfo();
    }

    public void getPostInfo(){
        app = (MyApplication) getApplication();

        DatabaseReference postData = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("userFavourite");
        postData.orderByChild("username").equalTo(app.getUsername()).addChildEventListener(this);
    }

    //init View
    private void findView() {
        mListView = binding.favouriteListView;
        button_return_my = binding.buttonReturnMy;

        button_return_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //clicking
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                intent.putExtra("title", postsList.get(position).getTitle());
                intent.putExtra("location", postsList.get(position).getLocation());
                intent.putExtra("date", postsList.get(position).getDate());
                intent.putExtra("author", postsList.get(position).getAuthor());
                intent.putExtra("content", postsList.get(position).getContent());

                intent.putExtra("key", mList.get(position).getPostKey());

                startActivity(intent);
            }
        });
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        FavouritePost favouritePost = snapshot.getValue(FavouritePost.class);

        mList.add(favouritePost);

        DatabaseReference postData = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("posts/"+favouritePost.getPostKey());

        postData.addValueEventListener(this);

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Post post = snapshot.getValue(Post.class);

        postsList.add(post);
        PostAdapter adapter = new PostAdapter(this, postsList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.w("TAG", "loadPost:onCancelled", error.toException());
    }
}
