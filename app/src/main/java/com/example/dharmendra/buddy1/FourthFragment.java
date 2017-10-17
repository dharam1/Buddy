package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 12-06-2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class FourthFragment extends Fragment {
    String user,user_login;
    int count;
    DatabaseReference mDatabase,mDatabase1;
    ListView simpleList;
    LinkedHashMap<Integer,String> content_list=new LinkedHashMap<>();
    LinkedHashMap<Integer,String> address_list=new LinkedHashMap<>();
    TextView datashow;
    Boolean dis=false;
    String locality;
    String address;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private OnFragmentInteractionListener listener;
    HashMapAdapter adapter;

    public static FourthFragment newInstance() {
        return new FourthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("My Topics");
    }
    private void goLoginScreen() {
        Intent intent = new Intent(getContext(), Facebook_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_first1, menu);
        RelativeLayout noti = (RelativeLayout) menu.findItem(R.id.noti).getActionView();
        final TextView notimCounter = (TextView) noti.findViewById(R.id.counter);
        ImageButton notiimgb=(ImageButton)noti.findViewById(R.id.imgbutton);
        notiimgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = SixthFragment.newInstance();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });

        /**-----------------------------------------------Notification-------------------------------------------**/
        user_login=  FirebaseAuth.getInstance().getCurrentUser().getUid();
         mDatabase1= FirebaseDatabase.getInstance().getReference("users").child(user_login).child("Noti");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model1 m = postSnapshot.getValue(Model1.class);
                    int seen=m.getSeen();
                    if(seen==0){
                        count=count+1;
                    }
                }
                Log.d("pele",String.valueOf(count));

                notimCounter.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /**---------------------------------------------------------------------------------------------------------**/




        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.navigation_request).getActionView();
        final TextView mCounter = (TextView) badgeLayout.findViewById(R.id.counter);
        ImageButton imgb=(ImageButton)badgeLayout.findViewById(R.id.imgbutton);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = ThirdFragment.newInstance();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });

/**-----------------------------------------------Notification_Request-------------------------------------------**/
        user_login=  FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase1= FirebaseDatabase.getInstance().getReference("users").child(user_login).child("receiveFrom");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model1 m = postSnapshot.getValue(Model1.class);
                    int seen=m.getSeen();
                    if(seen==0){
                        count=count+1;

                    }
                }
                Log.d("pele",String.valueOf(count));

                mCounter.setText(String.valueOf(count));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /**---------------------------------------------------------------------------------------------------------**/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_sign_out) {
           /** user = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user).child("token");
            mDatabase.removeValue();**/
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            goLoginScreen();
        }
        if (item.getItemId() == R.id.menu_share) {
            Toast.makeText(getContext(), "Implement Share", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_fourth, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fourth, new FourthFragment());
                fragmentTransaction.commit();
            }
        });
        datashow=(TextView)rootview.findViewById(R.id.datashow);
        simpleList= (ListView)rootview.findViewById(R.id.simpleListView);
        mDatabase = FirebaseDatabase.getInstance().getReference("activity");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Activity1 post1 = postSnapshot.getValue(Activity1.class);
                        String f_id = post1.getUser();
                        int status = post1.getStatus();
                        if (status == 1) {
                            if (f_id.equals(user)) {
                                String content = post1.getName();
                                String address=post1.getAddress();
                                int id = post1.getCcid();
                                content_list.put(id, content);
                                address_list.put(id,address);
                                dis=true;
                            }
                        }

                    }
                adapter = new HashMapAdapter(content_list,address_list,getActivity(),getContext(),simpleList,getFragmentManager());
                simpleList.setAdapter(adapter);
                if(dis.equals(false)){
                    datashow.setText(Html.fromHtml("<b>No Topic Exist(s)</b> "));
                }
        }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

                return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
    }
}

