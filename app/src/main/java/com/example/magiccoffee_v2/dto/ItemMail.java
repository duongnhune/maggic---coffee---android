package com.example.magiccoffee_v2.dto;

public class ItemMail {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getQuantily() {
        return quantily;
    }

    public void setQuantily(int quantily) {
        this.quantily = quantily;
    }

    private String name, total;
    private int quantily;

    public ItemMail(String name, String total, int quantily) {
        this.name = name;
        this.total = total;
        this.quantily = quantily;
    }

}
