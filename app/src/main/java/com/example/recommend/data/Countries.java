package com.example.recommend.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Countries implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("introduction")
    private String introduction;
    private ArrayList<CityBrief> cities;

    public Countries() {
    }

    public Countries(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public ArrayList<CityBrief> getCities() {
        return cities;
    }

    public void setCities(ArrayList<CityBrief> cities) {
        this.cities = cities;
    }
}