package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserCart {

    private String productNumber;
    private String date;
    private int productPrice;
    private String productName;
    private String imageUrl;
    private int quantity;
    private String orderStatus;
    private String cartProductKey;
    private String totalPrice;
    private String orderNumber;
    private String paymentMode;
    private int minimumOrder;
    private int availableQuantity;

    public UserCart(String productNumber) {
        this.productNumber = productNumber;
    }

    public UserCart(String productNumber, int productPrice) {
        this.productNumber = productNumber;
        this.productPrice = productPrice;
    }

    //this constructor called from product description to load cart product
    public UserCart(String productNumber, String date, int productPrice, String productName, String imageUrl) {
        this.productNumber = productNumber;
        this.date = date;
        this.productPrice = productPrice;
        this.productName = productName;
        this.imageUrl = imageUrl;
//        this.minimumOrder = minimumOrder;
    }

    public UserCart(String productNumber, String date, int productPrice, String productName, String imageUrl, int quantity ) {
        this.productNumber = productNumber;
        this.date = date;
        this.productPrice = productPrice;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }



    //for setting the boolean..for out of stock in cartproduct class
    public UserCart(String productNumber, String date, int productPrice, String productName,
                    String imageUrl, int quantity, int minimumOrder, int availableQuantity ) {
        this.productNumber = productNumber;
        this.date = date;
        this.productPrice = productPrice;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.minimumOrder= minimumOrder;
        this.availableQuantity=availableQuantity;
    }

    public UserCart(String productNumber, String date, int productPrice, String productName, String imageUrl, int quantity, String orderStatus, String cartProductKey, String paymentMode) {
        this.productNumber = productNumber;
        this.date = date;
        this.productPrice = productPrice;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
        this.cartProductKey = cartProductKey;
        this.paymentMode = paymentMode;

    }

    public UserCart() {
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCartProductKey() {
        return cartProductKey;
    }

    public void setCartProductKey(String cartProductKey) {
        this.cartProductKey = cartProductKey;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(int minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("productNumber", productNumber);
        result.put("productPrice", productPrice);
        result.put("productName", productName);
        result.put("imageUrl",imageUrl);
        result.put("orderDate",date);
        result.put("quantity",quantity);
        result.put("minimumOrder",minimumOrder);
        result.put("availableQuantity",availableQuantity);
        return result;
    }

    @Exclude
    public Map<String, Object> toMapFinalOrderEntry(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("productNumber", productNumber);
        result.put("productPrice", productPrice);
        result.put("productName", productName);
        result.put("imageUrl",imageUrl);
        result.put("date",date);
        result.put("orderStatus",orderStatus);
        result.put("quantity", quantity);
        result.put("cartProductKey",cartProductKey);
        result.put("orderNumber",orderNumber);
        result.put("paymentMode",paymentMode);
        return result;
    }


}
