package com.example.dharmendra.buddy1;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class Manage_user_adapter1 extends BaseAdapter {
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
    ArrayList<String> receive=new ArrayList<>();
    ArrayList<String> send=new ArrayList<>();
    ArrayList<String> connection=new ArrayList<>();
    String actname;

    public Manage_user_adapter1(LinkedHashMap<String, String> map , int aid, ArrayList list,ArrayList receive,ArrayList send,ArrayList connection,String actname,Context context) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
       //Collections.reverse(mData);
        this.aid=aid;
        this.list=list;
        this.receive=receive;
        this.send=send;
        this.connection=connection;
        this.context=context;
        this.actname=actname;
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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view_manage1, parent, false);
        } else {
            result = convertView;
        }
        cardView = (CardView) result.findViewById(R.id.card_view);
        final LinkedHashMap.Entry<String, String> item = getItem(position);
        //if(!item.getKey().equals(user))
        ((TextView) result.findViewById(R.id.textView)).setText(item.getValue());
        kick = (TextView) result.findViewById(R.id.button);

        if(receive.contains(item.getKey())){
            kick.setText("Already Receive");
            kick.setEnabled(false);
        }
        else if(send.contains(item.getKey())){
            kick.setText("Already Send");
            kick.setEnabled(false);
        }
        else{
            kick.setEnabled(true);
            kick.setText("Send");
            kick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("uiio","connect");

                    mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user).child("sendTo");
                    final String userId = mDatabase.push().getKey();
                    Model m1 = new Model(item.getKey(),actname);
                    mDatabase.child(userId).setValue(m1);
                    Toast.makeText(context, "Succesfully Send", Toast.LENGTH_SHORT).show();

                    int seen=0;
                    mDatabase=FirebaseDatabase.getInstance().getReference("users").child(item.getKey()).child("receiveFrom");
                    final String user_Id = mDatabase.push().getKey();
                    long time = new Date().getTime();
                    Model3 m = new Model3(user,actname,seen,time);
                    mDatabase.child(user_Id).setValue(m);
                    mDatabase=FirebaseDatabase.getInstance().getReference("reqnoti");
                    final String user__Id = mDatabase.push().getKey();
                    final String to=item.getKey();
                    final String from =user;
                    Model2 m2 = new Model2(to,from,actname);
                    mDatabase.child(user__Id).setValue(m2);

                    kick.setText("Already Send");
                    send.add(item.getKey());
                    kick.setEnabled(false);
                    notifyDataSetChanged();
                }

            });

        }







            return result;
        }


}