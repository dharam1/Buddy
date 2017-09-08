package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 27-05-2017.
 */

public class user {

    public String UID;
    public String name,email,fid,url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public user() {
    }

    public user(String UID, String fid , String name, String email,String url) {
        this.url=url;
        this.UID = UID;
        this.fid=fid;
        this.name=name;
        this.email=email;

    }
    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }
}

