package com.example.recommend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.example.recommend.adapter.CountriesSpinnerAdapter;
import com.example.recommend.adapter.CountryAdapter;
import com.example.recommend.data.SpinnerCountries;
import com.example.recommend.databinding.CountryFragmentBinding;
import com.example.recommend.data.CityBrief;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
Piscum: https://picsum.photos/
CountryFlags: https://flagcdn.com/
Unsplash: https://source.unsplash.com
*/

public class CountryFragment extends Fragment{

    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String BackgroundURL = "https://picsum.photos/800/500";
    private CountryFragmentBinding binding;
    private String country;

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
        if(args==null){
            country = "Australia";
        }
        else{
            country = args.getString("country");
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
        getIntroductionFromDB(country);
        initCountryImage();
        getCitiesFromDB(country);
        clickCountrySpinner();
    }

    private void initCountryImage(){

        // initial the head image with Glide
        // this API will search for a random related image of this country
        String img_url = BackgroundURL;
        Glide.with(this)
                .load(img_url)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(image_country);
    }

    private void initViewsAdapter(List<CityBrief> cities_lists){

        // set the recycler view adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        countryRecycler.setLayoutManager(layoutManager);

        // set the recycler view item click event
        CountryAdapter countryAdapter = new CountryAdapter(cities_lists, new CountryAdapter.clickCardItem() {
            @Override
            public void onClickItem(int position) {

                // pass the parameter to CityFragment
                Bundle args = new Bundle();
                args.putString("city", cities_lists.get(position).getName());
                args.putString("country", cities_lists.get(position).getCountry());
                args.putString("position", String.valueOf(selectedPosition));

                // navigate to AttractionFragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                CityFragment cityFragment = new CityFragment();
                cityFragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,cityFragment).commit();
            }
        });

        countryRecycler.setAdapter(countryAdapter);

    }

    private void clickCountrySpinner() {

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

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CountryFragment countryFragment = new CountryFragment();
                    countryFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main,countryFragment).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getIntroductionFromDB(String country){

        // get introduction of this city from database
        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("recommendCountries/"+country+"/introduction");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String countryIntro = snapshot.getValue(String.class);
                binding.introduction.setText(countryIntro);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getCitiesFromDB(String country){
        // get popular cities of this country from database
        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("recommendCountries/"+country+"/cities");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<CityBrief> cities_list = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CityBrief cityBrief = dataSnapshot.getValue(CityBrief.class);
                    cities_list.add(cityBrief);
                }

                initViewsAdapter(cities_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
