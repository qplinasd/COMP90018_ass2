package com.example.recommend.adapter;


import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.recommend.R;
import com.example.recommend.bean.ListBean;
import com.example.recommend.view.CircleImageView;
import com.example.recommend.view.RoundImageView;


public class ListAdapter extends BaseQuickAdapter<ListBean, BaseViewHolder> {


    private OnMyClickListener onMyClickListener;

    public void setOnMyClickListener(OnMyClickListener onMyClickListener) {
        this.onMyClickListener = onMyClickListener;
    }

    public ListAdapter() {
        super(R.layout.list_item);
    }



    /*@NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view);
        return holder;
    }*/

    @Override
    protected void convert(@NonNull final BaseViewHolder helper, ListBean item) {
        TextView nameTv = helper.getView(R.id.name_tv);
        TextView contentTv = helper.getView(R.id.content_tv);
        TextView likeTv = helper.getView(R.id.like_tv);
        TextView commentTv = helper.getView(R.id.comment_tv);

        RoundImageView roundImageView = helper.getView(R.id.round_image_view);
        CircleImageView circleImageView = helper.getView(R.id.head_image);
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,3));


        circleImageView.setImageResource(item.getHeadImg());
        if (item.getImages() == null || item.getImages().size() == 0) {
            roundImageView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            if (item.getImgBig() != -1)
                roundImageView.setImageResource(item.getImgBig());

            roundImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMyClickListener != null)
                        onMyClickListener.OnClickListener(v,helper.getAdapterPosition(),-1);
                }
            });
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            roundImageView.setVisibility(View.GONE);
            ImageAdapter imageAdapter = new ImageAdapter();
            imageAdapter.bindToRecyclerView(recyclerView);
            imageAdapter.setNewData(item.getImages());
            imageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (onMyClickListener != null)
                        onMyClickListener.OnClickListener(view,helper.getAdapterPosition(),position);
                }
            });
        }

        if (TextUtils.isEmpty(item.getContent())) {
            contentTv.setVisibility(View.GONE);
        } else {
            contentTv.setVisibility(View.VISIBLE);
        }


        if (item.getImgBig() == -1
                && (item.getImages() == null || item.getImages().size() == 0)) {
            roundImageView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        contentTv.setText(item.getContent());
        nameTv.setText(item.getName());
        likeTv.setText("" + item.getLike());
        commentTv.setText("" + item.getComment());

    }

    public interface OnMyClickListener{
        void OnClickListener(View view, int position, int childrenPosition);
    }

}
