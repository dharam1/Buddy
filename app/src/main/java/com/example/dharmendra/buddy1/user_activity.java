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
    int fromconnection,global_buddies,status;

    public user_activity() {
    }

    public user_activity(String user,int aid,String type,int fromconnection,int global_buddies,int status) {
        this.user=user;
        this.aid=aid;
        time= new Date().getTime();
        this.type=type;
        this.fromconnection=fromconnection;
        this.global_buddies=global_buddies;
        this.status=status;

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

     public int getFromconnection(){
         return fromconnection;
     }
        public int getGlobal_buddies(){
            return global_buddies;
        }

        public int getStatus(){
            return status;
        }



}
