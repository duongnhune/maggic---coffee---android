package com.example.magiccoffee_v2.dto;

import java.io.Serializable;

public class OrderInfo implements Serializable {
    private String Adress;

    public OrderInfo(String adress, String status, String phone) {
        Adress = adress;
        Status = status;
        Phone = phone;
    }

    private String Status;
    private String Phone;

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
