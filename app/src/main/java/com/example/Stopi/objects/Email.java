package com.example.Stopi.objects;

public class Email {

    private String key;
    private String title;
    private String msg;
    private String itemPhotoUrl;

    //=============================

    public Email(){}

    //=============================

    public Email setTitle(String title) { this.title = title; return this; }

    public Email setMsg(String msg) { this.msg = msg; return this; }

    public Email setItemPhotoUrl(String itemPhotoUrl) { this.itemPhotoUrl = itemPhotoUrl; return this; }

    public Email setKey(String key) { this.key = key; return this; }

    //=============================

    public String getTitle() { return title; }

    public String getMsg() { return msg; }

    public String getItemPhotoUrl() { return itemPhotoUrl; }

    public String getKey() { return key; }

}
