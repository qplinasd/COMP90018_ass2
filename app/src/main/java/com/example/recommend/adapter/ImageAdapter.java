package com.example.recommend.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.recommend.R;
import com.example.recommend.view.RoundImageView;

public class ImageAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public ImageAdapter() {
        super(R.layout.item_image);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        RoundImageView roundImageView = helper.getView(R.id.round_image_view);
        roundImageView.setImageResource(item);
    }
}
