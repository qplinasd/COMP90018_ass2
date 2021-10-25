package com.example.recommend.retrofit;

import com.example.recommend.data.City;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface CityRetrofit {

    @GET("places/geoname")
    Call<City> cityInfo(@QueryMap Map<String, String> map);
}
