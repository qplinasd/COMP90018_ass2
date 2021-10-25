package com.example.recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recommend.databinding.AttractionItemBinding;

public class AttractionItemFragment extends Fragment {

    public static final String ARG_ITEM = "item";
    private AttractionItemBinding binding;
    private TextView text_attraction_item;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // bind the layout
        binding = AttractionItemBinding.inflate(inflater, container, false);
        text_attraction_item = binding.textAttractionItem;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // get the parameter passed from AttractionFragment
        Bundle args = getArguments();
        text_attraction_item.setText(args.getString(ARG_ITEM));

    }
}
