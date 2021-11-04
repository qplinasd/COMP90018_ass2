package com.example.recommend.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recommend.R;
import com.example.recommend.data.Countries;

import org.w3c.dom.Text;

import java.util.List;

public class CountriesSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<Countries> countriesList;

    public CountriesSpinnerAdapter(Context context, List<Countries> countriesList) {
        this.context = context;
        this.countriesList = countriesList;
    }


    @Override
    public int getCount() {
        return countriesList != null ? countriesList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_countries, viewGroup, false);

        // set the spinner content
        TextView txtName = rootView.findViewById(R.id.name_of_countries);
        txtName.setText(countriesList.get(i).getName());

        return rootView;
    }
}
