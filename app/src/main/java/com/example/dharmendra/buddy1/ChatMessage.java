package com.example.dharmendra.buddy1;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageUserId;
    private long messageTime;
    private int message_status;
    boolean timeMessage;

    public ChatMessage(String messageText, String messageUserId,int message_status) {
        this.messageText = messageText;
        messageTime = new Date().getTime();
        this.messageUserId = messageUserId;
        this.message_status=message_status;
    }

    public ChatMessage(){

    }
    public void setTimeMessage(boolean timeMessage){
        this.timeMessage = timeMessage;
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

    public int getMessage_status(){
        return message_status;
    }
}
