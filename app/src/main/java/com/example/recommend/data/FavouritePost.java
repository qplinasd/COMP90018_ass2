package com.example.recommend.data;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FavouritePost {
    private String username;
    private String postKey;

    public FavouritePost() {
    }

    public FavouritePost(String username, String postKey) {
        this.username = username;
        this.postKey = postKey;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("postKey", postKey);

        return result;
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
