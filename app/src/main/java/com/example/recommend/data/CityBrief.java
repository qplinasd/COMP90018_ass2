package com.example.recommend.data;

import com.google.gson.annotations.SerializedName;

public class CityBrief {

    @SerializedName("name")
    private String name;
    @SerializedName("country")
    private String country;

    public String getName() {
        return name;
    }

    public void setName(String cityName) {
        this.name = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String cityCountry) {
        this.country = cityCountry;
    }

    public CityBrief(String name, String cityCountry) {
        this.name = name;
        this.country = cityCountry;
    }

    public CityBrief(String name) {
        this.name = name;
    }

    public CityBrief() {
    }
}
