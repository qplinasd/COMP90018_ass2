package com.example.recommend.data;

import com.google.gson.annotations.SerializedName;

// Tourist Attraction info
public class TouristAttraction {

    private String xid;
    private String name;
    private String city;
    private String country;

    private String intro;
    private String location;

    public TouristAttraction(String name, String city, String country) {
        this.name = name;
        this.city = city;
        this.country = country;
    }

    public TouristAttraction(String xid, String name, String city, String country) {
        this.xid = xid;
        this.name = name;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getIntro() {
        return intro;
    }

    public String getLocation() {
        return location;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

}
