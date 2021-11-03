package com.example.recommend.bean;

import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListBean {
    private String createTime;
    private String name;
    private List<StorageReference> images;
    private String title;
    private int ImgBig = -1;
    private String location;
    private String key;
    private String content;


    public List<StorageReference> getImages() {
        return images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImages(List<StorageReference> images) {
        this.images = images;
    }

    public int getImgBig() {
        return ImgBig;
    }

    public void setImgBig(int imgBig) {
        ImgBig = imgBig;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
