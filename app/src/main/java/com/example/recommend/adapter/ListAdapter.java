package com.example.recommend.adapter;


import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.signature.ObjectKey;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.recommend.GlideApp;
import com.example.recommend.R;
import com.example.recommend.application.MyApplication;
import com.example.recommend.bean.ListBean;
import com.example.recommend.data.FavouritePost;
import com.example.recommend.data.User;
import com.example.recommend.view.CircleImageView;
import com.example.recommend.view.RoundImageView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;


public class ListAdapter extends BaseQuickAdapter<ListBean, BaseViewHolder> {


    private OnMyClickListener onMyClickListener;
    private String username;

    public void setOnMyClickListener(OnMyClickListener onMyClickListener) {
        this.onMyClickListener = onMyClickListener;
    }

    public ListAdapter() {
        super(R.layout.list_item);
    }

    public ListAdapter(String username){
        super(R.layout.list_item);
        this.username = username;
    }

    @Override
    protected void convert(@NonNull final BaseViewHolder helper, ListBean item) {
        TextView nameTv = helper.getView(R.id.name_tv);
        TextView contentTv = helper.getView(R.id.content_tv);
        TextView tv_location = helper.getView(R.id.tv_location);

        ImageView like_tv = helper.getView(R.id.like_tv);

        RoundImageView roundImageView = helper.getView(R.id.round_image_view);
        CircleImageView circleImageView = helper.getView(R.id.head_image);
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,3));

        // get user image
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("profileImages/" + item.getName()+ ".jpg");
        GlideApp.with(this.mContext)
                .load(storageRef)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .error(mContext.getResources().getDrawable(R.drawable.profile_image_test))
                .into(circleImageView);

        // set images
        if (item.getImages() == null || item.getImages().size() == 0) {
            roundImageView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            if (item.getImgBig() != -1)
                roundImageView.setImageResource(item.getImgBig());

            roundImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMyClickListener != null)
                        onMyClickListener.OnClickListener(v,helper.getAdapterPosition(),-1);
                }
            });
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            roundImageView.setVisibility(View.GONE);
            ImageAdapter imageAdapter = new ImageAdapter();
            imageAdapter.bindToRecyclerView(recyclerView);
            imageAdapter.setNewData(item.getImages());
            imageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (onMyClickListener != null)
                        onMyClickListener.OnClickListener(view,helper.getAdapterPosition(),position);
                }
            });
        }

        if (TextUtils.isEmpty(item.getTitle())) {
            contentTv.setVisibility(View.GONE);
        } else {
            contentTv.setVisibility(View.VISIBLE);
        }

        if (item.getImgBig() == -1
                && (item.getImages() == null || item.getImages().size() == 0)) {
            roundImageView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        //set post brief content
        contentTv.setText(item.getTitle());
        nameTv.setText(item.getName());
        tv_location.setText(item.getLocation());

        // user favourite post
        String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("userFavourite");

        databaseReference.orderByChild("username").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FavouritePost favouritePost = snapshot.getValue(FavouritePost.class);
                if(favouritePost.getPostKey().equals(item.getKey())){
                    like_tv.setColorFilter(Color.RED);
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

        // click like button
        like_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(like_tv.getColorFilter()==null)&&
                        like_tv.getColorFilter().equals(Color.RED)){
                    // delete the favourite post for user
                    like_tv.setColorFilter(null);
                }
                else{
                    // save the post to user favourite list
                    String key = databaseReference.push().getKey();

                    FavouritePost favouritePost = new FavouritePost(username, item.getKey());
                    Map<String, Object> favouritePostValues = favouritePost.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(key, favouritePostValues);

                    databaseReference.updateChildren(childUpdates);
                    like_tv.setColorFilter(Color.RED);
                }
            }
        });
    }

    public interface OnMyClickListener{
        void OnClickListener(View view, int position, int childrenPosition);
    }

}
