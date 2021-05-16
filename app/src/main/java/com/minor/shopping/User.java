package com.minor.shopping;

import java.util.List;

public class User {
    String Name,Phone,Pincode,City,Email,AccType;
    int cartNum;
    double cartPrice;
    List<String> cart,bought,visited;

    public List<String> getVisited() {
        return visited;
    }

    public void setVisited(List<String> visited) {
        this.visited = visited;
    }

    public int getCartNum() {
        return cartNum;
    }

    public void setCartNum(int cartNum) {
        this.cartNum = cartNum;
    }

    public double getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(double cartPrice) {
        this.cartPrice = cartPrice;
    }

    public User(String name, String phone, String pincode, String city, String email, String accType, int cartNum, double cartPrice, List<String> cart, List<String> bought, List<String> visited) {
        Name = name;
        Phone = phone;
        Pincode = pincode;
        City = city;
        Email = email;
        AccType = accType;
        this.cartNum = cartNum;
        this.cartPrice = cartPrice;
        this.cart = cart;
        this.bought = bought;
        this.visited = visited;
    }

    public User(){
        //public for firestore
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAccType() {
        return AccType;
    }

    public void setAccType(String accType) {
        AccType = accType;
    }

    public List<String> getCart() {
        return cart;
    }

    public void setCart(List<String> cart) {
        this.cart = cart;
    }

    public List<String> getBought() {
        return bought;
    }

    public void setBought(List<String> bought) {
        this.bought = bought;
    }

}
