package com.example.recommend.data;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("name")
    private String name;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;

    public City(String name) {
        this.name = name;
    }

    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
