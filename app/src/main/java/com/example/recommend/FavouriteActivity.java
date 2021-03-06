package com.example.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.recommend.adapter.PostAdapter;
import com.example.recommend.application.MyApplication;
import com.example.recommend.data.FavouritePost;
import com.example.recommend.data.Post;
import com.example.recommend.databinding.ActivityFavouriteBinding;
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

public class FavouriteActivity extends AppCompatActivity implements ChildEventListener, ValueEventListener {

    private ActivityFavouriteBinding binding;
    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private ListView mListView;
    private List<FavouritePost> mList = new ArrayList<>();
    private MyApplication app;
    private ImageButton button_return_my;
    private List<Post> postsList = new ArrayList<>();
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findView();
        getPostInfo();
    }

    public void getPostInfo() {
        app = (MyApplication) getApplication();

        // get user favourite list from database
        DatabaseReference postData = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("userFavourite");
        postData.orderByChild("username").equalTo(app.getUsername()).addChildEventListener(this);
    }

    //init View
    private void findView() {
        mListView = binding.favouriteListView;
        button_return_my = binding.buttonReturnMy;

        // click return button
        button_return_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return to previous page
                finish();
            }
        });
        adapter = new PostAdapter(this, postsList);
        // click a post
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // jump to post detail activity
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
        // ListView item delete event
        adapter.setOnItemDeleteClickListener(new PostAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int position) {
                Toast.makeText(FavouriteActivity.this,
                        "delete:" + postsList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                DatabaseReference postData = FirebaseDatabase
                        .getInstance(FirebaseURL)
                        .getReference("userFavourite");
                postsList.remove(position);
                postData.orderByChild("username").equalTo(app.getUsername())
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                FavouritePost favouritePost = snapshot.getValue(FavouritePost.class);
                                if (favouritePost.getPostKey().equals(mList.get(position).getPostKey())){
                                    // delete from database
                                    snapshot.getRef().removeValue();
                                }
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
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        FavouritePost favouritePost = snapshot.getValue(FavouritePost.class);

        mList.add(favouritePost);

        DatabaseReference postData = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("posts/" + favouritePost.getPostKey());

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
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Post post = snapshot.getValue(Post.class);
        postsList.add(post);
        mListView.setAdapter(adapter);
    }

}
