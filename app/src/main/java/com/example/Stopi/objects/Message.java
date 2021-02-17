package com.example.Stopi.objects;

public class Message {

    private String sender;
    private String receiver;
    private String message;

    //=============================

    public Message() { }

    //=============================

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    //=============================

    public Message setSender(String sender) { this.sender = sender; return this; }

    public Message setReceiver(String receiver) { this.receiver = receiver; return this; }

    public Message setMessage(String message) { this.message = message; return this; }
}
