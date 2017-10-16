package com.example.dharmendra.buddy1;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
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

import static com.example.dharmendra.buddy1.R.id.map;
import static com.example.dharmendra.buddy1.R.id.place_autocomplete_powered_by_google;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class timelineadapter extends BaseAdapter {
    private final ArrayList mData;
    ArrayList followedactivityname;
    ArrayList<TimeLineClass> followeduser;
    ArrayList followeddate;
    Context context;
    ArrayList timelist;
    CardView cardView;
    FragmentManager fragmentManager;
    DatabaseReference mDatabase;
    String user;
    Activity act;
    Bundle b;
    GoogleMap mMap;
    Double lat,longi;
    int aid;
    String type1;
    int count = 0;

    public timelineadapter(LinkedHashMap<Integer,Integer> map, ArrayList<TimeLineClass> list,Context context/**, LinkedHashMap<String, String> map2, LinkedHashMap<String, Long> map3**/, Activity act, FragmentManager fragmentManager,Bundle b) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());


        followeduser=new ArrayList();
        followeduser=list;
        this.act=act;
        this.context=context;
        this.fragmentManager=fragmentManager;
        this.b=b;

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
        MapView mapView;



        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_list_view, parent, false);
        } else {
            result = convertView;
        }


        final LinkedHashMap.Entry<Integer, Integer> item = getItem(position);
        final TextView t=(TextView)result.findViewById(R.id.textView);
        final TextView t2=(TextView)result.findViewById(R.id.textView1);
        final ImageView iv = (ImageView) result.findViewById(R.id.imageview);
        cardView=(CardView)result.findViewById(R.id.card_view);
        final TextView dot = (TextView) result.findViewById(R.id.tv_dot);
        final ImageView type = (ImageView) result.findViewById(R.id.type);
        final ImageView mapview=(ImageView)result.findViewById(R.id.map);
        final View line = (View) result.findViewById(R.id.vertical_bar);
        final String user11=followeduser.get(position).getName().toString();
        String[] split=user11.split("-");
        final String user1=split[1];
        final String user2=split[0];

        Resources r = context.getResources();
        final int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics()
        );

        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int x;
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                x = iv.getMeasuredWidth();
                iv.setLayoutParams(new RelativeLayout.LayoutParams(x,x));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                params.setMargins(0, px, 0, 0);
                return true;
            }
        });

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) line.getLayoutParams();
        Log.d("POSITION", String.valueOf(position));
        if(position == 0 && position == getCount() - 1 - count ){
            params.setMargins(0, px, 0, px);
        }
        else if(position == 0){
            Log.d("POSITION", "INSIDE 1");
            params.setMargins(0, px, 0, 0);
        }
        else if(position == getCount() - 1 - count){
            Log.d("POSITION", "INSIDE 2");
            params.setMargins(0, 0, 0, px);
        }

        line.setLayoutParams(params);

        if(user2.equals("Anonymous")){
             aid = followeduser.get(position).getAid();
            String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig().width(60).height(60).endConfig()
                    .buildRound("AN", Color.RED);
            Log.d("POPIKM",drawable.toString());
            iv.setImageDrawable(drawable);
            //Picasso.with(context).load(drawable).fit().centerCrop().into(iv);
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("activity").child(user1).child(String.valueOf(aid)).child("time");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        long t = Long.valueOf(dataSnapshot.getValue().toString());
                        String date = DateFormat.format("dd/MM/yyyy", t).toString();
                        long c_date = new Date().getTime();
                        String format = DateFormat.format("dd/MM/yyyy", c_date).toString();
                        final Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -1);
                        String yest = DateFormat.format("dd/MM/yyyy", cal.getTime()).toString();
                        if (date.equals(format)) {
                            String nontime = DateFormat.format("HH:mm", t).toString();
                            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                            try {
                                Date d = f1.parse(nontime);
                                SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                                t2.setText("Today at " + f2.format(d).toUpperCase());
                            } catch (Exception e) {

                            }


                        } else if (date.equals(yest)) {
                            String nontime = DateFormat.format("HH:mm", t).toString();
                            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                            try {
                                Date d = f1.parse(nontime);
                                SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                                t2.setText("Yesterday at " + f2.format(d).toUpperCase());
                            } catch (Exception e) {

                            }

                        } else {
                            String date1 = DateFormat.format("d MMMM", t).toString();
                            String nontime = DateFormat.format("HH:mm", t).toString();
                            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                            try {
                                Date d = f1.parse(nontime);
                                SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                                t2.setText(date1);
                            } catch (Exception e) {

                            }
                        }
                        //timev.setText(DateFormat.format("dd


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mDatabase = FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(aid));
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                        int status = post1.getStatus();
                        lat=post1.getLatitude();
                        longi=post1.getLongitude();
                        String url=post1.getMapurl();
                        String address = post1.getAddress();
                        String[] address_lines = address.split(",");
                        int length = address_lines.length;
                        /**if(address_lines.length>5)
                            t.setText(Html.fromHtml("A new group <b>" + post1.getName() + "</b> has been created at <b> "+ address_lines[length - 4] + ", " + address_lines[length - 3]+", " + address_lines[length - 2]+", " + address_lines[length - 1]));
                        else**/
                            t.setText(Html.fromHtml("A new group <b>" + post1.getName() + "</b> has been created<b> "));
                        Picasso.with(context).load(url).fit().centerCrop().into(mapview);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            type.setImageResource(R.drawable.ic_global);
            dot.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            type.setVisibility(View.VISIBLE);

        }
        else {

            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user1);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user use = dataSnapshot.getValue(user.class);
                    final String name = use.getName();
                    String url = use.getUrl();
                    Log.d("IMAGE", url);
                    Picasso.with(context).load(url).fit().centerCrop().noFade().into(iv);

                    aid = followeduser.get(position).getAid();
                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("activity").child(user1).child(String.valueOf(aid));
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                user_activity use=dataSnapshot.getValue(user_activity.class);
                                type1=use.getType();
                                long t = use.getTime();
                                String date = DateFormat.format("dd/MM/yyyy", t).toString();
                                long c_date = new Date().getTime();
                                String format = DateFormat.format("dd/MM/yyyy", c_date).toString();
                                final Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, -1);
                                String yest = DateFormat.format("dd/MM/yyyy", cal.getTime()).toString();
                                if (date.equals(format)) {
                                    String nontime = DateFormat.format("HH:mm", t).toString();
                                    SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                                    try {
                                        Date d = f1.parse(nontime);
                                        SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                                        t2.setText("Today at " + f2.format(d).toUpperCase());
                                    } catch (Exception e) {

                                    }


                                } else if (date.equals(yest)) {
                                    String nontime = DateFormat.format("HH:mm", t).toString();
                                    SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                                    try {
                                        Date d = f1.parse(nontime);
                                        SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                                        t2.setText("Yesterday at " + f2.format(d).toUpperCase());
                                    } catch (Exception e) {

                                    }

                                } else {
                                    String date1 = DateFormat.format("d MMMM", t).toString();
                                    String nontime = DateFormat.format("HH:mm", t).toString();
                                    SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                                    try {
                                        Date d = f1.parse(nontime);
                                        SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                                        t2.setText(date1);
                                    } catch (Exception e) {

                                    }
                                }
                                //timev.setText(DateFormat.format("dd


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mDatabase = FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(aid));
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                                int status = post1.getStatus();
                                lat=post1.getLatitude();
                                longi=post1.getLongitude();
                                String url=post1.getMapurl();
                                String address = post1.getAddress();
                                String[] address_lines = address.split(",");
                                int length = address_lines.length;
                                //if (status == 1)

                                    if(type1.equals("Created")){
                                        /**if(address_lines.length>5)
                                            t.setText(Html.fromHtml("<b>" + name + "</b> created <b>" + post1.getName() + "</b> at " + address_lines[length - 4] + ", " + address_lines[length - 3]+", " + address_lines[length - 2]+", " + address_lines[length - 1]));
                                        else**/
                                            t.setText(Html.fromHtml("<b>" + name + "</b> created group <b>" + post1.getName()));
                                        Picasso.with(context).load(url).fit().centerCrop().into(mapview);
                                        dot.setVisibility(View.VISIBLE);
                                        line.setVisibility(View.VISIBLE);
                                        type.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        t.setText(Html.fromHtml("<b>" + name + "</b> has started following <b>" + post1.getName() + "</b>"));
                                        mapview.setVisibility(View.GONE);
                                        dot.setVisibility(View.VISIBLE);
                                        line.setVisibility(View.VISIBLE);
                                        type.setVisibility(View.VISIBLE);
                                    }

                                    if(post1.getType() == 0)
                                        type.setImageResource(R.drawable.ic_global);
                                    else
                                        type.setImageResource(R.drawable.ic_private);
                                /**} else {
                                    cardView.setVisibility(View.GONE);
                                    count++;
                                }**/
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int aid=followeduser.get(position).getAid();
            Intent i = new Intent(act, Chat.class);
            i.putExtra("int_key", aid);
            act.startActivity(i);
            act.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

        }
    });




        return result;
    }

}