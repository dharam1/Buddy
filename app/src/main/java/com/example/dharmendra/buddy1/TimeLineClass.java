package com.example.dharmendra.buddy1;

import java.util.Date;

/**
 * Created by Dharmendra on 14-10-2017.
 */

public class TimeLineClass implements Comparable<TimeLineClass> {
    int aid;
    String name;
    Date d;
    public TimeLineClass(int aid,String name,Date d){
        this.aid=aid;
        this.name=name;
        this.d=d;
    }
    public int getAid(){
        return aid;
    }

    public String getName(){
        return name;
    }

     public Date getDate(){
         return d;
     }
    @Override
    public int compareTo(TimeLineClass o) {
        if (getDate() == null || o.getDate() == null)
            return 0;
        return getDate().compareTo(o.getDate());
    }
}
