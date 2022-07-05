package com.example.magiccoffee_v2.dto;

import java.util.List;

public class Mail {
    private String title, docId, totalPrice, address, receivingTime, content, to, template;
    private int quantity;
    private List<ItemMail> items;

    public Mail(String title, String docId, String totalPrice, String address, String receivingTime, String content, String to, String template, int quantity, List<ItemMail> items) {
        this.title = title;
        this.docId = docId;
        this.totalPrice = totalPrice;
        this.address = address;
        this.receivingTime = receivingTime;
        this.content = content;
        this.to = to;
        this.template = template;
        this.quantity = quantity;
        this.items = items;
    }

    public Mail() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceivingTime() {
        return receivingTime;
    }

    public void setReceivingTime(String receivingTime) {
        this.receivingTime = receivingTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<ItemMail> getItems() {
        return items;
    }

    public void setItems(List<ItemMail> items) {
        this.items = items;
    }
}
