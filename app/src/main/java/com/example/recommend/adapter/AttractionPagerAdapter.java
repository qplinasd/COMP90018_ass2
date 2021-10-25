package com.example.recommend.adapter;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.recommend.AttractionItemFragment;
import com.example.recommend.data.TouristAttraction;

public class AttractionPagerAdapter extends FragmentStateAdapter {

    private TouristAttraction attraction;
    private final int item_count = 2;

    public AttractionPagerAdapter(@NonNull Fragment fragment, TouristAttraction attraction) {
        super(fragment);
        this.attraction = attraction;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        // return a NEW fragment instance in createFragment(int)
        Fragment fragment = new AttractionItemFragment();
        Bundle args = new Bundle();

        // pass the parameter to AttractionItemFragment
        if(position == 0){
            // pass the introduction
            args.putString(AttractionItemFragment.ARG_ITEM, attraction.getIntro());
        }
        if(position == 1){
            // pass the location
            args.putString(AttractionItemFragment.ARG_ITEM, attraction.getLocation());
        }

        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public int getItemCount() {
        return item_count;
    }
}
