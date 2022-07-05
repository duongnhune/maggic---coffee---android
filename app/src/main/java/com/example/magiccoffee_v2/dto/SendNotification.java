package com.example.magiccoffee_v2.dto;

public class SendNotification {
    public ItemNotification getData() {
        return data;
    }

    public void setData(ItemNotification data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    private ItemNotification data;

    public SendNotification(ItemNotification data, String to) {
        this.data = data;
        this.to = to;
    }

    private String to;

}
