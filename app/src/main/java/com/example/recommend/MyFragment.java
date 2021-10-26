package com.example.recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.recommend.databinding.FragmentMyBinding;

/**
 * Created by Haoran Lin on 2021/10/26.
 */
public class MyFragment extends Fragment {

    private FragmentMyBinding binding;
    private LinearLayout favouriteFunction;
    private LinearLayout shareListFunction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my, null);

        return view;

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
