package com.example.recommend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recommend.application.MyApplication;
import com.example.recommend.data.Post;
import com.example.recommend.databinding.FragmentPostBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class PostFragment extends Fragment implements View.OnClickListener {

    private FragmentPostBinding binding;
    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private String username;
    private FirebaseStorage storage;

    private ImageButton button_return_home;
    private ImageView button_add_image;
    private LinearLayout layout_add_image;
    private TextView text_post_title;
    private TextView text_post_content;
    private ConstraintLayout layout_location;
    private Button button_post;
    private TextView text_current_location;

    private String post_title;
    private String post_content;
    private String key;

    private SensorManager sensorManager;
    private Vibrator vibrator;
    private Sensor sensor;
    private boolean hasShaked = false;
    private MyApplication app;

    private EasyImage easyImage;
    private ArrayList<Uri> imageUriList;
    private ArrayList<ImageView> imageList;

    // Shake to undo using sensor
    private SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            float values[] = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            int mediumValue = 9;
            app = (MyApplication) getActivity().getApplication();
            if ((Math.abs(x) > mediumValue || /*Math.abs(y) > mediumValue || */Math
                    .abs(z) > mediumValue) && !hasShaked && app.getShakeUndoOn()) {
                if (!(post_content.equals(""))) {
                    vibrator.vibrate(200);
                    showDialog();
                    hasShaked = true;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentPostBinding.inflate(inflater, container, false);
        button_return_home = binding.buttonReturnHome;
        button_add_image = binding.buttonAddImage;
        text_post_title = binding.textPostTitle;
        text_post_content = binding.textPostContent;
        layout_location = binding.layoutLocation;
        button_post = binding.buttonPost;
        layout_add_image = binding.layoutAddImage;
        text_current_location = binding.textCurrentLocation;

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        post_title = "";
        post_content = "";
        //TODO: get username from home page
        username = "test";
        key = "";
        imageUriList = new ArrayList<>();
        imageList = new ArrayList<>();
        imageList.add(button_add_image);
        storage = FirebaseStorage.getInstance();

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_return_home.setOnClickListener(this);
        button_add_image.setOnClickListener(this);
        button_post.setOnClickListener(this);
        layout_location.setOnClickListener(this);

        // get current location
        app = (MyApplication) getActivity().getApplication();
        if(app.getCurrentLocation() != null){
            text_current_location.setText(app.getCurrentLocation().getSubAdminArea() + ", " +
                    app.getCurrentLocation().getCountryName());

            app.setCurrentLocation(null);
        }

        // get post title
        text_post_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //get input value
                post_title = text_post_title.getText().toString();
            }
        });

        // get post content
        text_post_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //get input value
                post_content = text_post_content.getText().toString();
            }
        });

        easyImage = new EasyImage.Builder(getContext())
                .allowMultiple(true)
                .build();

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.layout_location:
                // click location
                MapsFragment mapsFragment  = new MapsFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,mapsFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.button_return_home:
                // return to previous page
                //TODO: change to pop stack when return
                HomeFragment homeFragment  = new HomeFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,homeFragment).commit();
                break;
            case R.id.button_post:
                // submit the post
                DatabaseReference databaseReference = FirebaseDatabase
                        .getInstance(FirebaseURL)
                        .getReference("posts");

                // save post to database
                key = databaseReference.push().getKey();
                Post newPost = new Post();
                newPost.setAuthor(username);
                newPost.setContent(post_content);
                newPost.setTitle(post_title);
                newPost.setLocation(text_current_location.getText().toString());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                newPost.setDate(format.format(Calendar.getInstance().getTime()));

                Map<String, Object> postValues = newPost.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, postValues);

                databaseReference.updateChildren(childUpdates);

                // change image uri to bitmap
                for(int i=0;i<imageUriList.size();i++){
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUriList.get(i));
//                        imageList.add(bitmap);
                        //save post images to firebase storage
                        StorageReference storageRef = storage.getReference().child("posts/"+key+"/"+i+".jpg");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                        byte[] imgData = baos.toByteArray();
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // return to home page
                    new androidx.appcompat.app.AlertDialog.Builder(getContext()).setMessage("Post successfully")
                            .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO: pass username
//                                    Intent intent = new Intent(getContext(), MainActivity.class);
//                                    intent.putExtra("username", username);
//                                    startActivity(intent);
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                }
                            })
                            .show();
                }


                break;
            default:
                // add more images
                easyImage.openChooser(this);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] mediaFiles, MediaSource mediaSource) {
                if (mediaFiles.length == 1){
                    Uri contentUri = Uri.fromFile(mediaFiles[0].getFile());
//                    button_add_image.setImageURI(contentUri);

                    imageUriList.add(contentUri);

                    for(int i=0;i<imageUriList.size();i++){
                        imageList.get(i).setImageURI(imageUriList.get(i));
                    }

                    // add more images for this post
                    addMoreImages();
                }
            }
        });
    }

    private void addMoreImages(){
        // add a new image view
        ImageView newImage = new ImageView(getContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(button_add_image.getWidth(), button_add_image.getHeight());
        params.setMargins(20, 0, 0, 0);
        newImage.setLayoutParams(params);
        newImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        newImage.setImageResource(R.drawable.post_add_image);
        newImage.setOnClickListener(this);
        layout_add_image.addView(newImage);
        imageList.add(newImage);
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage("Undo Typing");
        dialog.setPositiveButton("Undo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text_post_content.setText("");
                dialog.dismiss();
                hasShaked = false;
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                hasShaked = false;
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(listener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
    }
}
