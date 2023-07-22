package com.example.phoneshop;

import java.util.ArrayList;

public class OrderRVItemClass {
    int imageID;
    String title;
    String price;
    String createAt;
    int amount;
    int point;


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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }


    public OrderRVItemClass(int imageID, String title, String price, String createAt, int amount, int point) {
        this.imageID = imageID;
        this.title = title;
        this.price = price;
        this.createAt = createAt;
        this.amount = amount;
        this.point = point;
    }
    public static ArrayList<OrderRVItemClass> initData(int[] imageList, String[] titleList, String[] priceList , String[] createAtList, int[] amountList, int[] pointList) {

        ArrayList<OrderRVItemClass> arrList = new ArrayList<>();

        for (int i = 0; i < titleList.length; i++) {
            OrderRVItemClass item = new OrderRVItemClass(imageList[i], titleList[i], priceList[i],createAtList[i],amountList[i],pointList[i]);
            arrList.add(item);
        }
        return arrList;
    }
}
