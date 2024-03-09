package com.golobon.g_chat;

import java.util.Date;

public class Message {
    public String userName;
    public String textMessage;
    private long messageType;

    public Message() { }
    public Message(String userName, String textMessage) {
        this.userName = userName;
        this.textMessage = textMessage;
        this.messageType = new Date().getTime();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getMessageType() {
        return messageType;
    }

    public void setMessageType(long messageType) {
        this.messageType = messageType;
    }
}
