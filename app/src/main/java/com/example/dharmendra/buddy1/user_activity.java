package com.example.dharmendra.buddy1;

import java.util.Date;

/**
 * Created by Dharmendra on 11-05-2017.
 */

public class user_activity {

   public String user;
    int aid;
    public long time;
    public String type;

    public user_activity() {
    }

    public user_activity(String user,int aid,String type) {
        this.user=user;
        this.aid=aid;
        time= new Date().getTime();
        this.type=type;

    }
    public String getUser(){
        return user;
    }
    public int getAid(){
        return aid;
    }
     public long getTime(){
         return time;
     }
     public String getType(){
         return type;
     }




}
