package com.example.magiccoffee_v2.dto;

import java.io.Serializable;

public class Temper implements Serializable {
    private String Tpn;
    private String NotePrice;

    public String getTpn() {
        return Tpn;
    }

    public void setTpn(String tpn) {
        Tpn = tpn;
    }

    public String getNotePrice() {
        return NotePrice;
    }

    public void setNotePrice(String notePrice) {
        NotePrice = notePrice;
    }

    public Temper(String tpn, String notePrice) {
        Tpn = tpn;
        NotePrice = notePrice;
    }

    public double convertPrice(){
        double pri = Double.parseDouble(getNotePrice());
        return pri;
    }
}
