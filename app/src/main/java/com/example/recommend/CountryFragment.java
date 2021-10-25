package com.example.recommend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recommend.adapter.CountriesSpinnerAdapter;
import com.example.recommend.adapter.CountryAdapter;
import com.example.recommend.data.Countries;
import com.example.recommend.data.SpinnerCountries;
import com.example.recommend.databinding.CountryFragmentBinding;
import com.example.recommend.data.CountryData;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/*
Framework
---------------------------------------------
Retrofit: https://square.github.io/retrofit/
Glide: https://github.com/bumptech/glide
---------------------------------------------
API
---------------------------------------------
OpenTripMap: https://opentripmap.io/product
CountryFlags: https://flagcdn.com/
Unsplash: https://source.unsplash.com
*/

public class CountryFragment extends Fragment{

    private CountryFragmentBinding binding;
    private String country;
    private String introduction;

    private RecyclerView countryRecycler;
    private Spinner countries_spinner;
    private CountriesSpinnerAdapter countriesSpinner;
    private int selectedPosition;

    private ImageView image_country;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = CountryFragmentBinding.inflate(inflater, container, false);

        Bundle args = getArguments();
        selectedPosition = 0;
        if(args == null){
            country = "Australia";
            introduction = "Popular Australian destinations include the coastal cities of Sydney, Brisbane and Melbourne, as well as other high-profile destinations including regional Queensland, the Gold Coast and the Great Barrier Reef, the world's largest reef. Uluru and the Australian outback are other popular locations, as is the Tasmanian wilderness.";
        }
        else{
            country = args.getString("country");
            introduction = "This is the introduction of "+country+", which will be updated with the content from database record or network API";
            selectedPosition = Integer.parseInt(args.getString("position"));
        }

        countryRecycler = binding.recyclerViewCountry;
        image_country = binding.imageCountry;

        countries_spinner = binding.countriesSpinner;

        countriesSpinner = new CountriesSpinnerAdapter(getContext(), SpinnerCountries.getCountriesList());
        countries_spinner.setAdapter(countriesSpinner);
        countries_spinner.setSelection(selectedPosition, true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set the TextView data (city and country)
        binding.countryName.setText(country);
        binding.introduction.setText(introduction);

        List<CountryData> cities_list = new ArrayList<>();
        if(selectedPosition == 0){
            cities_list.add(new CountryData("Melbourne", country, "abc"));
            cities_list.add(new CountryData("Sydney", country, "bcd"));
        }
        if(selectedPosition == 1){
            cities_list.add(new CountryData("Shanghai", country, "abc"));
            cities_list.add(new CountryData("Beijing", country, "bcd"));
        }
        if(selectedPosition == 2){
            cities_list.add(new CountryData("New York", country, "abc"));
            cities_list.add(new CountryData("Chicago", country, "bcd"));
        }
        if(selectedPosition == 3){
            cities_list.add(new CountryData("Berlin", country, "abc"));
            cities_list.add(new CountryData("Hamburg", country, "bcd"));
        }

        initCountryImage();
        initViewsAdapter(cities_list);

        clickCountrySpinner();
    }

    private void initCountryImage(){

        // initial the head image with Glide
        // this API will search for a random related image of this country
        String img_url = "https://source.unsplash.com/1600x900/?" + country;
        Glide.with(this)
                .load(img_url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image_country);
    }


    private void initViewsAdapter(List<CountryData> cities_lists){

        // set the recycler view adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        countryRecycler.setLayoutManager(layoutManager);

        // set the recycler view item click event
        CountryAdapter countryAdapter = new CountryAdapter(cities_lists, new CountryAdapter.clickCardItem() {
            @Override
            public void onClickItem(int position) {

                // pass the parameter to AttractionFragment
                Bundle args = new Bundle();
                args.putString("city", cities_lists.get(position).getCityName());
                args.putString("country", cities_lists.get(position).getCityCountry());
                args.putString("position", String.valueOf(selectedPosition));

                // navigate to AttractionFragment
                NavHostFragment.findNavController(CountryFragment.this).navigate(R.id.action_fragment_country_to_fragment_city, args);
            }
        });

        countryRecycler.setAdapter(countryAdapter);

    }

    private void clickCountrySpinner(){

        int iCurrentSelection = countries_spinner.getSelectedItemPosition();

        // click spinner to refresh
        countries_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(iCurrentSelection != position) {
                    String countrySelected = SpinnerCountries.getCountriesList().get(position).getName();
                    Bundle args = new Bundle();
                    args.putString("country", countrySelected);
                    args.putString("position", String.valueOf(position));
                    NavHostFragment.findNavController(CountryFragment.this).navigate(R.id.action_fragment_country_to_fragment_country, args);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
