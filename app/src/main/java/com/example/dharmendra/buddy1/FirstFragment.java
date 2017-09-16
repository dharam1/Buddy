package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 12-06-2017.
 */

import android.*;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;

import static com.example.dharmendra.buddy1.R.id.map;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FirstFragment extends Fragment implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<myitem>,ClusterManager.OnClusterItemClickListener<myitem>{
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
    int count;


    private OnFragmentInteractionListener listener;

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        gps=new GPSTracker(getContext());
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if(latitude==0&&longitude==0){
                //Toast.makeText(getContext(), "Please Refresh to get Location", Toast.LENGTH_SHORT).show();
            }
        } else {
            gps.showSettingsAlert();
        }
        pos = new LatLng(latitude,longitude);

        }
    private void goLoginScreen() {
        Intent intent = new Intent(getContext(), Facebook_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_first, menu);
        RelativeLayout noti = (RelativeLayout) menu.findItem(R.id.noti).getActionView();
        final TextView notimCounter = (TextView) noti.findViewById(R.id.counter);
        ImageButton notiimgb=(ImageButton)noti.findViewById(R.id.imgbutton);
        notiimgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = SixthFragment.newInstance();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });

        /**-----------------------------------------------Notification-------------------------------------------**/
        user_login=  FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase1= FirebaseDatabase.getInstance().getReference("users").child(user_login).child("Noti");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model1 m = postSnapshot.getValue(Model1.class);
                    int seen=m.getSeen();
                    if(seen==0){
                        count=count+1;
                    }
                }
                Log.d("pele",String.valueOf(count));

                notimCounter.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /**---------------------------------------------------------------------------------------------------------**/




        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.navigation_request).getActionView();
        final TextView mCounter = (TextView) badgeLayout.findViewById(R.id.counter);
        ImageButton imgb=(ImageButton)badgeLayout.findViewById(R.id.imgbutton);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = ThirdFragment.newInstance();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });

/**-----------------------------------------------Notification_Request-------------------------------------------**/
        user_login=  FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase1= FirebaseDatabase.getInstance().getReference("users").child(user_login).child("receiveFrom");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model1 m = postSnapshot.getValue(Model1.class);
                    int seen=m.getSeen();
                    if(seen==0){
                        count=count+1;

                    }
                }
                Log.d("pele",String.valueOf(count));

                mCounter.setText(String.valueOf(count));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /**---------------------------------------------------------------------------------------------------------**/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_sign_out) {
           /** user = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user).child("token");
            mDatabase.removeValue();**/
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            goLoginScreen();
        }
        if (item.getItemId() == R.id.menu_share) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey there, I am using Buddy App! Download the app now :D");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Buddy"));
                }
            }, DRAWER_DELAY);
        }
        if (item.getItemId() == R.id.refresh) {
            Fragment fragment = FirstFragment.newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.first, fragment);
            fragmentTransaction.commit();
        }


        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          View rootview= inflater.inflate(R.layout.fragment_first, container, false);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);




        return rootview;
        }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        gps = new GPSTracker(getContext());
        /**final ProgressDialog Dialog = new ProgressDialog(getContext());
        Dialog.setMessage("Loading...");
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.show();**/
        /**------------------------------------------------------------------------------------------**/
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("activity");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    user_activity use=postSnapshot.getValue(user_activity.class);
                    final int aid=Integer.parseInt(String.valueOf(use.getAid()));
                    Log.d("popkl",String.valueOf(aid));
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
                            if(status==1) {
                                Boolean check = false;
                                if (f_id.equals(user)) {
                                    check = true;
                                    addItems(l, lo, n, ccid, check);
                                } else {
                                    addItems(l, lo, n, ccid, check);
                                }
                            }
                            customRenderer = new CustomRenderer(getApplicationContext(), mMap, mClusterManager);
                            mClusterManager.setRenderer(customRenderer);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        /**------------------------------------------------------------------------------------------**/
        /**user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("activity");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                int status=post1.getStatus();
                String f_id = post1.getUser();
                String n = post1.getName();
                Double l = post1.getLatitude();
                Double lo = post1.getLongitude();
                int ccid=post1.getCcid();
                ig = new IconGenerator(getApplicationContext());
                Bitmap bmp = ig.makeIcon(n);
                if(status==1) {
                    Boolean check = false;
                    if (f_id.equals(user)) {
                        check = true;
                        addItems(l, lo, n, ccid, check);
                    } else {
                        addItems(l, lo, n, ccid, check);
                    }
                }
                customRenderer = new CustomRenderer(getApplicationContext(), mMap, mClusterManager);
                mClusterManager.setRenderer(customRenderer);
               // Dialog.hide();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mMap.clear();
                mClusterManager.clearItems();
                Log.d("ronaldo","messi");
                mDatabase = FirebaseDatabase.getInstance().getReference("activity");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("ronaldo","messi1");
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Activity1 post1 = postSnapshot.getValue(Activity1.class);
                            int status=post1.getStatus();
                            String f_id = post1.getUser();
                            String n = post1.getName();
                            Double l = post1.getLatitude();
                            Double lo = post1.getLongitude();
                            int ccid=post1.getCcid();
                            ig = new IconGenerator(getApplicationContext());
                            Bitmap bmp = ig.makeIcon(n);
                            if(status==1) {
                                Boolean check = false;
                                if (f_id.equals(user)) {
                                    check = true;
                                    addItems(l, lo, n, ccid, check);
                                } else {
                                    addItems(l, lo, n, ccid, check);
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
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });**/

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10));
        mClusterManager = new ClusterManager<>(getContext(), mMap);
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
        Intent i = new Intent(getActivity(), Chat.class);
        i.putExtra("int_key", item.id);
        startActivity(i);
        getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
        return false;
    }

    }


