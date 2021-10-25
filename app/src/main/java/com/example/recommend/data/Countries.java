package com.example.recommend.data;

import java.io.Serializable;

public class Countries implements Serializable {

    private String name;

    public Countries() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}