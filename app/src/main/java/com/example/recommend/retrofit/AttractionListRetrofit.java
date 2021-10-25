package com.example.recommend.retrofit;

import com.example.recommend.data.Attraction;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface AttractionListRetrofit {

    @GET("places/radius")
    Call<List<Attraction>> attractionList(@QueryMap Map<String, String> map);
}
