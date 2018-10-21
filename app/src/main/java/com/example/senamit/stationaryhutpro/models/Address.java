package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Address {
    private String fullName;
    private String mobileNumber;
    private String pincode;
    private String addressPartOne;
    private String addressPartTwo;
    private String landMark;
    private String city;
    private String state;
    private int status;
    private String date;
    private String firebaseKey;

    public Address() {
    }

    public Address(String fullName, String mobileNumber, String pincode, String addressPartOne, String addressPartTwo, String landMark, String city, String state, int status, String date) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.pincode = pincode;
        this.addressPartOne = addressPartOne;
        this.addressPartTwo = addressPartTwo;
        this.landMark = landMark;
        this.city = city;
        this.state = state;
        this.status = status;
        this.date = date;
    }

    public Address(String fullName, String mobileNumber, String pincode, String addressPartOne, String addressPartTwo, String landMark, String city, String state, int status, String date, String firebaseKey) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.pincode = pincode;
        this.addressPartOne = addressPartOne;
        this.addressPartTwo = addressPartTwo;
        this.landMark = landMark;
        this.city = city;
        this.state = state;
        this.status = status;
        this.date = date;
        this.firebaseKey = firebaseKey;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddressPartOne() {
        return addressPartOne;
    }

    public void setAddressPartOne(String addressPartOne) {
        this.addressPartOne = addressPartOne;
    }

    public String getAddressPartTwo() {
        return addressPartTwo;
    }

    public void setAddressPartTwo(String addressPartTwo) {
        this.addressPartTwo = addressPartTwo;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("fullName", fullName);
        result.put("mobileNumber", mobileNumber);
        result.put("pincode", pincode);
        result.put("addressPartOne", addressPartOne);
        result.put("addressPartTwo", addressPartTwo);
        result.put("landMark", landMark);
        result.put("city", city);
        result.put("state", state);
        result.put("status", status);
        result.put("date", date);
        result.put("firebaseKey",firebaseKey);

        return result;
    }
}
