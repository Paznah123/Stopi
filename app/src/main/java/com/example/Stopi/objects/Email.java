package com.example.Stopi.objects;

public class Email {

    private String senderKey;
    private String title;
    private String msg;

    //=============================

    public Email(){}

    //=============================

    public Email setTitle(String title) { this.title = title; return this; }

    public Email setMsg(String msg) { this.msg = msg; return this; }

    public Email setSenderKey(String senderKey) { this.senderKey = senderKey; return this; }

    //=============================

    public String getTitle() { return title; }

    public String getMsg() { return msg; }

    public String getSenderKey() { return senderKey; }

}
