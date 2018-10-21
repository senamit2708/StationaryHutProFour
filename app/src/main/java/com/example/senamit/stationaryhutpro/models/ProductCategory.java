package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ProductCategory {

    private String productItem;

    public ProductCategory() {
    }

    public ProductCategory(String productItem) {
        this.productItem = productItem;
    }

    public String getProductItem() {
        return productItem;
    }

    public void setProductItem(String productItem) {
        this.productItem = productItem;
    }
}
