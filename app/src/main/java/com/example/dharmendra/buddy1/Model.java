package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 11-05-2017.
 */

public class Model {

    public String user,activityname;

    //String pos;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Model() {
    }

    public Model(String user, String activityname) {
        this.user=user;
       this.activityname=activityname;
    }



    public String getUser(){
        return user;
    }

  public String getActivityname(){
      return activityname;
  }
}
