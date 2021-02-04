package com.example.Stopi.objects;

public class StoreItem {

    private String title;
    private String photoUrl;
    private int price;

    //============================

    public StoreItem() { }

    /**
     * for keeping track on gift bag items amount
     */
    public void incrementAmount() { this.price++; }

    /**
     * for keeping track on gift bag items amount
     */
    public void reduceAmount() { this.price--; }

    //============================

    public StoreItem setTitle(String title) { this.title = title; return this; }

    public StoreItem setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; return this; }

    public StoreItem setPrice(int price) { this.price = price; return this; }

    //============================

    public String getTitle() { return title; }

    public String getPhotoUrl() { return photoUrl; }

    public int getPrice() { return price; }
}
