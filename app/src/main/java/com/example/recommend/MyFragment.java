package com.example.recommend;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.recommend.data.User;
import com.example.recommend.databinding.CountryFragmentBinding;
import com.example.recommend.databinding.FragmentMyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by Haoran Lin on 2021/10/26.
 */
public class MyFragment extends Fragment implements ValueEventListener {

    private FragmentMyBinding binding;

    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private String username;

    private ImageView profileImg;
    private TextView profileName;
    private TextView profileGender;
    private TextView profileDesc;
    private Button btnExit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyBinding.inflate(inflater, container, false);

        Bundle args = getArguments();
        username = args.getString("username");

        profileName = binding.profileName;
        profileGender = binding.profileGender;
        profileImg = binding.profileImage;
        profileDesc = binding.profileDescription;
        btnExit = binding.btnExitUser;

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        getUserInfo();
    }

    public void getUserInfo(){

        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("users");

        databaseReference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
            User user = dataSnapshot.getValue(User.class);

            if(user.getUsername().equals(username)){

                profileName.setText(user.getUsername());
                profileDesc.setText(user.getDescription());
                profileGender.setText(user.getGender());

                if (user.getGender().equalsIgnoreCase("female")){
                    showGenderImg("icon_female");
                }
                if (user.getGender().equalsIgnoreCase("non-binary")){
                    showGenderImg("icon_non_binary");
                }
                if (user.getGender().equalsIgnoreCase("male")){
                    showGenderImg("icon_male");
                }

                break;
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    public void showGenderImg(String imgName){

        Glide.with(this)
                .load(getResources()
                        .getIdentifier(imgName, "drawable", getContext().getPackageName()))
                .into(new CustomTarget<Drawable>(50,50) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        profileGender.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        profileGender.setCompoundDrawablesWithIntrinsicBounds(placeholder, null, null, null);
                    }
                });

    }
}
