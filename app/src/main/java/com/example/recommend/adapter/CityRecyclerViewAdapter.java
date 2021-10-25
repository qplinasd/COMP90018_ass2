package com.example.recommend.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.example.recommend.R;
import com.example.recommend.data.TouristAttraction;

import java.util.List;

public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewAdapter.cityViewHolder>{

    private List<TouristAttraction> attraction_lists;
    private clickCardItem clickCardItem;


    public CityRecyclerViewAdapter(List<TouristAttraction> lists, clickCardItem clickCardItem) {
        this.attraction_lists = lists;
        this.clickCardItem = clickCardItem;
    }

    public class cityViewHolder extends RecyclerView.ViewHolder{

        private TextView text_tourist_name;
        private TextView text_city_country;
        private CardView card_city_item;

        private RelativeLayout layout;

        public cityViewHolder(View itemView) {
            super(itemView);
            // bind the components
            text_tourist_name = itemView.findViewById(R.id.text_city_attraction_item);
            text_city_country = itemView.findViewById(R.id.text_city_item_country);
            card_city_item = itemView.findViewById(R.id.card_city_item);

            layout = itemView.findViewById(R.id.layout_city_item);
        }
    }

    @NonNull
    @Override
    public CityRecyclerViewAdapter.cityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // return the recycler view holder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_recycler_item, parent, false);

        return new cityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cityViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // set the content of each card of the recycler view
        holder.text_tourist_name.setText(attraction_lists.get(position).getName());
        String city_country = attraction_lists.get(position).getCity() + ", " + attraction_lists.get(position).getCountry();
        holder.text_city_country.setText(city_country);

        String img_url = "https://source.unsplash.com/1600x900/?random";
        Glide.with(holder.card_city_item)
                .load(img_url)
                .signature(new ObjectKey(position))
                .into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                holder.layout.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }

        });

        // set the card click event
        holder.card_city_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCardItem.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attraction_lists.size();
    }

    public interface clickCardItem{
        void onClickItem(int position);
    }
}
