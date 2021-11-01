package com.example.recommend.data;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    private String author;
    private String title;
    private String content;
    private String location;
    private String date;

    public Post() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("title", title);
        result.put("content", content);
        result.put("location", location);
        result.put("date", date);

        return result;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
