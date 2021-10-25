package com.example.recommend;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import com.example.recommend.adapter.CountriesSpinnerAdapter;
import com.example.recommend.data.SpinnerCountries;

public class RecommendActivity extends AppCompatActivity {

    private Spinner countries_spinner;
    private CountriesSpinnerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}