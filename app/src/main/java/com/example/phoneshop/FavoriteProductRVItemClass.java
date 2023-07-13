package com.example.phoneshop;

import java.util.ArrayList;

public class FavoriteProductRVItemClass {
    int imageID;
    String title;
    String price;

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
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

    public FavoriteProductRVItemClass(int imageID, String title, String price) {
        this.imageID = imageID;
        this.title = title;
        this.price = price;
    }


    public static ArrayList<FavoriteProductRVItemClass> initData(int[] imageList, String[] titleList, String[] priceList) {

        ArrayList<FavoriteProductRVItemClass> arrList = new ArrayList<>();

        for (int i = 0; i < titleList.length; i++) {
            FavoriteProductRVItemClass item = new FavoriteProductRVItemClass(imageList[i], titleList[i], priceList[i]);
            arrList.add(item);
        }
        return arrList;
    }
}
