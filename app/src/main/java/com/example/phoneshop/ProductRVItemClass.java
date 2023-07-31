package com.example.phoneshop;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProductRVItemClass {
    Bitmap imageID;
    String title;
    String price;
    String rating;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Bitmap getImageID() {
        return imageID;
    }

    public Bitmap setImageID(Bitmap imageID) {
        this.imageID = imageID;
        return imageID;
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

    public ProductRVItemClass(Bitmap imageID, String title, String price, String rating) {
        this.imageID = imageID;
        this.title = title;
        this.price = price;
        this.rating = rating;
    }


    public static ArrayList<ProductRVItemClass> initData(Bitmap[] imageList, String[] titleList, String[] priceList, String[] rating) {

        ArrayList<ProductRVItemClass> arrList = new ArrayList<>();

        for (int i = 0; i < titleList.length; i++) {
            ProductRVItemClass item = new ProductRVItemClass(imageList[i], titleList[i], priceList[i], rating[i]);
            arrList.add(item);
        }
        return arrList;
    }

}
