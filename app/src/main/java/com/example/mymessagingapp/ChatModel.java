package com.example.mymessagingapp;

public class ChatModel {
    public String chatMessage;
    public boolean isSend;

    public ChatModel(String chatMessage, boolean isSend) {
        this.chatMessage = chatMessage;
        this.isSend = isSend;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public boolean isSend() {
        return isSend;
    }
}
