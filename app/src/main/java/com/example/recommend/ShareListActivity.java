package com.example.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Haoran Lin on 2021/10/26.
 */
public class ShareListActivity extends AppCompatActivity implements ChildEventListener {
    private ActivitySharelistBinding binding;
    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private ListView mListView;
    private List<Post> mList = new ArrayList<>();
    //标题
    private List<String> mListTitle = new ArrayList<>();
    //地址
    private List<String> mListLocation = new ArrayList<>();
    private MyApplication app;
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

        DatabaseReference postData = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("posts");
        postData.orderByChild("author").equalTo(app.getUsername()).addChildEventListener(this);


    }

    //初始化View
    private void findView() {
        mListView = binding.shareListView;

        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                intent.putExtra("title", mListTitle.get(position));
                intent.putExtra("location", mListLocation.get(position));
                startActivity(intent);
            }
        });
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonresult = jsonObject.getJSONObject("result");
            JSONArray jsonList = jsonresult.getJSONArray("list");
            for (int i = 0; i < jsonList.length(); i++) {

//                mList.add(data);
//
//                mListTitle.add(titlr);
//                mListLocation.add(url);
            }
            PostAdapter adapter = new PostAdapter(this, mList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Post posts = snapshot.getValue(Post.class);
        Log.i("postdata",posts.toString());
//        parsingJson(posts.toString());
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
