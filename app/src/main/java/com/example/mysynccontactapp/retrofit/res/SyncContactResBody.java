package com.example.mysynccontactapp.retrofit.res;

import java.util.List;

public class SyncContactResBody {
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


    @Override
    public String toString() {
        return "SyncContactResBody{" +
                "count=" + count +
                ", friends=" + friends +
                '}';
    }
}
