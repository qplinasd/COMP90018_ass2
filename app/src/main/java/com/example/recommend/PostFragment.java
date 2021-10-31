package com.example.recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.recommend.databinding.FragmentPostBinding;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


/**
 * Created by Haoran Lin on 2021/10/26.
 */
public class PostFragment extends Fragment implements View.OnClickListener {

    private FragmentPostBinding binding;

    private ImageButton button_return_home;
    private ImageButton button_add_image;
    private TextView text_post_title;
    private TextView text_post_content;
    private ConstraintLayout layout_location;
    private Button button_post;

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

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_return_home.setOnClickListener(this);
        button_add_image.setOnClickListener(this);
        button_post.setOnClickListener(this);
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
}
