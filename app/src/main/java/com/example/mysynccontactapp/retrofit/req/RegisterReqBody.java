package com.example.mysynccontactapp.retrofit.req;

import com.google.gson.annotations.SerializedName;

public class RegisterReqBody {
    @SerializedName("user_phone")
    private String userPhone;

    public RegisterReqBody(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Override
    public String toString() {
        return "RegisterReqBody{" +
                "userPhone='" + userPhone + '\'' +
                '}';
    }
}
