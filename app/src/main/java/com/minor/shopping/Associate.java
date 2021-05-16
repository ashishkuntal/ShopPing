package com.minor.shopping;

import java.util.ArrayList;
import java.util.List;

public class Associate {
    String Name,Phone,Pincode,City,Email,AccType;
    ArrayList<String> Products;
    List<String> categories;
    public Associate(){
        //public to firestore
    }

    public Associate(String name, String phone, String pincode, String city, String email, String accType, ArrayList<String> products, List<String> categories) {
        Name = name;
        Phone = phone;
        Pincode = pincode;
        City = city;
        Email = email;
        AccType = accType;
        Products = products;
        this.categories = categories;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
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

    public ArrayList<String> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<String> products) {
        this.Products = products;
    }
}
