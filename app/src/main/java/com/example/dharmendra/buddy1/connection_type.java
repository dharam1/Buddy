package com.example.dharmendra.buddy1;

import java.util.Date;

/**
 * Created by Dharmendra on 11-05-2017.
 */

public class connection_type {

    public String uid;
    public String type;


    public connection_type() {
    }

    public connection_type (String uid,String type){
        this.uid=uid;
        this.type=type;
    }

    public String getUid(){
        return uid;
    }

    public String getType(){
        return type;
    }
}
