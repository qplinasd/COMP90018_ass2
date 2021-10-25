package com.example.recommend.data;

import com.google.gson.annotations.SerializedName;

public class Wikipedia {

    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
