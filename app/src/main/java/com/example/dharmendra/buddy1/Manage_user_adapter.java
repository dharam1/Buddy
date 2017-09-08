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
    TextView kick,unkick;
    int aid;
    DatabaseReference mDatabase;
    ArrayList<String> list=new ArrayList<>();
    String admin;

    public Manage_user_adapter(LinkedHashMap<String, String> map ,int aid,ArrayList list,String admin) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
       //Collections.reverse(mData);
        this.aid=aid;
        this.list=list;
        this.admin=admin;
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
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view_manage, parent, false);
        } else {
            result = convertView;
        }
        kick = (TextView) result.findViewById(R.id.button);
        cardView = (CardView) result.findViewById(R.id.card_view);
        final LinkedHashMap.Entry<String, String> item = getItem(position);
        //if(!item.getKey().equals(user))
        ((TextView) result.findViewById(R.id.textView)).setText(item.getValue());
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
            /**else{
             kick.setText("Group Admin");
             //notifyDataSetChanged();
             }**/


            return result;
        }


}