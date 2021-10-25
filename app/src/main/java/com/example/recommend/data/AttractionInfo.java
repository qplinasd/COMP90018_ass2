package com.example.recommend.data;

import com.google.gson.annotations.SerializedName;

public class AttractionInfo {

    @SerializedName("preview")
    private Preview preview;
    @SerializedName("address")
    private Address address;
    @SerializedName("wikipedia_extracts")
    private Wikipedia wikipedia;
    @SerializedName("xid")
    private String xid;
    @SerializedName("rate")
    private String rate;

    public Preview getPreview ()
    {
        return preview;
    }

    public void setPreview (Preview preview)
    {
        this.preview = preview;
    }

    public Address getAddress ()
    {
        return address;
    }

    public void setAddress (Address address)
    {
        this.address = address;
    }


    public Wikipedia getWikipedia ()
    {
        return wikipedia;
    }

    public void setWikipedia (Wikipedia wikipedia)
    {
        this.wikipedia = wikipedia;
    }

    public String getXid ()
    {
        return xid;
    }

    public void setXid (String xid)
    {
        this.xid = xid;
    }

    public String getRate ()
    {
        return rate;
    }

    public void setRate (String rate)
    {
        this.rate = rate;
    }

}
