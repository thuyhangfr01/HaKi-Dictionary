package com.ute.hakidictionary.model;

public class WordSearch {
    private int id;
    private String name;
    private String content;
    private String imageUrl;
    private int count;

    public WordSearch(int id, String name, String content, String imageUrl, int count) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.imageUrl = imageUrl;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public WordSearch() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
