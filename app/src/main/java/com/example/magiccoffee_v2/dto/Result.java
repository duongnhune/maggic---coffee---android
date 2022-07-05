package com.example.magiccoffee_v2.dto;

public class Result {
    public Result(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    private String Message;

}
