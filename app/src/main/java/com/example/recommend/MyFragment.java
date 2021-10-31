package com.example.recommend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.recommend.data.User;
import com.example.recommend.databinding.FragmentMyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

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
    private CustomDialog dialog;
    private TextView text_my_profile;
    private AppCompatButton btn_camera;
    private AppCompatButton btn_album;
    private AppCompatButton btn_cancel;
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private File tempFile = null;

    private TextView text_settings;
    private TextView text_password_update;
    private SwitchCompat switch_shake_undo;
    private CustomDialog dialog_settings;
    private AppCompatButton btn_setting_back;

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
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        dialog.setCancelable(false);
        btn_camera = dialog.findViewById(R.id.btn_camera);
        btn_album = dialog.findViewById(R.id.btn_album);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        text_my_profile = binding.textMyProfile;

        text_settings = binding.textSettings;
        dialog_settings = new CustomDialog(getActivity(), 0, 0, R.layout.dialog_settings,
                R.style.pop_anim_style, Gravity.BOTTOM, 0);
        dialog_settings.setCancelable(false);
        text_password_update = dialog_settings.findViewById(R.id.text_password_update);
        switch_shake_undo = dialog_settings.findViewById(R.id.switch_shake_undo);
        btn_setting_back = dialog_settings.findViewById(R.id.btn_setting_back);

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

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        text_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_settings.show();
            }
        });

        text_password_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_settings.dismiss();
                Bundle args = new Bundle();
                args.putString("username", username);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                PasswordFragment passwordFragment  = new PasswordFragment();
                passwordFragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,passwordFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        switch_shake_undo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }else{

                }
            }
        });

        btn_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_settings.dismiss();
            }
        });

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //alnum
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;

                case RESULT_REQUEST_CODE:

                    if (data != null) {

                        setImageToView(data);

                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {

            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //set cut
        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //quality
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);

        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            profileImg.setImageBitmap(bitmap);
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
}
