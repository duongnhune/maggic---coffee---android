package com.example.magiccoffee_v2.dto;

import java.io.Serializable;

public class Notification implements Serializable {

    public Notification(String title, String content) {
        this.title = title;
        this.content = content;
    }

    private String title;

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

    private String content;
}
