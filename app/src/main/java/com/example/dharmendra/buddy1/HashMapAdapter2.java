/**Hash Map Adapter**/
package com.example.dharmendra.buddy1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class HashMapAdapter2 extends BaseAdapter {
    ArrayList<connection_class> con;
    private ArrayList mData;
    Context context;
    CardView cardView;
    Activity act;
    DatabaseReference mDatabase;
    FragmentManager fragmentManager;


    public HashMapAdapter2(LinkedHashMap<String, String> map, ArrayList<connection_class> con1, Context context, Activity act, FragmentManager fragmentManager) {
        Log.d("POPKLLL", con1.size() + "");
        mData = new ArrayList<>();
        mData.addAll(map.entrySet());
        con = new ArrayList<>();
        this.con = con1;
        this.context = context;
        this.act=act;
        this.fragmentManager=fragmentManager;

    }

    @Override
    public int getCount() {
        return con.size();
    }

    @Override
    public LinkedHashMap.Entry<String, String> getItem(int position) {
        return (LinkedHashMap.Entry) mData.get(position);
    }


    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View result;
        Log.d("POPKLL", "POPKL_EXE");
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view, parent, false);
        } else {
            result = convertView;
        }

        LinkedHashMap.Entry<String, String> item = getItem(position);
        String name = con.get(position).getName();
        String url11 = con.get(position).getUrl();
        String lm = con.get(position).getMessage();
        String time = String.valueOf(con.get(position).getTime());
        String type = con.get(position).getType();
        CircleImageView typev = (CircleImageView) result.findViewById(R.id.type);
        cardView=(CardView)result.findViewById(R.id.card_view);
        Log.d("POPKLLL", name + "" + url11 + "" + lm + "" + time + "" + type);

        if (type.equals("facebook"))
            typev.setImageResource(R.drawable.ic_active_fb);
        else if (type.equals("buddy"))
            typev.setImageResource(R.mipmap.ic_launcher);

        String count = con.get(position).getCount();

        // TODO replace findViewById by ViewHolder
        //((TextView) result.findViewById(R.id.textView)).setText(String.valueOf(item.getKey()));
        ((TextView) result.findViewById(R.id.textView)).setText(name);
        ImageView imgv = (ImageView) result.findViewById(R.id.imageview);
        Picasso.with(context).load(url11).fit().centerCrop().into(imgv);
        TextView counter = (TextView) result.findViewById(R.id.counter);
        TextView lmv = (TextView) result.findViewById(R.id.textView1);
        TextView timev = (TextView) result.findViewById(R.id.textView2);
        LinearLayout ll = (LinearLayout) result.findViewById(R.id.ll);
        LinearLayout ll2 = (LinearLayout) result.findViewById(R.id.ll2);
        if (time.equals("0")) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.weight = 2.0f;
            ll.setLayoutParams(params);
            ll2.setVisibility(View.GONE);
            Log.d("hjk", "inside");
        } else {
            long t = Long.parseLong(time);
            String date = DateFormat.format("dd/MM/yyyy", t).toString();
            long c_date = new Date().getTime();
            String format = DateFormat.format("dd/MM/yyyy", c_date).toString();
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String yest = DateFormat.format("dd/MM/yyyy", cal.getTime()).toString();
            if (date.equals(format)) {
                String nontime = DateFormat.format("HH:mm:ss", t).toString();
                SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                try {
                    Date d = f1.parse(nontime);
                    SimpleDateFormat f2 = new SimpleDateFormat("hh:mm");
                    timev.setText(f2.format(d).toUpperCase());
                } catch (Exception e) {

                }


            } else if (date.equals(yest)) {
                timev.setText("YESTERDAY");
            } else {
                timev.setText(DateFormat.format("dd/MM/yyyy", t));
                Log.d("hjk", "outside");
            }
        }
        if (count.equals("0")) {
            counter.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lmv.getLayoutParams();
            params.weight = 8.0f;
            lmv.setLayoutParams(params);
            Log.d("hjk", "inside");
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lmv.getLayoutParams();
            params.weight = 7.0f;
            lmv.setLayoutParams(params);
            counter.setText(count);
            Log.d("hjk", "outside");
        }
        if (lm.equals("0")) {
            lmv.setVisibility(View.GONE);
            Log.d("hjk", "inside");
        } else {
            int size = lm.length();

            lmv.setText(lm);
        }

        Log.d("hjk", "outside");
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=con.get(position).getId();
                String name=con.get(position).getName();
                Intent i = new Intent(act, PcChat.class);
                i.putExtra("int_key",id);
                i.putExtra("int_key1",name);
                act.startActivity(i);
                act.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String content=con.get(position).getId();
                String name=con.get(position).getName();
                AlertDialog alertDialog=new AlertDialog.Builder(context).create();
                alertDialog.setMessage("Remove From Connection");
                alertDialog.setButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(haveNetworkConnection()){
                            final String user= FirebaseAuth.getInstance().getCurrentUser().getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        connection_type con=postSnapshot.getValue(connection_type.class);
                                        final String id=con.getUid();
                                        if(id.equals(content)){
                                            /**-------------------------------------------------------------------------------------------**/
                                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("connection").child(content);
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    connection_type con=dataSnapshot.getValue(connection_type.class);
                                                    String type=con.getType().toString();
                                                    if(type.equals("facebook")){

                                                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(content).child("fid");
                                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                String fid=dataSnapshot.getValue().toString();
                                                                mDatabase = FirebaseDatabase.getInstance().getReference("fbblockeduser").child(user).child(content);
                                                                mDatabase.setValue(fid);
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("fid");
                                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                String fid=dataSnapshot.getValue().toString();
                                                                mDatabase = FirebaseDatabase.getInstance().getReference("fbblockeduser").child(content).child(user);
                                                                mDatabase.setValue(fid);
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



                                            /**-------------------------------------------------------------------------------------------**/
                                            postSnapshot.getRef().removeValue();
                                            /**-------------------------------------------------------------------------------------------**/
                                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("activity");
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                        for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                                                            user_activity use = snapshot.getValue(user_activity.class);
                                                            String n = use.getUser();
                                                            int x = use.getGlobal_buddies();
                                                            if (n.equals(content) && x == 1) {
                                                                snapshot.getRef().removeValue();
                                                            } else if (n.equals(content) && x == 0) {
                                                                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("activity").child(content).child(String.valueOf(use.getAid())).child("fromconnection");
                                                                mDatabase.setValue(0);
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            /**--------------------------------------------------------------------------------------------**/
                                            Toast.makeText(context, "Successfully Removed", Toast.LENGTH_SHORT).show();
                                            fragmentManager .beginTransaction().replace(R.id.second, new SecondFragment()).commit();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("Noti");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Model1 m = postSnapshot.getValue(Model1.class);
                                            String id = m.getUser();
                                            if (id.equals(content)) {
                                                postSnapshot.getRef().removeValue();
                                                break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(content).child("connection");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        connection_type con=postSnapshot.getValue(connection_type.class);
                                        final String id=con.getUid();
                                        if(id.equals(user)){
                                            postSnapshot.getRef().removeValue();
                                            /**---------------------------------------------------------------------------------------------**/
                                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(content).child("activity");
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                        for (DataSnapshot Snapshot : postSnapshot.getChildren()) {
                                                            user_activity use = Snapshot.getValue(user_activity.class);
                                                            String n = use.getUser();
                                                            int x = use.getGlobal_buddies();
                                                            if (n.equals(user) && x == 1) {
                                                                Snapshot.getRef().removeValue();
                                                            } else if (n.equals(user) && x == 0) {
                                                                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(content).child("activity").child(user).child(String.valueOf(use.getAid())).child("fromconnection");
                                                                mDatabase.setValue(0);
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            /**-----------------------------------------------------------------------------------------------**/
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(content).child("Noti");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Model1 m = postSnapshot.getValue(Model1.class);
                                            String id = m.getUser();
                                            if (id.equals(user)) {
                                                postSnapshot.getRef().removeValue();
                                                break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertDialog.show();







                return true;
            }
        });
        return result;
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
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