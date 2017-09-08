package com.example.dharmendra.buddy1;

import java.util.Date;

public class ChatMessage2 {
    public String message;

    public String from,to;

    public ChatMessage2(String message, String from, String to) {
        this.message = message;
        this.from = from;
        this.to=to;
    }

    public ChatMessage2(){

    }
    public String getMessage(){
        return message;
    }
    public String getFrom(){
        return from;
    }
    public String getTo(){
        return to;
    }


}
