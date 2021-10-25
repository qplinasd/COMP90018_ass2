package com.example.recommend.data;

import com.google.gson.annotations.SerializedName;

public class Attraction {

    @SerializedName("xid")
    private String xid;
    @SerializedName("name")
    private String name;

    public Attraction(String xid, String name) {
        this.xid = xid;
        this.name = name;
    }

    public String getXid ()
    {
        return xid;
    }

    public void setXid (String xid)
    {
        this.xid = xid;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }
}
