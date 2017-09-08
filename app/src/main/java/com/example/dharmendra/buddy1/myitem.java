package com.example.dharmendra.buddy1;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Dharmendra on 22-05-2017.
 */

 public class myitem implements ClusterItem {
    private final LatLng mPosition;
   // private final String mTitle;
    //private final String mSnippet;
    String n;
    int id;
    Boolean check;

    public myitem(double lat, double lng, String n, int k, Boolean check) {
        mPosition = new LatLng(lat, lng);
        this.n=n;
        this.id=k;
        this.check=check;
    }





    @Override
    public LatLng getPosition() {
        return mPosition;
    }

   @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}