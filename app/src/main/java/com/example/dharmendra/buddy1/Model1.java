package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 11-05-2017.
 */

public class Model1 {

    public String user,activityname;
    public int seen;

    //String pos;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Model1() {
    }

    public Model1(String user, String activityname,int seen) {
        this.user=user;
       this.activityname=activityname;
        this.seen=seen;
    }



    public String getUser(){
        return user;
    }

  public String getActivityname(){
      return activityname;
  }

  public int getSeen(){
      return seen;
  }
}
