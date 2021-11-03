package com.example.recommend.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.signature.ObjectKey;
import com.example.recommend.GlideApp;
import com.example.recommend.R;
import com.example.recommend.data.Post;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by Haoran Lin on 2021/11/2.
 * * stuId:1019019
 */
public class PostAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Post> mList;
    private Post data;
    private int width, height;
    private WindowManager wm;

    public PostAdapter(Context mContext, List<Post> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.post_item, null);
            viewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);

//            viewHolder.btn_favourite = (ImageButton)convertView.findViewById(R.id.btn_favourite);
            viewHolder.btn_delete = (ImageButton) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });
        data = mList.get(position);
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_author.setText(data.getAuthor());
        viewHolder.tv_location.setText(data.getLocation());
        viewHolder.tv_date.setText(data.getDate());

        // get user image
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profileImages/" + data.getAuthor() + ".jpg");
        GlideApp.with(convertView.getContext())
                .load(storageRef)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .error(convertView.getResources().getDrawable(R.drawable.profile_image_test))
                .into(viewHolder.iv_img);

        return convertView;
    }

    /**
     * btn_delete interface
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }

    class ViewHolder {
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_author;
        private TextView tv_location;
        private TextView tv_date;

        //        private ImageButton btn_favourite;
        private ImageButton btn_delete;
    }
}
