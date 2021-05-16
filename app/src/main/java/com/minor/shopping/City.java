package com.minor.shopping;

import java.util.ArrayList;

public class City {
    String name;
    ArrayList<String> associates;
    ArrayList<String> products;
    public City(){

    }
    public City(String name, ArrayList<String> associates, ArrayList<String> products) {
        this.name = name;
        this.associates = associates;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAssociates() {
        return associates;
    }

    public void setAssociates(ArrayList<String> associates) {
        this.associates = associates;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }
}
