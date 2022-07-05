package com.example.magiccoffee_v2.dto;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    public User() {
    }

    public User(String id, String uid, String name, boolean isMember, String phoneNumber, String email, List<OrderInfo> orderInfos, List<Notification> notifications, String address) {
        this.id = id;
        Uid = uid;
        Name = name;
        IsMember = isMember;
        PhoneNumber = phoneNumber;
        Email = email;
        OrderInfo = orderInfos;
        notifications = notifications;
        this.address = address;
    }

    public User(String name, String phoneNumber, String email, List<OrderInfo> orderInfos) {
        Name = name;
        PhoneNumber = phoneNumber;
        Email = email;
        OrderInfo = orderInfos;
    }
    public User(String udi, String name, String phoneNumber, String email, List<OrderInfo> orderInfos) {
        Uid = udi;
        Name = name;
        PhoneNumber = phoneNumber;
        Email = email;
        OrderInfo = orderInfos;
    }
    public User(String udi, String name, String phoneNumber, String email) {
        Uid = udi;
        Name = name;
        PhoneNumber = phoneNumber;
        Email = email;
    }

    private String id;
    private String Uid;
    private String Name;
    private boolean IsMember;
    private String PhoneNumber;
    private String Email;
    private String address;
    private List<Notification> notifications;
    private String CreatedAt;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public List<com.example.magiccoffee_v2.dto.OrderInfo> getOrderInfo() {
        return OrderInfo;
    }

    public void setOrderInfo(List<com.example.magiccoffee_v2.dto.OrderInfo> orderInfo) {
        OrderInfo = orderInfo;
    }
    public List<OrderInfo> getOrderInfos() {
        return OrderInfo;
    }

    public void setOrderInfos(List<OrderInfo> orderInfos) {
        OrderInfo = orderInfos;
    }

    private List<OrderInfo> OrderInfo;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isMember() {
        return IsMember;
    }

    public void setMember(boolean member) {
        IsMember = member;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
