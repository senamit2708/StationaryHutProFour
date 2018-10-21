package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FilterCategoryModel {

    private String type;

    public FilterCategoryModel() {
    }

    public FilterCategoryModel(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}