package com.example.phoneshop;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class FavoriteProductRVItemClass {
    Bitmap imageID;
    String title;
    String price;

    public FavoriteProductRVItemClass(Bitmap imageID, String title, String price) {
        this.imageID = imageID;
        this.title = title;
        this.price = price;
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

    public static ArrayList<FavoriteProductRVItemClass> initData(Bitmap[] imageList, String[] titleList, String[] priceList) {

        ArrayList<FavoriteProductRVItemClass> arrList = new ArrayList<>();

        for (int i = 0; i < titleList.length; i++) {
            FavoriteProductRVItemClass item = new FavoriteProductRVItemClass(imageList[i], titleList[i], priceList[i]);
            arrList.add(item);
        }
        return arrList;
    }
}
