package com.example.recommend.retrofit;

import com.example.recommend.data.AttractionInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AttractionInfoRetrofit {

    @GET("places/xid/{xid}")
    Call<AttractionInfo> attractionInfo(@Path("xid") String xid, @Query("apikey") String apikey);
}
