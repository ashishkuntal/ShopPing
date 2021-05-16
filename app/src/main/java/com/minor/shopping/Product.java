package com.minor.shopping;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    String name, desc;
    double price;
    long quantity;
    ArrayList<String> images;
    ArrayList<String> associates;
    String cityName,category;
    List<String> tag;
    public Product(){

    }

    public Product(String name, String desc, double price, long quantity, ArrayList<String> images, ArrayList<String> associates, String cityName, String category, List<String> tag) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.quantity = quantity;
        this.images = images;
        this.associates = associates;
        this.cityName = cityName;
        this.category = category;
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public ArrayList<String> getAssociates() {
        return associates;
    }

    public void setAssociates(ArrayList<String> associates) {
        this.associates = associates;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
