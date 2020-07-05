package com.example.mysynccontactapp.retrofit.res;

import java.util.List;

public class RegisterUserResBody {

    private List<String> notif;

    public RegisterUserResBody() {
    }

    public List<String> getNotif() {
        return notif;
    }

    public void setNotif(List<String> notif) {
        this.notif = notif;
    }

    @Override
    public String toString() {
        return "RegisterUserRes{" +
                "notif=" + notif +
                '}';
    }
}
