package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FilterDetailModel {

    private String item;
    private String type;

    public FilterDetailModel() {
    }

    public FilterDetailModel(String item) {
        this.item = item;
    }

    public FilterDetailModel(String item, String type) {
        this.item = item;
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
