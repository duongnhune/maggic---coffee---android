package com.example.magiccoffee_v2.dto;

import java.io.Serializable;

public class Size implements Serializable {
    private String NSize;
    private String NotePrice;

    public Size(String NSize, String notePrice) {
        this.NSize = NSize;
        NotePrice = notePrice;
    }

    public String getNSize() {
        return NSize;
    }

    public void setNSize(String NSize) {
        this.NSize = NSize;
    }

    public String getNotePrice() {
        return NotePrice;
    }

    public void setNotePrice(String notePrice) {
        NotePrice = notePrice;
    }

    public double convertPrice(){
        double pri = Double.parseDouble(getNotePrice());
        return pri;
    }
}
