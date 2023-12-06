package com.example.phoneshop;

public class Constant {

    public static String idAddress = "10.0.2.2" + ":3001"; // ipV4Address + post server
    // 10.0.3.2 -- gen
    //10.0.2.2 -- android
    public  static  int orderId ;
    public static  int userId;

    public static String getIdAddress() {
        return idAddress;
    }

    public static void setIdAddress(String idAddress) {
        Constant.idAddress = idAddress;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        Constant.userId = userId;
    }

    public static int getOrderId() {
        return orderId;
    }

    public static void setOrderId(int orderId) {
        Constant.orderId = orderId;
    }
}
