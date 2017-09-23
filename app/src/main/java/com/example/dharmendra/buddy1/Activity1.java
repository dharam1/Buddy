package com.example.dharmendra.buddy1;

import java.util.Date;

/**
 * Created by Dharmendra on 11-05-2017.
 */

public class Activity1 {

    public String user;
    public String name;
    public Double latitude;
    public Double longitude;
    public int ccid;
    public int status;
    public String address;
    long actdate;
    int type;
    //String pos;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Activity1() {
    }

    public Activity1(String user, String name, Double latitude, Double longitude, int ccid, int status,String address,int type) {
        this.user=user;
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.ccid=ccid;
        this.status=status;
        this.address=address;
        actdate = new Date().getTime();
        this.type=type;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
     return latitude;
     }

    public Double getLongitude() {
        return longitude;
    }

    public String getUser(){
        return user;
    }

    public int getCcid(){
        return ccid;
    }

    public int getStatus(){
        return status;
    }

    public String getAddress(){
        return address;
    }

    public long getActdate(){
        return  actdate;
    }

    public  int getType(){
        return type;
    }
}
