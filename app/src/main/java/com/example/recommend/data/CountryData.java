package com.example.recommend.data;

public class CountryData {

    private String cityName;
    private String cityCountry;
    private String xid;
    private String introduction;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public void setCityCountry(String cityCountry) {
        this.cityCountry = cityCountry;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public CountryData(String cityName, String cityCountry, String xid) {
        this.cityName = cityName;
        this.cityCountry = cityCountry;
        this.xid = xid;
    }


}
