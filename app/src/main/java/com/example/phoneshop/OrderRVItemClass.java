package com.example.phoneshop;

import java.util.ArrayList;

public class OrderRVItemClass {
    String title;
    String price;
    String createAt;
    int amount;
    String point;




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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }


    public OrderRVItemClass(String title, String price, String createAt, int amount, String point) {
        this.title = title;
        this.price = price;
        this.createAt = createAt;
        this.amount = amount;
        this.point = point;
    }
    public static ArrayList<OrderRVItemClass> initData(int[] imageList, String[] titleList, String[] priceList , String[] createAtList, int[] amountList, String[] pointList) {

        ArrayList<OrderRVItemClass> arrList = new ArrayList<>();

        for (int i = 0; i < titleList.length; i++) {
            OrderRVItemClass item = new OrderRVItemClass(titleList[i], priceList[i],createAtList[i],amountList[i],pointList[i]);
            arrList.add(item);
        }
        return arrList;
    }
}
