package com.example.dharmendra.buddy1;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class timelineadapter extends BaseAdapter {
    private final ArrayList mData;
    ArrayList followedactivityname;
    ArrayList followeduser;
    ArrayList followeddate;
    Context context;
    ArrayList timelist;
    CardView cardView;

    DatabaseReference mDatabase;
    String user;
    String name;
    Activity act;


    public timelineadapter(LinkedHashMap<Integer,Integer> map, LinkedHashMap<Integer,String> map1,Context context/**, LinkedHashMap<String, String> map2, LinkedHashMap<String, Long> map3**/,Activity act) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        Collections.reverse(mData);
        followeduser=new ArrayList();
        for ( Map.Entry<Integer, String> entry : map1.entrySet()) {
            followeduser.add(entry.getValue());
        }
        Collections.reverse(followeduser);
        this.act=act;
        this.context=context;
        /**followedactivityname=new ArrayList();
        for ( Map.Entry<String, String> entry : map2.entrySet()) {
            followedactivityname.add(entry.getValue());
        }
        Collections.reverse(followedactivityname);
        followeddate=new ArrayList();
        for ( Map.Entry<String, Long> entry : map3.entrySet()) {
            followeddate.add(entry.getValue());
        }
        Collections.reverse(followeddate);**/

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public LinkedHashMap.Entry<Integer, Integer> getItem(int position) {
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

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_list_view, parent, false);
        } else {
            result = convertView;
        }

        final LinkedHashMap.Entry<Integer, Integer> item = getItem(position);
        final TextView t=(TextView)result.findViewById(R.id.textView);
        final TextView t2=(TextView)result.findViewById(R.id.textView1);
        final CircleImageView iv = (CircleImageView) result.findViewById(R.id.imageview);
        cardView=(CardView)result.findViewById(R.id.card_view);
        View line = (View) result.findViewById(R.id.vertical_bar);
        String user1=followeduser.get(position).toString();

        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics()
        );
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) line.getLayoutParams();

        if(position == 0){
            params.setMargins(0, px, 0, 0);
        }
        else if(position == getCount() - 1){
            params.setMargins(0, 0, 0, px);
        }
        else{
            params.setMargins(0, 0, 0, 0);
        }

        line.setLayoutParams(params);

        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user1).child("url");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue().toString();
                Log.d("IMAGE", url);
                Picasso.with(context).load(url).fit().centerCrop().into(iv);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user1).child("name");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = dataSnapshot.getValue().toString();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        int aid = item.getValue();
        String user=FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d("POPkl",name+"");
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("activity").child(String.valueOf(aid)).child("time");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long t=Long.valueOf(dataSnapshot.getValue().toString());
                String date = DateFormat.format("dd/MM/yyyy", t).toString();
                long c_date=new Date().getTime();
                String format=DateFormat.format("dd/MM/yyyy", c_date).toString();
                final Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                String yest=DateFormat.format("dd/MM/yyyy",cal.getTime()).toString();
                if(date.equals(format)){
                    String nontime= DateFormat.format("HH:mm",t).toString();
                    SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                    try{
                        Date d = f1.parse(nontime);
                        SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                        t2.setText("Today at "+f2.format(d).toUpperCase());
                    }
                    catch (Exception e){

                    }


                }
                else if(date.equals(yest)){
                    String nontime= DateFormat.format("HH:mm",t).toString();
                    SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                    try{
                        Date d = f1.parse(nontime);
                        SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                        t2.setText("Yesterday at "+f2.format(d).toUpperCase());
                    }catch (Exception e){

                    }

                }

                else {
                    String date1=DateFormat.format("d MMMM",t).toString();
                    String nontime= DateFormat.format("HH:mm",t).toString();
                    SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                    try{
                        Date d = f1.parse(nontime);
                        SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                        t2.setText(date1);
                    }
                    catch (Exception e){

                    }
                }
                //timev.setText(DateFormat.format("dd


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(aid));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                int status = post1.getStatus();

                if(status==1) {
                    t.setText(Html.fromHtml(name + " has started following activity <b>" + post1.getName() + "</b>"));
                }
                else{
                   cardView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int aid=item.getValue();
            Intent i = new Intent(act, Chat.class);
            i.putExtra("int_key", aid);
            act.startActivity(i);
            act.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

        }
    });




        return result;
    }
}