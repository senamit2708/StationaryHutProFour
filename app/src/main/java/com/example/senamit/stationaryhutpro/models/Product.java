package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Product {

    String productName;
    String productNumber;
    double productPrice;
    String imageUrl;
    String category;
    int minimumOrder;
    int productQuantity;

    public Product(String productName, String productNumber, Double productPrice, String imageUrl) {
        this.productName = productName;
        this.productNumber = productNumber;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;

    }

    //in product description i m using this..
    public Product(String productName, String productNumber, Double productPrice, String imageUrl,
                   int minimumOrder, int productQuantity) {
        this.productName = productName;
        this.productNumber = productNumber;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.minimumOrder = minimumOrder;
        this.productQuantity = productQuantity;

    }

    public Product(String productName, String productNumber, Double productPrice, String imageUrl, String category) {
        this.productName = productName;
        this.productNumber = productNumber;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    //for getting price and available quantity from productcartviewmodel
    public Product(Double productPrice, int availableQuantity) {
        this.productPrice = productPrice;
        this.productQuantity = availableQuantity;
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

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
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

    public int getMinimumOrder() {
        return minimumOrder;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setMinimumOrder(int minimumOrder) {
        this.minimumOrder = minimumOrder;
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
