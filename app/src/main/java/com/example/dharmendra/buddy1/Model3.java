package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 11-05-2017.
 */

public class Model3 {

    public String user,activityname;
    public int seen;
    public long time;
    //String pos;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Model3() {
    }

    public Model3(String user, String activityname, int seen,long time) {
        this.user=user;
       this.activityname=activityname;
        this.seen=seen;
        this.time=time;
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

  public long getTime(){
      return time;
  }
}
