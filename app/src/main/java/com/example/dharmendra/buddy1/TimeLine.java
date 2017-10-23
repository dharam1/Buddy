package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 12-06-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import static com.example.dharmendra.buddy1.R.id.map;
import static com.facebook.FacebookSdk.getApplicationContext;

public class TimeLine extends Fragment {
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
    ListView timeline;
    LinkedHashMap<String ,String> followedactivity =new LinkedHashMap<>();
    LinkedHashMap<Integer ,Integer> followedactivityid =new LinkedHashMap<>();
    LinkedHashMap<Integer ,String> followeduser =new LinkedHashMap<>();
    timelineadapter adapter;
    String name,n;
    int status;
    ArrayList<Integer> aidlist=new ArrayList<>();
    FloatingActionButton gotomaps;


    private OnFragmentInteractionListener listener;

    public static TimeLine newInstance() {
        return new TimeLine();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Timeline");
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
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.developers.buddy");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Buddy"));
                }
            }, DRAWER_DELAY);
        }
        /**if (item.getItemId() == R.id.refresh) {
            Fragment fragment = TimeLine.newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.first, fragment);
            fragmentTransaction.commit();
        }**/


        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
          View rootview= inflater.inflate(R.layout.fragment_timeline, container, false);
        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.activity_main_swipe_refresh_layout);
        timeline= (ListView)rootview.findViewById(R.id.simpleListView);
        gotomaps=(FloatingActionButton)rootview.findViewById(R.id.gotomaps);
        //gotomaps.setVisibility(View.VISIBLE);
        gotomaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Fragment fragment = new FirstFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.timeline, fragment);
                fragmentTransaction.addToBackStack(null);
                gotomaps.setVisibility(View.INVISIBLE);
                fragmentTransaction.commitAllowingStateLoss();**/
                Intent i = new Intent(getActivity(), maps_topic.class);
                startActivity(i);
                getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.timeline, new TimeLine());
                fragmentTransaction.commit();
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ArrayList<TimeLineClass> timeLinelist=new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("activity");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    followedactivityid.clear();
                    timeLinelist.clear();
                    int io = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (!postSnapshot.getKey().equals(user)) {
                        for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                            user_activity use = snapshot.getValue(user_activity.class);
                                final int aid = Integer.parseInt(String.valueOf(use.getAid()));
                                final String userid = use.getUser();
                                final long time = use.getTime();
                                Date d=new Date(time);
                                if (use.getFromconnection() == 1&&use.getStatus()==1) {
                                    Log.d("POPIKLJ",aid+"");
                                    followeduser.put(io, "-" + userid);
                                    String name="-"+userid;
                                    followedactivityid.put(io, aid);
                                    TimeLineClass t=new TimeLineClass(aid,name,d);
                                    timeLinelist.add(t);
                                } else{
                                if(use.getStatus()==1) {
                                    Log.d("POPIKLJ", aid + "");
                                    aidlist.add(Integer.parseInt(snapshot.getKey()));
                                    followeduser.put(io, "Anonymous-" + userid);
                                    followedactivityid.put(io, aid);
                                    TimeLineClass t=new TimeLineClass(aid,"Anonymous-"+userid,d);
                                    timeLinelist.add(t);
                                }
                                }
                                io++;
                        }
                    }
                }

                Log.d("POPKLL", followeduser.size() + "");
                    Collections.sort(timeLinelist);
                    Collections.reverse(timeLinelist);
                adapter = new timelineadapter(followedactivityid,timeLinelist,getApplicationContext(), getActivity(),getFragmentManager(),savedInstanceState);
                timeline.setAdapter(adapter);
            }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootview;
        }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
            //gotomaps.show();
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
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    }


