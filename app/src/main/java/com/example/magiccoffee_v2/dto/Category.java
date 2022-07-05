package com.example.magiccoffee_v2.dto;


public class Category {
    public Category(String name, String type) {
        Name = name;
        Type = type;
    }

    private String Name;
    private String Type;

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
}

