package com.example.recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.recommend.databinding.FragmentShareBinding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by Haoran Lin on 2021/10/26.
 */
public class ShareFragment extends Fragment {

    private FragmentShareBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentShareBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
