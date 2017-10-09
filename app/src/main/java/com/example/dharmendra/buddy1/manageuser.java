package com.example.dharmendra.buddy1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.uguratar.countingtextview.countingTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;


public class manageuser extends AppCompatActivity  {
    String user;
    ListView manageuser;
    Bundle b;
    int cidd;
    MapView mapView;
    GoogleMap mMap;
    DatabaseReference mDatabase;
    LinkedHashMap<String,String> map=new LinkedHashMap<>();
    Manage_user_adapter adapter;
    ArrayList<String> list=new ArrayList<>();
    ArrayList<String> receive=new ArrayList<>();
    ArrayList<String> send=new ArrayList<>();
    ArrayList<String> connection=new ArrayList<>();
    TextView tt,count,ttt;
    String admin,activityName;
    ImageView mapImage;
    LatLng pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageuser);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final CardView card = (CardView) findViewById(R.id.card_view);
        card.setVisibility(View.GONE);

        ttt=(TextView)findViewById(R.id.date);
        tt=(TextView)findViewById(R.id.activity_name);
        manageuser = (ListView) findViewById(R.id.simpleListView);
        mapImage = (ImageView) findViewById(R.id.map);

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        manageuser= (ListView)findViewById(R.id.simpleListView);
        final countingTextView count = (countingTextView) findViewById(R.id.c);
        ttt=(TextView)findViewById(R.id.date);
        tt=(TextView)findViewById(R.id.activity_name);

        b= getIntent().getExtras();
        if (b!= null){
            cidd=b.getInt("int_key");
        }

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase=FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(cidd));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                long t =post1.getActdate();
                activityName=post1.getName();

                String imageURL = post1.getMapurl();
                Picasso.with(getApplication()).load(imageURL).fit().centerCrop().noFade().into(mapImage);

                admin=post1.getUser();
                Log.d("yuio",admin);
                tt.setText(activityName);

                String date= DateFormat.format("dd-MM-yyyy", t).toString();
                long c_date=new Date().getTime();
                String format=DateFormat.format("dd-MM-yyyy", c_date).toString();
                final Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                String yest=DateFormat.format("dd-MM-yyyy",cal.getTime()).toString();
                if(date.equals(format)){
                    String nontime= DateFormat.format("HH:mm:ss",t).toString();
                    SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
                    try{
                        Date d = f1.parse(nontime);
                        SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                        ttt.setText("Created Today at "+f2.format(d).toUpperCase());
                    }
                    catch (Exception e){

                    }
                }
                else if(date.equals(yest)){
                    ttt.setText("Created Yesterday");
                }
                else {
                    ttt.setText("Created on "+DateFormat.format("dd-MM-yyyy", t));
                    Log.d("hjk", "outside");
                }
                if(user.equals(admin)) {
                    Log.d("yuio",user+" "+admin);
                    card.setVisibility(View.VISIBLE);
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog alertDialog=new AlertDialog.Builder(manageuser.this).create();
                            alertDialog.setMessage("Sure want to Delete?");
                            alertDialog.setButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (haveNetworkConnection()) {
                                        /**----------------------------------------------------------------------------------------**/
                                        mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(postSnapshot.getKey()).child("activity");
                                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                for (DataSnapshot snapshot : snapshot1.getChildren()) {
                                                                    user_activity use = snapshot.getValue(user_activity.class);
                                                                    if (use.getAid() == cidd) {
                                                                        snapshot.getRef().child("status").setValue(0);
                                                                    }
                                                                }
                                                            }
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


                                        /**-----------------------------------------------------------------------------------**/
                                        mDatabase = FirebaseDatabase.getInstance().getReference("activity");
                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                    Activity1 post1 = postSnapshot.getValue(Activity1.class);
                                                    int ccid = post1.getCcid();
                                                    if (ccid == cidd) {
                                                        postSnapshot.getRef().child("status").setValue(0);
                                                        Toast.makeText(getApplicationContext(), "Succesfully Deleted", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(i);
                                                        break;
                                                    }

                                                }
                                            }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Toast.makeText(getApplicationContext(), "\"The read failed: \"" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            alertDialog.show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("chats").child("kick").child(String .valueOf(cidd));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String data=postSnapshot.getKey();
                   //if(!data.equals(admin))
                    list.add(data);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /***------------------------------------------------------------------**/
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("receiveFrom");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model m = postSnapshot.getValue(Model.class);
                    receive.add(m.getUser());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("sendTo");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model m = postSnapshot.getValue(Model.class);
                    send.add(m.getUser());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Model m = postSnapshot.getValue(Model.class);
                    connection_type con=postSnapshot.getValue(connection_type.class);
                    connection.add(con.getUid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        /**-----------------------------------------------------------------------**/

        mDatabase = FirebaseDatabase.getInstance().getReference("chats").child("nickname").child(String .valueOf(cidd));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String users=postSnapshot.getKey().toString();
                    String nickname=postSnapshot.getValue().toString();
                    //if(!users.equals(admin))
                    map.put(users,nickname);

                }
                count.animateText(0, map.size());
                adapter = new Manage_user_adapter(map,cidd,list,admin,send,receive,connection,activityName,getApplicationContext());
                manageuser.setAdapter(adapter);
                setListViewHeightBasedOnChildren(manageuser, adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void setListViewHeightBasedOnChildren(ListView listView, Manage_user_adapter listAdapter) {
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (50 * scale + 0.5f);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = pixels * listAdapter.getCount();
        listView.setLayoutParams(params);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;

    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
