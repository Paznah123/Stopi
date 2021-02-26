package com.example.Stopi.social.chat;

public class Message {

    private String sender;
    private String receiver;
    private String message;
    private long timestamp;

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

    public long getTimestamp() { return timestamp; }

    //=============================

    public Message setSender(String sender) { this.sender = sender; return this; }

    public Message setReceiver(String receiver) { this.receiver = receiver; return this; }

    public Message setMessage(String message) { this.message = message; return this; }

    public Message setTimestamp(long timestamp) { this.timestamp = timestamp; return this; }

}
