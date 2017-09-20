package com.example.dharmendra.buddy1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import static java.security.AccessController.getContext;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class HashMapAdapter1 extends BaseAdapter {
    private final ArrayList mData;
    ArrayList activityname;
    ArrayList urllist;
    Context context;
    ArrayList timelist;
    CardView cardView;
    Button accept;
    Button reject;
    int c_count;
    DatabaseReference mDatabase;
    String user;
    Activity act;
    FragmentManager fragmentManager;

    public HashMapAdapter1(LinkedHashMap<String, String> map, ArrayList<String> activityname1, LinkedHashMap<String, String> map1, ArrayList<String>timelist1, Context context, FragmentManager fragmentManager) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        Collections.reverse(mData);
        activityname=new ArrayList();
        activityname.addAll(activityname1);
        Collections.reverse(activityname);
        urllist=new ArrayList();
        for ( Map.Entry<String, String> entry : map1.entrySet()) {
            urllist.add(entry.getValue());
        }
        Collections.reverse(urllist);
        timelist=new ArrayList<>();
        this.timelist=timelist1;
        Collections.reverse(timelist);
        this.context=context;
        user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.act=act;
        this.fragmentManager=fragmentManager;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view1, parent, false);
        } else {
            result = convertView;
        }

        final LinkedHashMap.Entry<String, String> item = getItem(position);
        String url=urllist.get(position).toString();
        String time=timelist.get(position).toString();
        String actname=activityname.get(position).toString();
        cardView =(CardView) result.findViewById(R.id.card_view);
        accept=(Button) result.findViewById(R.id.accept);
        reject=(Button) result.findViewById(R.id.reject);
        ImageView imgv=(ImageView) result.findViewById(R.id.imageview);
        Picasso.with(context).load(url).fit().centerCrop().into(imgv);
        // TODO replace findViewById by ViewHolder
        //((TextView) result.findViewById(R.id.textView)).setText(String.valueOf(item.getKey()));
        ((TextView) result.findViewById(R.id.textView)).setText(item.getValue()+" @ "+actname+" send You Request.");

        TextView timev=(TextView)result.findViewById(R.id.textView2);
        long t=Long.parseLong(time);
        String date=DateFormat.format("dd/MM/yyyy", t).toString();
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
                timev.setText("Today at "+f2.format(d).toUpperCase());
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
                timev.setText("Yesterday at "+f2.format(d).toUpperCase());
            }catch (Exception e){

            }

        }

        else {
            String date1=DateFormat.format("dd/MM/yyyy",t).toString();
            String nontime= DateFormat.format("HH:mm",t).toString();
            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
            try{
                Date d = f1.parse(nontime);
                SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                timev.setText(date1+" at "+f2.format(d).toUpperCase());
            }
            catch (Exception e){

            }
        }
        //timev.setText(DateFormat.format("dd-MM-yyyy (HH:mm)", t));

       accept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                final String content=item.getKey();
               if(haveNetworkConnection()){
                   c_count=0;
                   mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");
                   mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                               c_count = c_count + 1;
                               Log.d("kumree",String.valueOf(c_count));
                           }
                           if(c_count>30){
                               Toast.makeText(context, "Delete non recent connection (max allow 30)", Toast.LENGTH_SHORT).show();
                           }else{
                               mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("receiveFrom");
                               mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                       for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                           Model m = postSnapshot.getValue(Model.class);
                                           String user1=m.getUser();
                                           if(user1.equals(content)){
                                               postSnapshot.getRef().removeValue();
                                               mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");
                                               final String userId = mDatabase.push().getKey();
                                               connection_type con=new connection_type(user1,"Buddy");
                                               mDatabase.child(user1).setValue(con);
                                               Toast.makeText(context, "Accepted ", Toast.LENGTH_SHORT).show();
                                               //FragmentTransaction fragmentTransaction = act.getFragmentManager().beginTransaction();
                                              //Fragment fragment=new ThirdFragment();
                                               fragmentManager .beginTransaction().replace(R.id.third, new ThirdFragment()).commit();
                                               break;
                                           }
                                       }
                                   }

                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {

                                   }
                               });
                               mDatabase = FirebaseDatabase.getInstance().getReference("users").child(content).child("sendTo");
                               mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                       for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                           Model m = postSnapshot.getValue(Model.class);
                                           String user1=m.getUser();
                                           String activityname=m.getActivityname();
                                           if(user1.equals(user)){
                                               postSnapshot.getRef().removeValue();
                                               mDatabase = FirebaseDatabase.getInstance().getReference("users").child(content).child("Noti");
                                               int seen=0;
                                               final String userId = mDatabase.push().getKey();
                                               long time = new Date().getTime();
                                               Model3 m1=new Model3(user,activityname,seen,time);
                                               mDatabase.child(userId).setValue(m1);
                                               mDatabase = FirebaseDatabase.getInstance().getReference("users").child(content).child("connection");
                                               final String user_Id = mDatabase.push().getKey();
                                               connection_type con=new connection_type(user,"Buddy");
                                               mDatabase.child(user).setValue(con);
                                               break;
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

               }
               else{
                   Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
               }

           }
       });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content=item.getKey();
                if(haveNetworkConnection()){
                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("receiveFrom");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Model m = postSnapshot.getValue(Model.class);
                                final String user1=m.getUser();
                                if (user1.equals(content)) {
                                    postSnapshot.getRef().removeValue();
                                    Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show();
                                    fragmentManager .beginTransaction().replace(R.id.third, new ThirdFragment()).commit();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(content).child("sendTo");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Model m = postSnapshot.getValue(Model.class);
                                final String user1=m.getUser();
                                if (user1.equals(user)) {
                                    postSnapshot.getRef().removeValue();
                                    break;
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
        return result;
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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