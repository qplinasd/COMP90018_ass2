package com.example.recommend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.recommend.adapter.PostAdapter;
import com.example.recommend.application.MyApplication;
import com.example.recommend.data.Post;
import com.example.recommend.data.User;
import com.example.recommend.databinding.ActivityMainBinding;
import com.example.recommend.databinding.ActivitySharelistBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class ShareListActivity extends AppCompatActivity implements ChildEventListener {
    private ActivitySharelistBinding binding;
    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private ListView mListView;
    private List<Post> mList = new ArrayList<>();
    private MyApplication app;
    private ImageButton button_return_my;
    private List<String> postKey = new ArrayList<>();
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySharelistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findView();
        getPostInfo();
    }

    public void getPostInfo(){
        app = (MyApplication) getApplication();

        // get user shared post list info
        DatabaseReference postData = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("posts");
        postData.orderByChild("author").equalTo(app.getUsername()).addChildEventListener(this);
    }

    // init View
    private void findView() {
        mListView = binding.shareListView;
        button_return_my = binding.buttonReturnMy;
        adapter = new PostAdapter(this, mList);
        button_return_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // click one post
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // jump to post detail
                Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("location", mList.get(position).getLocation());
                intent.putExtra("date", mList.get(position).getDate());
                intent.putExtra("author", mList.get(position).getAuthor());
                intent.putExtra("content", mList.get(position).getContent());
                intent.putExtra("key", postKey.get(position));

                startActivity(intent);
            }
        });
        // ListView item delete event
        adapter.setOnItemDeleteClickListener(new PostAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int position) {
                clickToDelete(position);
            }
        });
    }

    private void clickToDelete(int position){
        new AlertDialog.Builder(this).setMessage("Are you sure you want to delete it?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete a post
                        mList.remove(position);
                        Toast.makeText(ShareListActivity.this, "delete item:" + position, Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();

                        DatabaseReference postData = FirebaseDatabase
                                .getInstance(FirebaseURL)
                                .getReference("posts");
                        postData.orderByKey().equalTo(postKey.get(position)).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                snapshot.getRef().removeValue();
                                // delete user favourite for this post
                                deleteUserFavourite(snapshot);
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
                    }
                }).setNegativeButton("Cancel", null).show();

    }

    private void addPost(Post post) {
        mList.add(post);
    }

    private void deleteUserFavourite(DataSnapshot snapshot){

        DatabaseReference postData = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("userFavourite");

        postData.orderByChild("postKey").equalTo(snapshot.getKey()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                snapshot.getRef().removeValue();
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

    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Post post = snapshot.getValue(Post.class);
        addPost(post);

        // set list view adapter
        mListView.setAdapter(adapter);
        postKey.add(snapshot.getKey());
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
}
