package com.example.recommend;

import android.graphics.drawable.Drawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.example.recommend.adapter.CityRecyclerViewAdapter;
import com.example.recommend.data.Attraction;
import com.example.recommend.data.CountryCode;
import com.example.recommend.data.TouristAttraction;
import com.example.recommend.databinding.CityFragmentBinding;
import com.example.recommend.data.City;
import com.example.recommend.retrofit.AttractionListRetrofit;
import com.example.recommend.retrofit.CityRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
*/

public class CityFragment extends Fragment{

    private static final String APIKEY = "5ae2e3f221c38a28845f05b60618e795fe7e56472fbf4e3de4e72e30";
    private static final String FlagURL = "https://flagcdn.com/64x48/";
    private static final String BackgroundURL = "https://picsum.photos/800/500";
    private static final String RetrofitURL = "https://api.opentripmap.com/0.1/en/";

    private CityFragmentBinding binding;
    private String city;
    private String country;
    private int selectedPosition;

    private RecyclerView recycler_view_city;
    private ImageButton button_return_city;
    private ImageView image_head_city;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = CityFragmentBinding.inflate(inflater, container, false);

        // save the city and country value
        Bundle args = getArguments();
        city = args.getString("city");
        country = args.getString("country");
        selectedPosition = Integer.parseInt(args.getString("position"));

        // bind the components
        recycler_view_city = binding.recyclerViewCity;
        button_return_city = binding.buttonReturnCity;
        image_head_city = binding.imageHeadCity;

        retrofitGetCityInfo();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set the TextView data (city and country)
        binding.textCity.setText(city);
        binding.textCityCountry.setText(country);

        //Return to country page
        button_return_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("country", country);
                args.putString("position", String.valueOf(selectedPosition));

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                CountryFragment countryFragment = new CountryFragment();
                countryFragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,countryFragment).commit();
            }

        });

        initCityImage();
        initCountryFlag();

    }

    private void initViewsAdapter(List<TouristAttraction> attraction_lists){

        // set the recycler view adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view_city.setLayoutManager(layoutManager);

        // set the recycler view item click event
        CityRecyclerViewAdapter cityAdapter = new CityRecyclerViewAdapter(attraction_lists, new CityRecyclerViewAdapter.clickCardItem() {
            @Override
            public void onClickItem(int position) {

                // pass the parameter to AttractionFragment
                Bundle args = new Bundle();
                args.putString("name", attraction_lists.get(position).getName());
                args.putString("city", attraction_lists.get(position).getCity());
                args.putString("country", attraction_lists.get(position).getCountry());
                args.putString("xid", attraction_lists.get(position).getXid());
                args.putString("position", String.valueOf(selectedPosition));

                // navigate to AttractionFragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                AttractionFragment fragment = new AttractionFragment();
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,fragment).commit();
            }
        });

        recycler_view_city.setAdapter(cityAdapter);
    }

    private void initCountryFlag(){

        // set the country flag with Glide
        String country_code = new CountryCode().getCode(country).toLowerCase(Locale.ROOT);
        String flag_url = FlagURL+country_code+".png";

        Glide.with(this)
                .load(flag_url)
                .into(new CustomTarget<Drawable>(50,50) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        binding.textCityCountry.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        binding.textCityCountry.setCompoundDrawablesWithIntrinsicBounds(placeholder, null, null, null);
                    }
                });
    }

    private void initCityImage(){

        // initial the head image with Glide
        // this API will search for a random related image of this city
        String img_url = BackgroundURL;
        Glide.with(this)
                .load(img_url)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(image_head_city);
    }

    private void retrofitGetCityInfo(){

        // API base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // get the city information with retrofit
        Map<String, String> map = new HashMap<>();
        map.put("name", city);
        map.put("apikey", APIKEY);

        CityRetrofit request = retrofit.create(CityRetrofit.class);
        Call<City> call = request.cityInfo(map);
        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                City city_info = new City(city);
                city_info.setLatitude(response.body().getLatitude());
                city_info.setLongitude(response.body().getLongitude());

                // git the tourist attractions list with retrofit
                map.clear();
                map.put("radius", "50000");
                map.put("lon", String.valueOf(city_info.getLongitude()));
                map.put("lat", String.valueOf(city_info.getLatitude()));
                map.put("src_attr", "wikidata");
                map.put("kinds", "interesting_places");
                map.put("rate", "3h");
                map.put("format", "json");
                map.put("limit", "15");
                map.put("apikey", APIKEY);

                AttractionListRetrofit request_list = retrofit.create(AttractionListRetrofit.class);
                Call<List<Attraction>> call_list = request_list.attractionList(map);
                call_list.enqueue(new Callback<List<Attraction>>() {
                    @Override
                    public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {

                        List<TouristAttraction> attraction_lists = new ArrayList<>();
                        for (int i=0;i<response.body().size();i++){
                            TouristAttraction attraction = new TouristAttraction(response.body().get(i).getXid(),
                                    response.body().get(i).getName(), city, country);

                            attraction_lists.add(attraction);
                        }

                        for(int i=0 ; i<attraction_lists.size()-1;i ++ ) {
                            for(int j=attraction_lists.size()- 1;j>i;j--) {
                                if  (attraction_lists.get(j).getName().equals(attraction_lists.get(i).getName()))  {
                                    attraction_lists.remove(j);
                                }
                            }
                        }

                        // set the view adapter to show the attractions
                        if (attraction_lists.size() > 5){
                            initViewsAdapter(attraction_lists.subList(0, 5));
                        }
                        else{
                            initViewsAdapter(attraction_lists);
                        }


                    }

                    @Override
                    public void onFailure(Call<List<Attraction>> call, Throwable t) {
                        Log.e("Error", "onFailure: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });

    }

}