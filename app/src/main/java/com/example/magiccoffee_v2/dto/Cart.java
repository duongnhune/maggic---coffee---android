package com.example.magiccoffee_v2.dto;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CartItem> getItems() {
        return Items;
    }

    public void setItems(List<CartItem> items) {
        Items = items;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getUidNV() {
        return UidNV;
    }

    public void setUidNV(String uidNV) {
        UidNV = uidNV;
    }
    public String getPhoneNumber() {
        return PhoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
    public String getReceivingAddress() {
        return ReceivingAddress;
    }
    public void setReceivingAddress(String receivingAddress) {
        ReceivingAddress = receivingAddress;
    }

    public Cart(String uid, String id, String status, List<CartItem> items, String price, String note, String uidNV, String phoneNumber, String email, String receivingAddress, String token) {
        Uid = uid;
        this.id = id;
        Status = status;
        Items = items;
        Price = price;
        Note = note;
        UidNV = uidNV;
        PhoneNumber = phoneNumber;
        Email = email;
        ReceivingAddress = receivingAddress;
        Token = token;
    }

    public Cart(String status, List<CartItem> items, String price, String note) {
        Status = status;
        Items = items;
        Price = price;
        Note = note;
    }
    public Cart(Cart cart) {
        Uid = cart.Uid;
        this.id = cart.id;
        Status = cart.Status;
        Items = cart.Items;
        Price = cart.Price;
        Note = cart.Note;
        UidNV = cart.UidNV;
        PhoneNumber = cart.PhoneNumber;
        Email = cart.Email;
        ReceivingAddress = cart.ReceivingAddress;
        TimeOrder = cart.TimeOrder;
        ReceivingTime = cart.ReceivingTime;
        Token = cart.Token;
    }
    public Cart() {
    }
    private String Uid;
    private String id;
    private String Status;
    private List<CartItem> Items;
    private String Price;
    private String Note;
    private String UidNV;
    private String Email;
    private String ReceivingAddress;
    private String TimeOrder;
    private String ReceivingTime;
    private String PhoneNumber;
    private String Token;


    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTimeOrder() {
        return TimeOrder;
    }

    public void setTimeOrder(String timeOrder) {
        TimeOrder = timeOrder;
    }

    public String getReceivingTime() {
        return ReceivingTime;
    }

    public void setReceivingTime(String receivingTime) {
        ReceivingTime = receivingTime;
    }

    public void updateTotalPrice(){
        int totalPrice = 0;
        for(CartItem cartItem: Items){
            totalPrice += (cartItem.getPrice() * cartItem.getQuantity());
        }
        Price = totalPrice+"";
    }

    public int getTotalQuantity(){
        int total = 0;
        for(CartItem cartItem: Items) {
            total += cartItem.getQuantity();
        }
        return total;
    }


    public void updateQuantity(CartItem item){
        for(CartItem cartItem: Items) {
            if (cartItem.getCfId().equals(item.getCfId())) {
                if (cartItem.getSize().equals(item.getSize())) {
                    if (cartItem.getTemper().equals(item.getTemper())) {
                        if(item.getQuantity() == 0){
                            Items.remove(cartItem);
                        }
                        cartItem.setQuantity(item.getQuantity());
                        return;
                    }
                }
            }
        }
    }

    public void addItem(CartItem item){
        if(item.getQuantity() > 0){
            for(CartItem cartItem: Items){
                if(cartItem.getCfId().equals(item.getCfId())){
                    if(cartItem.getSize().equals(item.getSize())){
                        if(cartItem.getTemper().equals(item.getTemper())){
                            cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                            return;
                        }
                    }
                }
            }
            Items.add(item);
        }
    }

    public List<ItemMail> getItemMail(){
        List<ItemMail> items = new ArrayList<ItemMail>();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        for (CartItem item : Items){
            String price = formatter.format(item.getPrice() * item.getQuantity());
            items.add(new ItemMail(item.getName(), price, item.getQuantity()));
        }
        return items;
    }
}
