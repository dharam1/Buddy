package com.example.dharmendra.buddy1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class HashMapAdapter extends BaseAdapter {
    private final ArrayList mData;
    ArrayList addresslist;
    private SparseBooleanArray mSelectedItemsIds;
    ImageButton manageuser;
    CardView cardview;
    Activity act;
    Context context;
    ListView listView;
    HashMapAdapter adapter;
    DatabaseReference mDatabase;
    LinkedHashMap<Integer,String> map=new LinkedHashMap<>();
    FragmentManager fragmentManager;

    public HashMapAdapter(LinkedHashMap<Integer, String> map, LinkedHashMap<Integer, String> map1, Activity act, Context context,ListView listView,FragmentManager fragmentManager) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        Collections.reverse(mData);
        addresslist=new ArrayList();
        for ( Map.Entry<Integer, String> entry : map1.entrySet()) {
            addresslist.add(entry.getValue());
        }
        Collections.reverse(addresslist);
        mSelectedItemsIds = new  SparseBooleanArray();
        this.act=act;
        this.context=context;
        this.listView=listView;
        this.map=map;
        this.fragmentManager=fragmentManager;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public LinkedHashMap.Entry<Integer, String> getItem(int position) {
        return (LinkedHashMap.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final View result;
        String address=addresslist.get(position).toString();
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view_delete, parent, false);
        } else {
            result = convertView;
        }
        Log.d("POl__KL1","POl__KL1");
        cardview=(CardView)result.findViewById(R.id.card_view);
        final LinkedHashMap.Entry<Integer, String> item = getItem(position);
        manageuser=(ImageButton)result.findViewById(R.id.manageuser);
        manageuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int aid=item.getKey();
                Log.d("tyu",String.valueOf(aid));
                /**PopupMenu popup = new PopupMenu(v.getContext(),v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.multiple_delete, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position,aid,act,context,fragmentManager));
                popup.show();**/
                Intent i = new Intent(act, manageuser.class);
                i.putExtra("int_key", aid);
                act.startActivity(i);
                act.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

            }
        });
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int aid=item.getKey();
                Intent i = new Intent(act, Chat.class);
                i.putExtra("int_key", aid);
                act.startActivity(i);
                act.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });



        // TODO replace findViewById by ViewHolder
       //((TextView) result.findViewById(R.id.textView)).setText(String.valueOf(item.getKey()));

        ((TextView) result.findViewById(R.id.textView)).setText(item.getValue());
        ((TextView) result.findViewById(R.id.textView2)).setText(address);

        return result;
    }
    public void  toggleSelection(int position, ListView listView) {
        selectView(position, !mSelectedItemsIds.get(position),listView);
    }
    public void selectView(int position, boolean value,ListView listView) {
        if (value) {
            mSelectedItemsIds.put(position, value);
            Log.d("dfghj", "1");
            //change color
            listView.getChildAt(position).setBackgroundColor(Color.parseColor("#8CFFFFFF"));
        }
        else {
            mSelectedItemsIds.delete(position);
            Log.d("dfghj","2");
            listView.getChildAt(position).setBackgroundColor(Color.WHITE);
        }
        notifyDataSetChanged();
    }
    public void  removeSelection() {
        mSelectedItemsIds = new  SparseBooleanArray();
        notifyDataSetChanged();
    }
    public  SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

   }
class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

    private int position;
    int aid;
    Activity act;
    DatabaseReference mDatabase;
    Context context;
    FragmentManager fragmentManager;
    public MyMenuItemClickListener(int positon,int aid,Activity act,Context context,FragmentManager fragmentManager) {
        this.position=positon;
        this.aid=aid;
        this.act=act;
        this.context=context;
        this.fragmentManager=fragmentManager;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.manageuser:
                Intent i = new Intent(act, manageuser.class);
                i.putExtra("int_key", aid);
                act.startActivity(i);
                act.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                return true;
            case R.id.delete:
                if(haveNetworkConnection()){
                    mDatabase = FirebaseDatabase.getInstance().getReference("activity");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Activity1 post1 = postSnapshot.getValue(Activity1.class);
                                int ccid=post1.getCcid();
                                if(ccid==aid){
                                    postSnapshot.getRef().child("status").setValue(0);
                                    Toast.makeText(context, "Succesfully Deleted", Toast.LENGTH_SHORT).show();
                                    fragmentManager .beginTransaction().replace(R.id.fourth, new FourthFragment()).commit();
                                    break;
                                }

                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context, "\"The read failed: \"" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                }
                Log.d("lopp",String.valueOf(aid));

            default:
        }
        return false;
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