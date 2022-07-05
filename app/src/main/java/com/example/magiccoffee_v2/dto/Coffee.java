package com.example.magiccoffee_v2.dto;


import java.io.Serializable;
import java.util.List;

public class Coffee implements Serializable {

    public Coffee(String id, String imageLink, String name, String type, int price, List<Size> size, List<Temper> temper) {
        this.id = id;
        ImageLink = imageLink;
        Name = name;
        Type = type;
        Price = price;
        Size = size;
        Temper = temper;
    }
    private String id;
    private String ImageLink;
    private String Name;
    private String Type;
    private int Price;

    public List<Size> getSize() {
        return Size;
    }

    public void setSize(List<Size> size) {
        Size = size;
    }

    public List<Temper> getTemper() {
        return Temper;
    }

    public void setTemper(List<Temper> temper) {
        Temper = temper;
    }

    private List<Size> Size;
    private List<Temper> Temper;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public Coffee(String name,String Type, int price) {
        this.Name = name;
        this.Price = price;
        this.Type = Type;
    }
}
