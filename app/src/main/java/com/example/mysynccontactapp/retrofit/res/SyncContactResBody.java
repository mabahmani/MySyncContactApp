package com.example.mysynccontactapp.retrofit.res;

import java.util.List;

public class SyncContactResBody {
    private String msg;
    private int count;
    private List<String> friends;

    public SyncContactResBody() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SyncContactResBody{" +
                "msg='" + msg + '\'' +
                ", count=" + count +
                ", friends=" + friends +
                '}';
    }
}
