package com.minor.shopping;

public class CartItem {
    int quant;
    Product product;
    public CartItem(){

    }
    public CartItem(int quant, Product product) {
        this.quant = quant;
        this.product = product;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
