package com.example.mysynccontactapp.retrofit.req;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncContactReqBody {
    @SerializedName("user_phone")
    private String userPhone;
    @SerializedName("user_contacts")
    private List<String> userContacts;

    public SyncContactReqBody(String userPhone, List<String> userContacts) {
        this.userPhone = userPhone;
        this.userContacts = userContacts;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public List<String> getUserContacts() {
        return userContacts;
    }

    public void setUserContacts(List<String> userContacts) {
        this.userContacts = userContacts;
    }

    @Override
    public String toString() {
        return "SyncContactReqBody{" +
                "userPhone='" + userPhone + '\'' +
                ", userContacts=" + userContacts +
                '}';
    }
}
