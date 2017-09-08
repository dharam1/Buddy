package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 11-05-2017.
 */

public class Model2 {

    public String to,from,actname;

    //String pos;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Model2() {
    }

    public Model2(String to, String from, String actname) {
        this.to=to;
        this.from=from;
        this.actname=actname;
    }



   public String getTo(){
       return to;
   }
   public String getFrom(){
       return from;
   }
   public String getActname(){
       return actname;
   }
}
