package com.example.dharmendra.buddy1;

import java.util.Date;

public class ChatMessage1 {
    private String messageText;
    private String messageUser;
    private String messageUserId,nickname;
    private long messageTime;

    public ChatMessage1(String messageText, String messageUserId,String nickname) {
        this.nickname=nickname;
        this.messageText = messageText;
        messageTime = new Date().getTime();
        this.messageUserId = messageUserId;
    }

    public ChatMessage1(){

    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public void getMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
 }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getNickname(){
        return nickname;
    }
}
