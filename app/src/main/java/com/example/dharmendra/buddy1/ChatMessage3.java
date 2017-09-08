package com.example.dharmendra.buddy1;

import java.util.Date;

public class ChatMessage3 {
    private String message;
    private String from;
    int id;

    public ChatMessage3(String message, String from, int id) {
       this.message=message;
        this.from=from;
        this.id=id;

    }

    public ChatMessage3(){

    }

   public String getMessage(){
       return  message;
   }
   public String getFrom(){
       return from;
   }
   public int getId(){
       return id;
   }
}
