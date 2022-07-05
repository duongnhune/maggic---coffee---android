package com.example.magiccoffee_v2.dto;

public class ItemNotification {
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

    public String getUid() {
        return id;
    }

    public void setUid(String id) {
        this.id = id;
    }

    public ItemNotification(String title, String content, String id) {
        this.title = title;
        this.content = content;
        this.id = id;
    }

    private String title, content, id;
}