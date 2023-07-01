package com.example.phoneshop;

import java.util.ArrayList;

public class BrandRVItemClass {
    int imageID;
    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }


    public BrandRVItemClass(int imageID) {
        this.imageID = imageID;
    }


    public static ArrayList<BrandRVItemClass> initData(int[] imageList) {

        ArrayList<BrandRVItemClass> arrList = new ArrayList<>();

        for (int i = 0; i < imageList.length; i++) {
            BrandRVItemClass item = new BrandRVItemClass(imageList[i]);
            arrList.add(item);
        }
        return arrList;
    }
}
