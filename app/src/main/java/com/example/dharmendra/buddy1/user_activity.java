package com.example.dharmendra.buddy1;

import java.util.Date;

/**
 * Created by Dharmendra on 11-05-2017.
 */

public class user_activity {

   public String user;
    int aid;
    public user_activity() {
    }

    public user_activity(String user,int aid) {
        this.user=user;
        this.aid=aid;
    }
    public String getUser(){
        return user;
    }
    public int getAid(){
        return aid;
    }


}
