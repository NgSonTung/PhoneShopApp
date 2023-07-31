package com.example.phoneshop;

public class ProductItemClass {
    String Name;
    String Price;
    String Rating;
    int BrandID;
    int Stock;
    int ProductID;
    float Sale;
    String Description;
    String Image;

    public ProductItemClass(String name, String price, String rating, int brandID, int stock, int productID, float sale, String description, String image) {
        Name = name;
        Price = price;
        Rating = rating;
        BrandID = brandID;
        Stock = stock;
        ProductID = productID;
        Sale = sale;
        Description = description;
        Image = image;
    }

    public ProductItemClass() {
        Name = "";
        Price = "";
        Rating = "";
        BrandID = 0;
        Stock = 0;
        ProductID = 0;
        Sale = 0;
        Description = "";
        Image = "";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public int getBrandID() {
        return BrandID;
    }

    public void setBrandID(int brandID) {
        BrandID = brandID;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public float getSale() {
        return Sale;
    }

    public void setSale(float sale) {
        Sale = sale;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
