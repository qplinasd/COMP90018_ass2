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
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.recommend.data.User;
import com.example.recommend.databinding.CountryFragmentBinding;
import com.example.recommend.databinding.FragmentMyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by Haoran Lin on 2021/10/26.
 */
public class MyFragment extends Fragment implements ChildEventListener {

    private FragmentMyBinding binding;

    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private String username;

    private ImageView profileImg;
    private TextView profileName;
    private TextView profileGender;
    private TextView profileDesc;
    private Button btnExit;

    private TextView text_my_profile;

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

        text_my_profile = binding.textMyProfile;

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

        databaseReference.orderByChild("username").equalTo(username).addChildEventListener(this);
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

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        User user = snapshot.getValue(User.class);

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

        text_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("username", user.getUsername());
                args.putString("email", user.getEmail());
                args.putString("gender", user.getGender());
                args.putString("description", user.getDescription());

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                ProfileFragment profileFragment  = new ProfileFragment();
                profileFragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,profileFragment).commit();
            }
        });
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
