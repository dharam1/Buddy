package com.example.dharmendra.buddy1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class Manage_user_adapter extends BaseAdapter {
    private final ArrayList mData;
    ArrayList addresslist;
    private SparseBooleanArray mSelectedItemsIds;
    ImageButton manageuser;
    CardView cardview;
    Context context;
    TextView kick,request;
    int aid;
    DatabaseReference mDatabase;
    ArrayList<String> list=new ArrayList<>();
    ArrayList<String> receive=new ArrayList<>();
    ArrayList<String> send=new ArrayList<>();
    ArrayList<String> connection=new ArrayList<>();
    String admin,actname;



    public Manage_user_adapter(LinkedHashMap<String, String> map ,int aid,ArrayList list,String admin,ArrayList send,ArrayList receive,ArrayList connection,String actname,Context context) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
       //Collections.reverse(mData);
        this.aid=aid;
        this.list=list;
        this.admin=admin;
        this.send=send;
        this.receive=receive;
        this.connection=connection;
        this.actname=actname;
        this.context=context;
    }

    @Override
    public int getCount() {
        return mData.size();
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
    public View getView(int position, final View convertView, ViewGroup parent) {
        final View result;
        CardView cardView;
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view_manage, parent, false);
        } else {
            result = convertView;
        }
        kick = (TextView) result.findViewById(R.id.button);
        request = (TextView) result.findViewById(R.id.button1);
        cardView = (CardView) result.findViewById(R.id.card_view);
        final LinkedHashMap.Entry<String, String> item = getItem(position);
        //if(!item.getKey().equals(user))
        ((TextView) result.findViewById(R.id.textView)).setText(item.getValue());
        Log.d("grp info", user + " " + admin + " " + item.getKey());
        if(user.equals(item.getKey())){
            kick.setVisibility(View.INVISIBLE);
            request.setVisibility(View.INVISIBLE);
        }
        else if(user.equals(admin) && !user.equals(item.getKey())){
            kick.setVisibility(View.VISIBLE);
            request.setVisibility(View.VISIBLE);
            if (list.contains(item.getKey())/**&&!item.getKey().equals(user)**/) {
                kick.setText("Un Ban");
                kick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView b = (TextView) v.findViewById(R.id.button);
                        b.setText("Ban");
                        Log.d("qwerty", "11");
                        final String key = item.getKey().toString();
                        mDatabase = FirebaseDatabase.getInstance().getReference("chats").child("kick").child(String.valueOf(aid)).child(key);
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    list.remove(key);
                                    dataSnapshot.getRef().removeValue();
                                    notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

            } else /**if(!item.getKey().equals(user))**/ {
                kick.setText("Ban");
                kick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView b = (TextView) v.findViewById(R.id.button);
                        b.setText("Un Ban");
                        Log.d("qwerty", "22");
                        final String user = item.getKey().toString();
                        final String nickname = item.getValue().toString();
                        mDatabase = FirebaseDatabase.getInstance().getReference("chats").child("kick").child(String.valueOf(aid));
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                list.add(user);
                                mDatabase.child(user).setValue(nickname);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

            }



            if (receive.contains(item.getKey())) {
                request.setText("Already Receive");
                request.setEnabled(false);
            } else if (send.contains(item.getKey())) {
                request.setText("Already Send");
                request.setEnabled(false);
            } else {
                request.setEnabled(true);
                request.setText("Send");
                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("uiio", "connect");

                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("sendTo");
                        final String userId = mDatabase.push().getKey();
                        Model m1 = new Model(item.getKey(), actname);
                        mDatabase.child(userId).setValue(m1);
                        Toast.makeText(context, "Succesfully Send", Toast.LENGTH_SHORT).show();

                        int seen = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(item.getKey()).child("receiveFrom");
                        final String user_Id = mDatabase.push().getKey();
                        long time = new Date().getTime();
                        Model3 m = new Model3(user, actname, seen, time);
                        mDatabase.child(user_Id).setValue(m);
                        mDatabase = FirebaseDatabase.getInstance().getReference("reqnoti");
                        final String user__Id = mDatabase.push().getKey();
                        final String to = item.getKey();
                        final String from = user;
                        Model2 m2 = new Model2(to, from, actname);
                        mDatabase.child(user__Id).setValue(m2);

                        request.setText("Already Send");
                        send.add(item.getKey());
                        request.setEnabled(false);
                        notifyDataSetChanged();
                    }

                });

            }
        }
       else if(!user.equals(admin)&&!user.equals(item.getKey())){
            request.setVisibility(View.VISIBLE);
            kick.setVisibility(View.INVISIBLE);
            if (receive.contains(item.getKey())) {
                request.setText("Already Receive");
                request.setEnabled(false);
            } else if (send.contains(item.getKey())) {
                request.setText("Already Send");
                request.setEnabled(false);
            } else {
                request.setEnabled(true);
                request.setText("Send");
                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("uiio", "connect");

                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("sendTo");
                        final String userId = mDatabase.push().getKey();
                        Model m1 = new Model(item.getKey(), actname);
                        mDatabase.child(userId).setValue(m1);
                        Toast.makeText(context, "Succesfully Send", Toast.LENGTH_SHORT).show();

                        int seen = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(item.getKey()).child("receiveFrom");
                        final String user_Id = mDatabase.push().getKey();
                        long time = new Date().getTime();
                        Model3 m = new Model3(user, actname, seen, time);
                        mDatabase.child(user_Id).setValue(m);
                        mDatabase = FirebaseDatabase.getInstance().getReference("reqnoti");
                        final String user__Id = mDatabase.push().getKey();
                        final String to = item.getKey();
                        final String from = user;
                        Model2 m2 = new Model2(to, from, actname);
                        mDatabase.child(user__Id).setValue(m2);

                        request.setText("Already Send");
                        send.add(item.getKey());
                        request.setEnabled(false);
                        notifyDataSetChanged();
                    }

                });

            }
        }


        return result;
        }


}