package com.example.phoneshop;

public class ResponseProductClass {
    private int Code;
    private String Msg;
    private ProductItemClass Data;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public ProductItemClass getData() {
        return Data;
    }

    public void setData(ProductItemClass data) {
        Data = data;
    }
}
