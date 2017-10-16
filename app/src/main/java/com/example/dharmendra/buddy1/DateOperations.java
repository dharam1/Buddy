package com.example.dharmendra.buddy1;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import static android.text.format.DateFormat.*;

/**
 * Created by karthik on 1/10/17.
 */

public class DateOperations {

    static Comparator<Date> dateComparator = new Comparator<Date>() {
        @Override
        public int compare(Date date, Date t1) {
            if(date.getMonth() * 35 + date.getDay() == t1.getMonth() * 35 + t1.getDay()) return 0;
            if(date.getMonth() * 35 + date.getDay() < t1.getMonth() * 35 + t1.getDay()) return -1;
            if(date.getMonth() * 35 + date.getDay() > t1.getMonth() * 35 + t1.getDay()) return +1;

            return 1111111;
        }
    };

    static String Month(Date d) {
        return (String) format("MMM",  d);

    }

    public static int logComparision(Date d1, Date d2){
        int compare = dateComparator.compare(d1, d2);
        // Log.v("Date comparision", "Dates were" +d1.toString() +"\t" + d2.toString() +"\t" + compare);
        return compare;
    }
    static String toString(long timestamp) {
        Date date=new Date(timestamp);

        String dateString;
        Date today = new Date();
        Date yesterday = new Date();
        yesterday.setTime(today.getTime() - ((long)864E5));

        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        /**if(df.format(new Date()).equals(df.format(date)))
        dateString = "Today";
        else if(df.format(yesterday).equals(df.format(date)))
            dateString ="Yesterday";
        else {**/
            dateString=new SimpleDateFormat("d MMMM").format(date);
            if(!(today.getYear()==date.getYear()))
                dateString+=" "+date.getYear()+" ";
        //}
        return dateString;
    }
}
