package com.example.recommend.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("city")
    private String city;
    @SerializedName("road")
    private String road;
    @SerializedName("house_number")
    private String house_number;
    @SerializedName("state")
    private String state;
    @SerializedName("house")
    private String house;
    @SerializedName("county")
    private String county;
    @SerializedName("suburb")
    private String suburb;

    @NonNull
    @Override
    public String toString() {
        return "State: " + state + "\n" +
                "County: " + county +"\n" +
                "City: " + city + "\n" +
                "Suburb: " + suburb + "\n" +
                "Road: " + road + "\n" +
                "Number: " + house_number + "\n" +
                "House: " + house;
    }
}
