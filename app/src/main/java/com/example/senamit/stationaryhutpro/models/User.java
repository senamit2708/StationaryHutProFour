package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    //   private String username;
    private String mobileNumber;
    private String firstName;
    private String lastName;
    private String emailId;

    public User(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public User(String mobileNumber, String firstName, String lastName, String emailId) {
        this.mobileNumber = mobileNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    public User(String firstName, String lastName, String emailId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    public User() {
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Map<String, Object> toUserProfileEntry(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstName",firstName);
        result.put("lastName", lastName);
        result.put("emailId", emailId);
        result.put("mobileNumber", mobileNumber);
        return result;
    }
}
