package com.example.mysynccontactapp.model;

public class SystemContactInfo {
    private String name;
    private String number;
    private long id;

    public SystemContactInfo(String name, String number, long id) {
        this.name = name;
        this.number = number;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SystemContactInfo{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", id=" + id +
                '}';
    }
}
