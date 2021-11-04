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

import com.example.recommend.data.CityBrief;
import com.example.recommend.R;

import java.util.List;


public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<CityBrief> cities_list;
    private clickCardItem clickCardItem;

    public CountryAdapter(List<CityBrief> cities_list, CountryAdapter.clickCardItem clickCardItem) {
        this.cities_list = cities_list;
        this.clickCardItem = clickCardItem;
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {


        private TextView cityName, cityCountry;
        private CardView cardView;
        private RelativeLayout layout;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_country_city);
            cityName = itemView.findViewById(R.id.city_name);
            cityCountry = itemView.findViewById(R.id.city_country);

            layout = itemView.findViewById(R.id.layout_country_cities);

        }
    }


    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_recycler_cities, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.cityName.setText(cities_list.get(position).getName());
        holder.cityCountry.setText(cities_list.get(position).getCountry());

        String img_url = "https://picsum.photos/800/500";
        Glide.with(holder.cardView)
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCardItem.onClickItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cities_list.size();
    }

    public interface clickCardItem{
        void onClickItem(int position);
    }

}
