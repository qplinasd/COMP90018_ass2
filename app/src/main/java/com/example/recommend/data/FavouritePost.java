package com.example.recommend.data;

public class FavouritePost {
    private String username;
    private String postKey;

    public FavouritePost() {
    }

    public FavouritePost(String username, String postKey) {
        this.username = username;
        this.postKey = postKey;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
