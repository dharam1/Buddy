package com.example.dharmendra.buddy1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
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
    ImageButton kick;
    ImageButton request;
    int aid;
    DatabaseReference mDatabase;
    ArrayList<String> list=new ArrayList<>();
    ArrayList<String> receive=new ArrayList<>();
    ArrayList<String> send=new ArrayList<>();
    ArrayList<String> connection=new ArrayList<>();
    String admin,actname;
    LinkedHashMap<String,String> connectionlist;

    public Manage_user_adapter(LinkedHashMap<String, String> map ,int aid,ArrayList list,String admin,ArrayList send,ArrayList receive,ArrayList connection,String actname,LinkedHashMap<String,String> connectionlist1,Context context) {
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
        connectionlist=new LinkedHashMap<>();
        connectionlist=connectionlist1;
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
        kick = (ImageButton) result.findViewById(R.id.button);
        request = (ImageButton) result.findViewById(R.id.button1);
        cardView = (CardView) result.findViewById(R.id.card_view);
        final LinkedHashMap.Entry<String, String> item = getItem(position);
        //if(!item.getKey().equals(user))
        /**if(connection.contains(item.getKey())){
            mDatabase=FirebaseDatabase.getInstance().getReference("users").child(item.getKey()).child("name");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((TextView) result.findViewById(R.id.textView)).setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{**/
            if(connectionlist.containsKey(item.getKey())){
                ((TextView) result.findViewById(R.id.textView)).setText(connectionlist.get(item.getKey()));
                Log.d("LOLPP",connectionlist.get(item.getKey()));
            }
            else {
                ((TextView) result.findViewById(R.id.textView)).setText(item.getValue());
                Log.d("LOLPP",item.getKey());
            }
        //}

        Log.d("grp info", user + " " + admin + " " + item.getKey());
        if(user.equals(item.getKey())){
            if(user.equals(admin)){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) kick.getLayoutParams();
                params.weight = 0.0f;
                kick.setLayoutParams(params);

                params = (LinearLayout.LayoutParams) request.getLayoutParams();
                params.weight = 0.0f;
                request.setLayoutParams(params);

                GradientDrawable drawable = (GradientDrawable) ((TextView) result.findViewById(R.id.tv_admin)).getBackground();
                drawable.setStroke(2, Color.parseColor("#63E722"));
                ((TextView) result.findViewById(R.id.tv_admin)).setBackgroundResource(R.drawable.card_edge);
                params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.tv_admin)).getLayoutParams();
                params.weight = 1.0f;
                ((TextView) result.findViewById(R.id.tv_admin)).setLayoutParams(params);
                ((TextView) result.findViewById(R.id.tv_admin)).setVisibility(View.VISIBLE);

                drawable = (GradientDrawable) ((TextView) result.findViewById(R.id.tv_you)).getBackground();
                drawable.setStroke(2, Color.parseColor("#ff7d0c"));
                ((TextView) result.findViewById(R.id.tv_you)).setBackgroundResource(R.drawable.card_edge);
                params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.tv_you)).getLayoutParams();
                params.weight = 1.0f;
                ((TextView) result.findViewById(R.id.tv_you)).setLayoutParams(params);
                ((TextView) result.findViewById(R.id.tv_you)).setVisibility(View.VISIBLE);

                params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.textView)).getLayoutParams();
                params.weight = 3.0f;
                ((TextView) result.findViewById(R.id.textView)).setLayoutParams(params);
            }
            else{
                GradientDrawable drawable = (GradientDrawable) ((TextView) result.findViewById(R.id.tv_you)).getBackground();
                drawable.setStroke(2, Color.parseColor("#ff7d0c"));
                ((TextView) result.findViewById(R.id.tv_you)).setBackgroundResource(R.drawable.card_edge);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.tv_you)).getLayoutParams();
                params.weight = 1.0f;
                ((TextView) result.findViewById(R.id.tv_you)).setLayoutParams(params);
                ((TextView) result.findViewById(R.id.tv_you)).setVisibility(View.VISIBLE);

                params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.textView)).getLayoutParams();
                params.weight = 4.0f;
                ((TextView) result.findViewById(R.id.textView)).setLayoutParams(params);

                ((TextView) result.findViewById(R.id.tv_admin)).setVisibility(View.GONE);
            }
            kick.setVisibility(View.GONE);
            request.setVisibility(View.GONE);
        }
        else if(user.equals(admin) && !user.equals(item.getKey())){

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) kick.getLayoutParams();
            params.weight = 0.6f;
            kick.setLayoutParams(params);
            kick.setVisibility(View.VISIBLE);

            params = (LinearLayout.LayoutParams) request.getLayoutParams();
            params.weight = 0.6f;
            request.setLayoutParams(params);
            request.setVisibility(View.VISIBLE);

            params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.tv_admin)).getLayoutParams();
            params.weight = 0.0f;
            ((TextView) result.findViewById(R.id.tv_admin)).setLayoutParams(params);
            ((TextView) result.findViewById(R.id.tv_admin)).setVisibility(View.GONE);

            ((TextView) result.findViewById(R.id.tv_you)).setVisibility(View.GONE);

            params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.textView)).getLayoutParams();
            params.weight = 3.8f;
            ((TextView) result.findViewById(R.id.textView)).setLayoutParams(params);

            if (list.contains(item.getKey())/**&&!item.getKey().equals(user)**/) {
                kick.setImageResource(R.drawable.ic_ban);
                kick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageButton b = (ImageButton) v.findViewById(R.id.button);
                        b.setImageResource(R.drawable.ic_unban);
                        Toast.makeText(context, "User " + item.getValue() + " has been unbanned", Toast.LENGTH_SHORT).show();
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
                kick.setImageResource(R.drawable.ic_unban);
                kick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageButton b = (ImageButton) v.findViewById(R.id.button);
                        kick.setImageResource(R.drawable.ic_ban);
                        Toast.makeText(context, "User has been banned", Toast.LENGTH_SHORT).show();
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
                request.setImageResource(R.drawable.ic_wait);
                request.setClickable(false);
            } else if (send.contains(item.getKey())) {
                request.setImageResource(R.drawable.ic_wait);
                request.setClickable(false);
            } else if(connection.contains(item.getKey())){
                request.setVisibility(View.GONE);
                request.setClickable(false);
                params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.textView)).getLayoutParams();
                params.weight = 4.4f;
                ((TextView) result.findViewById(R.id.textView)).setLayoutParams(params);
            }
            else {
                request.setClickable(true);
                request.setImageResource(R.drawable.ic_send);
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

                        request.setImageResource(R.drawable.ic_alreadysent);
                        send.add(item.getKey());
                        receive.add(user);
                        request.setClickable(false);
                        notifyDataSetChanged();
                    }

                });

            }
        }
       else if(!user.equals(admin)&&!user.equals(item.getKey())){


            if(!admin.equals(item.getKey())){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) kick.getLayoutParams();
                params.weight = 0.0f;
                kick.setLayoutParams(params);
                kick.setVisibility(View.GONE);

                params = (LinearLayout.LayoutParams) request.getLayoutParams();
                params.weight = 0.6f;
                request.setLayoutParams(params);
                request.setVisibility(View.VISIBLE);

                params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.textView)).getLayoutParams();
                params.weight = 4.4f;
                ((TextView) result.findViewById(R.id.textView)).setLayoutParams(params);

                ((TextView) result.findViewById(R.id.tv_admin)).setVisibility(View.GONE);

                ((TextView) result.findViewById(R.id.tv_you)).setVisibility(View.GONE);
            }
            else{
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) kick.getLayoutParams();
                params.weight = 0.0f;
                kick.setLayoutParams(params);
                kick.setVisibility(View.GONE);

                params = (LinearLayout.LayoutParams) request.getLayoutParams();
                params.weight = 0.6f;
                request.setLayoutParams(params);
                request.setVisibility(View.VISIBLE);

                GradientDrawable drawable = (GradientDrawable) ((TextView) result.findViewById(R.id.tv_admin)).getBackground();
                drawable.setStroke(2, Color.parseColor("#63E722"));
                ((TextView) result.findViewById(R.id.tv_admin)).setBackgroundResource(R.drawable.card_edge);
                params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.tv_admin)).getLayoutParams();
                params.weight = 1.0f;
                ((TextView) result.findViewById(R.id.tv_admin)).setLayoutParams(params);
                ((TextView) result.findViewById(R.id.tv_admin)).setVisibility(View.VISIBLE);

                params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.textView)).getLayoutParams();
                params.weight = 3.4f;
                ((TextView) result.findViewById(R.id.textView)).setLayoutParams(params);

                ((TextView) result.findViewById(R.id.tv_you)).setVisibility(View.GONE);
            }

            if (receive.contains(item.getKey())) {
                request.setImageResource(R.drawable.ic_wait);
                request.setClickable(false);
            } else if (send.contains(item.getKey())) {
                request.setImageResource(R.drawable.ic_wait);
                request.setClickable(false);
            } else if(connection.contains(item.getKey())){
                request.setVisibility(View.GONE);
                request.setClickable(false);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((TextView) result.findViewById(R.id.textView)).getLayoutParams();
                params.weight = 4.0f;
                ((TextView) result.findViewById(R.id.textView)).setLayoutParams(params);
            }
            else {
                request.setClickable(true);
                request.setImageResource(R.drawable.ic_send);
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

                        request.setImageResource(R.drawable.ic_wait);
                        send.add(item.getKey());
                        receive.add(user);
                        request.setClickable(false);
                        notifyDataSetChanged();
                    }

                });

            }
        }


        return result;
        }


}