package com.example.recommend.data;

import java.util.ArrayList;
import java.util.List;

public class SpinnerCountries {

    public static List<Countries> getCountriesList() {
        List<Countries> countryList = new ArrayList<>();

        Countries Australia = new Countries("Australia");
        countryList.add(Australia);

        Countries China = new Countries("China");
        countryList.add(China);

        Countries United_States = new Countries("United States");
        countryList.add(United_States);

        Countries Germany = new Countries("Germany");
        countryList.add(Germany);


        return countryList;
    }

}