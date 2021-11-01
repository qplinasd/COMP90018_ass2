package com.example.recommend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.example.recommend.application.MyApplication;
import com.example.recommend.data.User;
import com.example.recommend.databinding.FragmentMyBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

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
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private static final int CAMERA_REQUEST_CODE = 103;
    private File tempFile = null;
    private FirebaseStorage storage;

    private TextView text_settings;
    private TextView text_password_update;
    private SwitchCompat switch_shake_undo;
    private CustomDialog dialog_settings;
    private AppCompatButton btn_setting_back;
    private MyApplication app;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

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

        storage = FirebaseStorage.getInstance();

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

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                dialog.dismiss();

//                ImagePicker.Builder imagePicker = new ImagePicker.Builder(getActivity());
            }
        });

        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
//                new ImagePicker.Builder(getActivity()).galleryOnly().cropSquare().start();
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
                app = (MyApplication) getActivity().getApplication();
                app.setShakeUndoOn(isChecked);
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

        // get profile image
        StorageReference storageRef = storage.getReference().child("profileImages/"+username+".jpg");
        GlideApp.with(this)
                .load(storageRef)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .error(getResources().getDrawable(R.drawable.profile_image_test))
                .into(profileImg);

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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //album
                case IMAGE_REQUEST_CODE:
                    try {
                        setImageToView(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                //camera
                case CAMERA_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    try {
                        setImageToView(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case RESULT_REQUEST_CODE:

                    Log.d("TAG", "RESULT_REQUEST_CODE: "+data);
                    if (data != null) {

                        try {
                            setImageToView(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;

            }
        }
    }

    //crop
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
        intent.putExtra("outputX", 210);
        intent.putExtra("outputY", 210);
        intent.putExtra("scale", true);

        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //set the image to profile image
    private void setImageToView(Intent data) throws IOException {
        Bundle bundle = data.getExtras();
        Log.d("TAG", "setImageToView: "+bundle);
        Bitmap bitmap;
        if(bundle != null){
            bitmap = bundle.getParcelable("data");
        }else{
            bitmap =  MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
        }
        StorageReference storageRef = storage.getReference().child("profileImages/"+username+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        profileImg.setImageBitmap(bitmap);
        byte[] imgData = baos.toByteArray();

        // update profile image
        UploadTask uploadTask = storageRef.putBytes(imgData);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("TAG", "onFailure: Upload fails");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.d("TAG", "onSuccess: Upload successfully");
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
