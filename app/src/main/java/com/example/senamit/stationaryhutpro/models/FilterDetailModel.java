package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FilterDetailModel {

    private int index;
    private String item;
    private String type;
    private Boolean status;
    private String categoryName;

    public FilterDetailModel() {
    }

    public FilterDetailModel(String item) {
        this.item = item;
    }

//    public FilterDetailModel(String item, String type) {
//        this.item = item;
//        this.type = type;
//        this.status = false;
//    }


    public FilterDetailModel(int index, Boolean status) {
        this.index = index;
        this.status = status;
    }

    public FilterDetailModel(String item, String type, int index) {
        this.index = index;
        this.item = item;
        this.type = type;
        this.status = false;
    }

    public FilterDetailModel(String item, String type,int index, Boolean status) {
        this.index = index;
        this.item = item;
        this.type = type;
        this.status = status;
    }
    public FilterDetailModel(String categoryName, String item, String type,int index, Boolean status) {
        this.categoryName = categoryName;
        this.index = index;
        this.item = item;
        this.type = type;
        this.status = status;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
