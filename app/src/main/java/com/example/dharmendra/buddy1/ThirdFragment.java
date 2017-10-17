package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 12-06-2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThirdFragment extends Fragment {
    int c_count;
    ListView requestlist;
    String user,user_login;
    int count;
    DatabaseReference mDatabase,mDatabase1;
    private static final long DRAWER_DELAY = 250;
    String name;
    LinkedHashMap<String,String> request_list=new LinkedHashMap<>();
    LinkedHashMap<String,String> url_list=new LinkedHashMap<>();
    TextView datashow;
    ArrayList<String> activitylist=new ArrayList<>();
    ArrayList<String> timelist=new ArrayList<>();
    Boolean check=true;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private OnFragmentInteractionListener listener;

    public static ThirdFragment newInstance() {
        return new ThirdFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Friend Requests");
    }
    private void goLoginScreen() {
        Intent intent = new Intent(getContext(), Facebook_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_first1, menu);
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

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_third, container, false);
        datashow=(TextView)rootview.findViewById(R.id.datashow);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.activity_main_swipe_refresh_layout);
        requestlist= (ListView)rootview.findViewById(R.id.simpleListView);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("receiveFrom");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model3 m = postSnapshot.getValue(Model3.class);
                    final String user1 = m.getUser();
                    final String activityname = m.getActivityname();
                    final long time=m.getTime();
                    Log.d("pele", user1);
                    mDatabase1 = FirebaseDatabase.getInstance().getReference("users").child(user1);
                    mDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                          if (dataSnapshot.exists()) {
                                user use = dataSnapshot.getValue(user.class);
                                name = use.getName();
                                String url=use.getUrl();
                                Log.d("pele", name);
                                request_list.put(user1, name);
                                url_list.put(user1,url);
                                activitylist.add(activityname);
                                timelist.add(String.valueOf(time));
                                HashMapAdapter1 adapter = new HashMapAdapter1(request_list, activitylist,url_list,timelist,getContext(),getFragmentManager());
                                requestlist.setAdapter(adapter);
                           }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                Log.d("pelle", String.valueOf(request_list));
            }
            else{
                    datashow.setText(Html.fromHtml("<b>You have no new Request</b> "));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.third, new ThirdFragment());
                fragmentTransaction.commit();
            }
        });



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

}


