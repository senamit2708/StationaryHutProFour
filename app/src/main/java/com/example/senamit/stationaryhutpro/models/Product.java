package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Product {

    String productName;
    String productNumber;
    int productPrice;
    String imageUrl;
    String category;

    public Product(String productName, String productNumber, int productPrice, String imageUrl) {
        this.productName = productName;
        this.productNumber = productNumber;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;

    }

    public Product(String productName, String productNumber, int productPrice, String imageUrl, String category) {
        this.productName = productName;
        this.productNumber = productNumber;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("productNumber", productNumber);
        result.put("productName", productName);
        result.put("productPrice", productPrice);
        result.put("imageUrl", imageUrl);

        return result;
    }


}
