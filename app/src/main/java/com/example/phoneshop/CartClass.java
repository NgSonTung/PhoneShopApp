package com.example.phoneshop;

import android.graphics.Bitmap;

public class CartClass {
    Bitmap imageID;
    String title;
    String price;
    String amount;

    public CartClass(Bitmap imageID, String title, String price, String amount) {
        this.imageID = imageID;
        this.title = title;
        this.price = price;
        this.amount = amount;
    }

    public Bitmap getImageID() {
        return imageID;
    }

    public void setImageID(Bitmap imageID) {
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
