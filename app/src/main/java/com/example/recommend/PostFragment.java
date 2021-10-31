package com.example.recommend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recommend.databinding.FragmentPostBinding;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class PostFragment extends Fragment implements View.OnClickListener {

    private FragmentPostBinding binding;

    private ImageButton button_return_home;
    private ImageButton button_add_image;
    private TextView text_post_title;
    private TextView text_post_content;
    private ConstraintLayout layout_location;
    private Button button_post;

    private String post_title;
    private String post_content;

    private SensorManager sensorManager;
    private Vibrator vibrator;
    private Sensor sensor;
    private boolean hasShaked = false;
    private MyApplication app;

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

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        post_title = "";
        post_content = "";

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_return_home.setOnClickListener(this);
        button_add_image.setOnClickListener(this);
        button_post.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_return_home:
                // return to previous page
            case R.id.button_add_image:
                // add more images
            case R.id.button_post:
                // submit the post
        }
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
