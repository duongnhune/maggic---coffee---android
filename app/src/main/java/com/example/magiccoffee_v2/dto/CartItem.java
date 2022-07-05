package com.example.magiccoffee_v2.dto;

import java.io.Serializable;

public class CartItem implements Serializable {

    public CartItem() {

    }

    public CartItem(int quantity, String name, String image, int totalPrice, String cfId, String temper, String size) {
        Quantity = quantity;
        Name = name;
        Image = image;
        Price = totalPrice;
        CfId = cfId;
        Temper = temper;
        Size = size;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getCfId() {
        return CfId;
    }

    public void setCfId(String cfId) {
        CfId = cfId;
    }

    public String getTemper() {
        return Temper;
    }

    public void setTemper(String temper) {
        Temper = temper;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    private int Quantity;
    private String Name;
    private String Image;
    private int Price;
    private String CfId;
    private String Temper;


    private String Size;


}
