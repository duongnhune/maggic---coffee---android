package com.example.magiccoffee_v2.dto;

import java.io.Serializable;

public class Member implements Serializable {

    public Member(String id, String name, boolean isMember, String phoneNumber, String email, String username, String password) {
        this.id = id;
        Name = name;
        IsMember = isMember;
        PhoneNumber = phoneNumber;
        Email = email;
        Username = username;
        Password = password;
    }

    public Member() {

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

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String Name;
    private boolean IsMember;
    private String PhoneNumber;
    private String Email;
    private String Username;
    private String Password;
}
