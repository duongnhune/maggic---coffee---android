package com.example.magiccoffee_v2.dto;

import android.content.Intent;

public class ItemClick {

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public ItemClick(Intent intent, int icon, String title) {
        this.intent = intent;
        this.icon = icon;
        this.title = title;
    }


    private Intent intent;
    private int icon;
    private String title;
}
