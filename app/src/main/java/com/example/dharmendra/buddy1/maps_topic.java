package com.example.dharmendra.buddy1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;

import java.util.Date;

import static com.example.dharmendra.buddy1.R.id.map;
import static com.facebook.FacebookSdk.getApplicationContext;

public class maps_topic extends AppCompatActivity implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<myitem>,ClusterManager.OnClusterItemClickListener<myitem> {
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    String user,user_login;
    IconGenerator ig;
    DatabaseReference mDatabase,mDatabase1;
    CustomRenderer customRenderer;
    private ClusterManager<myitem> mClusterManager;
    private static final long DRAWER_DELAY = 250;
    GPSTracker gps;
    Double latitude=0.0,longitude=0.0;
    LatLng pos;
    PlaceAutocompleteFragment autocompleteFragment;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_maps_topic);
        gps=new GPSTracker(getApplicationContext());
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if(latitude==0&&longitude==0){
                //Toast.makeText(getContext(), "Please Refresh to get Location", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(getActivity(), "False", Toast.LENGTH_SHORT).show();
            gps.showSettingsAlert();
        }
        pos = new LatLng(21.753109, -10.696095);
        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        mapFragment = (SupportMapFragment)fragment;
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //Toast.makeText(getActivity(), "Place: "+place.getLatLng(), Toast.LENGTH_SHORT).show();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),13));
            }

            @Override
            public void onError(Status status) {

            }
        });
        mapFragment.getMapAsync(maps_topic.this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        gps = new GPSTracker(getApplicationContext());
        /**------------------------------------------------------------------------------------------**/
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("activity");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (!postSnapshot.getKey().equals(user)) {
                            for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                                user_activity use = snapshot.getValue(user_activity.class);
                                final int aid = Integer.parseInt(String.valueOf(use.getAid()));
                                final String userid = use.getUser();
                                final long time = use.getTime();
                                Date d=new Date(time);
                                if (use.getFromconnection() == 1&&use.getStatus()==1&&use.getType().equals("Created")) {
                                    Log.d("POPIKLJ",aid+"");
                                    mDatabase = FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(aid));
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.d("popkl","execute");
                                            Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                                            String f_id = post1.getUser();
                                            String n = post1.getName();
                                            Double l = post1.getLatitude();
                                            Double lo = post1.getLongitude();
                                            int ccid=post1.getCcid();
                                            ig = new IconGenerator(getApplicationContext());
                                            Bitmap bmp = ig.makeIcon(n);
                                            Boolean check = true;
                                            addItems(l, lo, n, ccid, check);
                                            //aidlist.add(aid);
                                            //customRenderer = new CustomRenderer(getApplicationContext(), mMap, mClusterManager);
                                            //mClusterManager.setRenderer(customRenderer);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                } else{
                                    if(use.getStatus()==1&&use.getType().equals("Created")) {
                                        mDatabase = FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(aid));
                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Log.d("popkl","execute");
                                                Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                                                int status=post1.getStatus();
                                                String f_id = post1.getUser();
                                                String n = post1.getName();
                                                Double l = post1.getLatitude();
                                                Double lo = post1.getLongitude();
                                                int ccid=post1.getCcid();
                                                ig = new IconGenerator(getApplicationContext());
                                                Bitmap bmp = ig.makeIcon(n);
                                                boolean check=false;
                                                addItems(l, lo, n, ccid, check);
                                                //aidlist.add(aid);
                                                //customRenderer = new CustomRenderer(getApplicationContext(), mMap, mClusterManager);
                                                //mClusterManager.setRenderer(customRenderer);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                    customRenderer = new CustomRenderer(getApplicationContext(), mMap, mClusterManager);
                    mClusterManager.setRenderer(customRenderer);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 4));
        mClusterManager = new ClusterManager<>(getApplicationContext(), mMap);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnCameraIdleListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.cluster();

    }
    private void addItems(Double lat,Double lng,String n,int k,Boolean check) {
        myitem offsetItem = new myitem(lat, lng, n, k, check);
        mClusterManager.addItem(offsetItem);
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
        // Toast.makeText(getContext(), "Clicked" +item.id, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(maps_topic.this, Chat.class);
        i.putExtra("int_key", item.id);
        startActivity(i);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
        return false;
    }

}
