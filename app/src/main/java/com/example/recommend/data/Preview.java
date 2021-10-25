package com.example.recommend.data;

import com.google.gson.annotations.SerializedName;

public class Preview {

    @SerializedName("source")
    private String source;

    public String getSource ()
    {
        return source;
    }

    public void setSource (String source)
    {
        this.source = source;
    }

}
