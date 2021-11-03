package com.example.recommend.bean;

import java.util.List;

public class ListBean {
    private String createTime;
    private String name;
    private int like;
    private int comment;
    private List<Integer> images;
    private String content;
    private int headImg;
    private int ImgBig = -1;


    public List<Integer> getImages() {
        return images;
    }

    public void setImages(List<Integer> images) {
        this.images = images;
    }

    public int getImgBig() {
        return ImgBig;
    }

    public void setImgBig(int imgBig) {
        ImgBig = imgBig;
    }

    public int getHeadImg() {
        return headImg;
    }

    public void setHeadImg(int headImg) {
        this.headImg = headImg;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
