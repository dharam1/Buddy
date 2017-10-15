/**Second Fragment**/
package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 12-06-2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecondFragment extends Fragment {
    int i;
    long x=0;
    String message;
    long time;
    ListView connectionlist;
    String user,name,url,user_login;
    int count1;
    DatabaseReference mDatabase,mDatabase1,mDatabase2,mDatabase3;
    LinkedHashMap<String,String> connection_list=new LinkedHashMap<>();
    LinkedHashMap<String,String> map=new LinkedHashMap<String, String>();
    private static final long DRAWER_DELAY = 250;
    ArrayList<Integer> s_countlist = new ArrayList<Integer>();
    ArrayList<String> last_message = new ArrayList<String>();
    ArrayList<String> s_lastmessage = new ArrayList<String>();
    ArrayList<String> s_urllist = new ArrayList<String>();
    ArrayList<String> ss_urllist = new ArrayList<String>();
    LinkedHashMap<String,String> s_connection_list=new LinkedHashMap<>();
    TextView datashow;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int count=0,c=0;
    Boolean refresh=false;
    String temp;
    Boolean check=false;
    ArrayList<connection_class> con_list=new ArrayList<>();
    LinkedHashMap<String, connection_class> con_map=new LinkedHashMap<>();

    private OnFragmentInteractionListener listener;

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Friends");
    }
    private void goLoginScreen() {
        Intent intent = new Intent(getContext(), Facebook_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_second, menu);
        super.onCreateOptionsMenu(menu,inflater);

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
                count1=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model1 m = postSnapshot.getValue(Model1.class);
                    int seen=m.getSeen();
                    if(seen==0){
                        count1=count1+1;
                    }
                }
                Log.d("pele",String.valueOf(count1));

                notimCounter.setText(String.valueOf(count1));
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
                count1=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model1 m = postSnapshot.getValue(Model1.class);
                    int seen=m.getSeen();
                    if(seen==0){
                        count1=count1+1;

                    }
                }
                Log.d("pele",String.valueOf(count1));

                mCounter.setText(String.valueOf(count1));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        MenuItem search =  menu.findItem(R.id.menu_search);
        MenuItemCompat.setOnActionExpandListener(search,
                new MenuItemCompat.OnActionExpandListener()
                {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item)
                    {
                        //Toast.makeText(getActivity(), "Collapse", Toast.LENGTH_SHORT).show();
                        HashMapAdapter2 adapter = new HashMapAdapter2(map,con_list,getContext(),getActivity(),getFragmentManager());
                        connectionlist.setAdapter(adapter);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item)
                    {
                        SearchManager searchManager =
                                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                        SearchView searchView =
                                (SearchView) menu.findItem(R.id.menu_search).getActionView();
                        searchView.setSearchableInfo(
                                searchManager.getSearchableInfo(getActivity().getComponentName()));
                        searchView.setSubmitButtonEnabled(true);
                        searchView.setQueryHint(getString(R.string.search_hint));
                        searchView.onActionViewExpanded();
                        searchView.onActionViewCollapsed();
                        searchView.setFocusable(false);
                        searchView.setIconified(false);
                        searchView.clearFocus();
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String query) {
                                s_connection_list.clear();
                                ss_urllist.clear();
                                int i=0;
                                for (Map.Entry<String, String> data : connection_list.entrySet()) {
                                    String name = data.getValue();
                                    String key = data.getKey();
                                    if (query.length() <= name.length()) {
                                        //String sub = name.substring(0, query.length());
                                        if (name.toLowerCase().contains(query.toLowerCase())) {
                                            s_connection_list.put(key, name);
                                            String url = s_urllist.get(i);
                                            ss_urllist.add(url);
                                            s_lastmessage.add(last_message.get(i));
                                        }
                                    }
                                    HashMapAdapter2s adapter = new HashMapAdapter2s(s_connection_list, ss_urllist, s_lastmessage,getContext());
                                    connectionlist.setAdapter(adapter);
                                    i++;
                                }
                                return false;
                            }
                        });
                        return true; // Return true to expand action view
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_sign_out) {
            /** try {
             FirebaseInstanceId.getInstance().deleteInstanceId();
             }catch (Exception e){

             }
             user = FirebaseAuth.getInstance().getCurrentUser().getUid();
             mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user).child("token");
             mDatabase.removeValue();**/
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            goLoginScreen();
        }
        if (item.getItemId() == R.id.menu_share) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey there, I am using Buddy App! Download the app now :D");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Buddy"));
                }
            }, DRAWER_DELAY);
        }

        return false;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_second, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.activity_main_swipe_refresh_layout);
        datashow=(TextView)rootview.findViewById(R.id.datashow);
        connectionlist= (ListView)rootview.findViewById(R.id.simpleListView);
        View root=inflater.inflate(R.layout.activity_list_view, container, false);
        final TextView textView=(TextView)root.findViewById(R.id.textView);
        final ProgressDialog Dialog = new ProgressDialog(getContext());
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.d("sui","sui");
                        final connection_type con=postSnapshot.getValue(connection_type.class);
                        final String user1=con.getUid();
                        final String type=con.getType();
                        Log.d("qwerty1",user1);

                        mDatabase1 = FirebaseDatabase.getInstance().getReference("users").child(user1);
                        mDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                user use = dataSnapshot.getValue(user.class);
                                name = use.getName();
                                map.put(user1,name);
                                url = use.getUrl();
                                connection_list.put(use.getUid(),name);
                                s_urllist.add(url);
                                Log.d("qwerty2",user1);
                                c=0;
                                count = 0;
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mDatabase2 = FirebaseDatabase.getInstance().getReference("pcchats").child(user).child(user1);
                        mDatabase2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Log.d("tempooo",user1);
                                check=false;
                                if (dataSnapshot.exists()) {
                                    count = 0;
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        Log.d("dharam", "called");
                                        ChatMessage m = postSnapshot.getValue(ChatMessage.class);
                                        int message_status = m.getMessage_status();
                                        message=m.getMessageText();
                                        time=m.getMessageTime();
                                        if (message_status == 0) {
                                            count = count + 1;
                                            check=true;
                                        }
                                    }
                                    last_message.add(message);
                                    connection_class c=con_map.get(user1);
                                    if(c==null){
                                        Log.d("aswd","aswd");
                                        Date d=new Date(time);
                                        connection_class con=new connection_class( user1,name,type,url,message,time,String.valueOf(count),d);
                                        con_list.add(con);
                                        con_map.put(user1,con);

                                    }else{
                                        Log.d("aswd","aswd1");
                                        Date d=new Date(time);
                                        connection_class con=new connection_class(c.getId(),c.getName(),c.getType(),c.getUrl(),message,time,String.valueOf(count),d);
                                        int pos=con_list.indexOf(c);
                                        con_list.remove(pos);
                                        con_list.add(con);
                                        con_map.put(user1,con);
                                    }

                                    //Log.d("QWERTY", user1+"---"+name + "---" +type+"---"+ url+ "---" + message + "---" + time + "---" +count);
                                }
                                else{
                                    Log.d("aswd","aswd2");
                                    count=0;
                                    message="0";
                                    time=0;
                                    s_countlist.add(count);
                                    Date d=new Date(time);
                                    connection_class con=new connection_class( user1,name,type,url,message,time,String.valueOf(count),d);
                                    con_list.add(con);
                                    con_map.put(user1,con);

                                }
                                /**for ( Map.Entry<String, connection_class> entry : con_map.entrySet()) {
                                    con_list.add(entry.getValue());
                                }**/
                                Log.d("aswd","aswd4");
                                Collections.sort(con_list);
                                Collections.reverse(con_list);
                                //Log.d("POPKLLLL", name + "" + url+ "" + message + "" + time + "" +count);
                                HashMapAdapter2 adapter = new HashMapAdapter2(map,con_list,getContext(),getActivity(),getFragmentManager());
                                connectionlist.setAdapter(adapter);

                            }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }
                else{
                    datashow.setText("No Connection");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.second, new SecondFragment());
                fragmentTransaction.commit();
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
    /**@Override
    public void onStop() {
    super.onStop();
    refresh = true;
    }

     @Override
     public void onResume() {
     super.onResume();
     // Check should we need to refresh the fragment
     if(refresh) {
     FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
     fragmentTransaction.replace(R.id.second, new SecondFragment());
     fragmentTransaction.commit();
     }
     }**/



    public interface OnFragmentInteractionListener {
    }
}