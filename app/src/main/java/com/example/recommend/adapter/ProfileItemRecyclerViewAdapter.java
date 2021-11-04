package com.example.recommend.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recommend.R;
import com.example.recommend.data.User;

import java.util.ArrayList;

public class ProfileItemRecyclerViewAdapter extends RecyclerView.Adapter<ProfileItemRecyclerViewAdapter.ViewHolder> {

    private ArrayList<User> user;
    private clickProfileItem clickProfileItem;

    public ProfileItemRecyclerViewAdapter(ArrayList<User> user, clickProfileItem clickProfileItem) {
        this.user = user;
        this.clickProfileItem = clickProfileItem;
    }

    public ProfileItemRecyclerViewAdapter() {
    }

    public void setUser(ArrayList<User> user) {
        this.user = user;
    }

    public void setClickProfileItem(ProfileItemRecyclerViewAdapter.clickProfileItem clickProfileItem) {
        this.clickProfileItem = clickProfileItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // set user data for each component
        if(position == 0){
            holder.mItemNameView.setText("Username");
            holder.mContentView.setText(user.get(0).getUsername());
        }
        else if(position == 1){
            holder.mItemNameView.setText("Gender");
            holder.mContentView.setText(user.get(0).getGender());
        }
        else if(position == 2){
            holder.mItemNameView.setText("Email");
            holder.mContentView.setText(user.get(0).getEmail());
        }
        else if(position == 3){
            holder.mItemNameView.setText("Description");
            holder.mContentView.setText(user.get(0).getDescription());
        }

        // set click event fot user image changing
        holder.profile_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProfileItem.onClickProfileItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mItemNameView;
        public final TextView mContentView;
        public View profile_item;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemNameView = itemView.findViewById(R.id.item_name);
            mContentView = itemView.findViewById(R.id.item_content);
            profile_item = itemView.findViewById(R.id.profile_item);
        }
    }

    public interface clickProfileItem{
        void onClickProfileItem(int position);
    }

}