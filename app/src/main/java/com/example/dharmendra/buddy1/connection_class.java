package com.example.dharmendra.buddy1;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Dharmendra on 22-09-2017.
 */

public class connection_class implements Comparable<connection_class> {
     String name,type,url,message,count,id;
    long time;
    Date date;
    public connection_class() {
    }

    public connection_class(String id,String name, String type, String url, String messsage, long time, String count,Date date) {
        this.name=name;
        this.type=type;
        this.url=url;
        this.message=messsage;
        this.time=time;
        this.count=count;
        this.date=date;
        this.id=id;
    }


    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public String getUrl(){
        return url;
    }
    public String getMessage(){
        return message;
    }
    public long getTime(){
        return time;
    }
    public String getCount(){
        return count;
    }
    public Date getDate(){
        return date;
    }
    public String getId(){
        return id;
    }

    @Override
    public int compareTo(connection_class o) {
        if (getDate() == null || o.getDate() == null)
            return 0;
        return getDate().compareTo(o.getDate());
    }


}
