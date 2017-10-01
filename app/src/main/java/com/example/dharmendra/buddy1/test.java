package com.example.dharmendra.buddy1;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class test extends BaseAdapter {
    private final ArrayList mData;
    Chat activity;
    LinkedHashMap<String,ArrayList<String>> map1=new LinkedHashMap<>();
    LinkedHashMap<String,ArrayList<String>> map2=new LinkedHashMap<>();

    public test(LinkedHashMap<Integer,String > map,LinkedHashMap<String,ArrayList<String>> map1,LinkedHashMap<String,ArrayList<String>> map2,Chat activity) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        this.activity=activity;
        this.map1=map1;
        this.map2=map2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public LinkedHashMap.Entry<Integer,String> getItem(int position) {
        return (LinkedHashMap.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final LinkedHashMap.Entry<Integer,String> item = getItem(position);
        String d=item.getValue();
        ArrayList<String> value=map2.get(d);
        ArrayList<String > value1=map1.get(d);
        int pos=0;
        View result=null;
        Log.d("ZXCVB","Divide");
        for(String s:value){
            Log.d("ZXCVB",s+"  "+value1.get(pos));

            if (s.equals(user))
                result = activity.getLayoutInflater().inflate(R.layout.item_out_message,parent, false);
            else
                result = activity.getLayoutInflater().inflate(R.layout.item_in_message,parent, false);

            TextView messageText = (TextView) result.findViewById(R.id.message_text);
            TextView messageUser = (TextView) result.findViewById(R.id.message_user);
            TextView messageTime = (TextView) result.findViewById(R.id.message_time);

            messageText.setText(value1.get(pos));
            pos++;

        }
        return result;
    }

}