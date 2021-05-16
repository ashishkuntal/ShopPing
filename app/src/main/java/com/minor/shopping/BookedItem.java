package com.minor.shopping;

public class BookedItem {
    CartItem cartItem;
    String user;

    public BookedItem(){}
    public BookedItem(CartItem cartItem,String user) {
        this.cartItem = cartItem;
        this.user = user;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
