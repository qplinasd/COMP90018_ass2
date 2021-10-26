package com.example.recommend;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.recommend.adapter.AttractionPagerAdapter;
import com.example.recommend.data.AttractionInfo;
import com.example.recommend.data.CountryCode;
import com.example.recommend.data.TouristAttraction;
import com.example.recommend.databinding.AttractionFragmentBinding;
import com.example.recommend.retrofit.AttractionInfoRetrofit;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttractionFragment extends Fragment {

    private static final String APIKEY = "5ae2e3f221c38a28845f05b60618e795fe7e56472fbf4e3de4e72e30";
    private static final String FlagURL = "https://flagcdn.com/64x48/";
    private static final String RetrofitURL = "https://api.opentripmap.com/0.1/en/";

    private AttractionFragmentBinding binding;
    private TextView text_attraction_name;
    private TextView text_attraction_country;
    private ImageButton button_return;

    private TouristAttraction attraction;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = AttractionFragmentBinding.inflate(inflater, container, false);

        text_attraction_name = binding.textAttractionName;
        text_attraction_country = binding.textAttractionCountry;
        button_return = binding.buttonReturn;
        viewPager = binding.viewpagerAttraction;
        tabLayout = binding.tabAttraction;

        // save the data passed from CityFragment
        Bundle args = getArguments();
        attraction = new TouristAttraction(args.getString("xid"),
                args.getString("name"),
                args.getString("city"),
                args.getString("country"));

        return binding.getRoot();
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //return to city page
        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = getArguments();
                args.putString("city", attraction.getCity());
                args.putString("country", attraction.getCountry());
                args.putString("position", String.valueOf(args.getString("position")));

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                CityFragment fragment = new CityFragment();
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main,fragment).commit();
            }
        });

        text_attraction_name.setText(attraction.getName());
        text_attraction_country.setText(attraction.getCity() + ", " + attraction.getCountry());

        // set the country flag
        initCountryFlag();

        // set the tab layout, binding with ViewPager2
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // set the intro and location using retrofit
        retrofitAttractionInfo(this);


    }

    private void initCountryFlag(){
        // set the country flag with Glide
        String country_code = new CountryCode().getCode(attraction.getCountry()).toLowerCase(Locale.ROOT);
        String flag_url = FlagURL+country_code+".png";

        Glide.with(this)
                .load(flag_url)
                .into(new CustomTarget<Drawable>(50,50) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        text_attraction_country.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        text_attraction_country.setCompoundDrawablesWithIntrinsicBounds(placeholder, null, null, null);
                    }
                });
    }

    private void retrofitAttractionInfo(Fragment fragment){

        // API base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // get the attraction information with retrofit
        AttractionInfoRetrofit request = retrofit.create(AttractionInfoRetrofit.class);
        Call<AttractionInfo> call = request.attractionInfo(attraction.getXid(), APIKEY);
        call.enqueue(new Callback<AttractionInfo>() {
            @Override
            public void onResponse(Call<AttractionInfo> call, Response<AttractionInfo> response) {

                // get the wikipedia intro and location of the attraction
                attraction.setIntro(response.body().getWikipedia().getText());
                attraction.setLocation(response.body().getAddress().toString());

                // set the ViewPager adapter
                AttractionPagerAdapter adapter= new AttractionPagerAdapter(fragment, attraction);
                viewPager.setAdapter(adapter);

                // set the attraction image
                String img_url = response.body().getPreview().getSource();
                ImageView img_head = binding.imageHeadAttraction;
                Glide.with(fragment)
                        .load(img_url)
                        .into(img_head);
            }

            @Override
            public void onFailure(Call<AttractionInfo> call, Throwable t) {

            }
        });
    }
}