package com.example.dharmendra.buddy1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dharmendra on 22-05-2017.
 */

public class CustomRenderer extends DefaultClusterRenderer<myitem> implements ClusterManager.OnClusterClickListener<myitem>,ClusterManager.OnClusterItemClickListener<myitem>{

    private boolean shouldCluster = true;
    private static final int MIN_CLUSTER_SIZE = 1;
    IconGenerator ig;
    GoogleMap mMap;
    Map<String, Integer> mMarkers = new HashMap<String, Integer>();
    int k=0;
    public CustomRenderer(Context context, GoogleMap map, ClusterManager<myitem> clusterManager ) {
        super(context, map, clusterManager);
        // TODO Auto-generated constructor stub
        ig=new IconGenerator(context);


    }

    public void setMarkersToCluster(boolean toCluster)
    {
        this.shouldCluster = toCluster;
    }

    @Override
    protected void onBeforeClusterItemRendered(myitem myitem, MarkerOptions markerOptions) {
        // Draw a single person.
        // Set the info window to show their name
        if(myitem.check.equals(Boolean.TRUE)) {
            ig.setColor(Color.parseColor("#CFD8DC"));
        }
        else {
            ig.setColor(Color.parseColor("#ffffff"));
        }
        Bitmap bmp = ig.makeIcon(myitem.n);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bmp));
    }


    @Override
    protected boolean shouldRenderAsCluster(Cluster<myitem> cluster) {

        if(shouldCluster)
        {
            return cluster.getSize() > MIN_CLUSTER_SIZE;
        }
        else
        {
            return shouldCluster;
        }

    }
  @Override
    public boolean onClusterClick(Cluster<myitem> cluster) {
        Log.d("bhaumik","bhaumik");
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        return true;
    }
    @Override
    public boolean onClusterItemClick(myitem item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Log.d("dharam",String.valueOf(item.id));
        return false;
    }
}
