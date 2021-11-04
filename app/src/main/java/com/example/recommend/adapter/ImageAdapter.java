package com.example.recommend.adapter;

import androidx.annotation.NonNull;

import com.bumptech.glide.signature.ObjectKey;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.recommend.GlideApp;
import com.example.recommend.R;
import com.example.recommend.view.RoundImageView;
import com.google.firebase.storage.StorageReference;

public class ImageAdapter extends BaseQuickAdapter<StorageReference, BaseViewHolder> {

    public ImageAdapter() {
        super(R.layout.item_image);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, StorageReference item) {
        RoundImageView roundImageView = helper.getView(R.id.round_image_view);
        // set the images on home fragment
        GlideApp.with(this.mContext)
                .load(item)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .error(mContext.getResources().getDrawable(R.drawable.background_share))
                .into(roundImageView);
    }
}
