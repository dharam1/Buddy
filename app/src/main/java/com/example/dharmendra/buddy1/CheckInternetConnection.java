package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 14-10-2017.
 */

public class CheckInternetConnection {


    public boolean haveNetworkConnection() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


}
