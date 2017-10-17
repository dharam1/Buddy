package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 12-06-2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SixthFragment extends Fragment {
    String user,user_login;
    int count;
    DatabaseReference mDatabase,mDatabase1;
    ListView simpleList;
    TextView datashow;
    Boolean dis=false;
    LinkedHashMap<String,String> noti_list=new LinkedHashMap<>();
    LinkedHashMap<String,String> url_list=new LinkedHashMap<>();
    ArrayList<String> notiactlist=new ArrayList<>();
    ArrayList<String> timelist=new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    private OnFragmentInteractionListener listener;
    HashMapAdapter12 adapter;

    public static SixthFragment newInstance() {
        return new SixthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Notifications");
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_sixth, menu);
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

    }
    private void goLoginScreen() {
        Intent intent = new Intent(getContext(), Facebook_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
        if (item.getItemId() == R.id.menu_clear) {
            CheckInternetConnection c=new CheckInternetConnection();
            if(c.haveNetworkConnection()){
                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("Noti");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                        Toast.makeText(getContext(), "Cleared", Toast.LENGTH_SHORT).show();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.sixth, new SixthFragment());
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else{
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }

        }
        return false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_sixth, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.sixth, new SixthFragment());
                fragmentTransaction.commit();
            }
        });
        final ListView notilist=(ListView)rootview.findViewById(R.id.simpleListView);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final TextView datashow=(TextView)rootview.findViewById(R.id.datashow);
        View root=inflater.inflate(R.layout.activity_list_view12, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("Noti");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Model3 m = postSnapshot.getValue(Model3.class);
                        final String user1 = m.getUser();
                        final long time=m.getTime();
                        final String activityname = m.getActivityname();
                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user1);
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                user use = dataSnapshot.getValue(user.class);
                                String name = use.getName();
                                String url=use.getUrl();
                                noti_list.put(user1, name);
                                url_list.put(user1,url);
                                notiactlist.add(activityname);
                                timelist.add(String.valueOf(time));
                                adapter = new HashMapAdapter12(noti_list,url_list,notiactlist,timelist ,getContext());
                                notilist.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }
                else{
                    datashow.setText(Html.fromHtml("<b>No Notification(s)</b> "));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        notilist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        notilist.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount  = notilist.getCheckedItemCount();
                mode.setTitle(checkedCount  + "  Selected");
                adapter.toggleSelection(position,notilist);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.multiple_clear, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch  (item.getItemId()) {
                    case R.id.selectAll:
                        final int checkedCount  = noti_list.size();
                        adapter.removeSelection();
                        for (int i = 0; i <  checkedCount; i++) {
                            notilist.setItemChecked(i,true);
                        }
                        mode.setTitle(checkedCount  + "  Selected");
                        return true;
                    case R.id.clear:
                        AlertDialog.Builder  builder = new AlertDialog.Builder(
                                getContext());
                        builder.setMessage("Do you  want to clear ?");

                        builder.setNegativeButton("No", new  DialogInterface.OnClickListener() {

                            @Override
                            public void  onClick(DialogInterface dialog, int which) {
                                // TODO  Auto-generated method stub

                            }
                        });
                        builder.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {

                            @Override
                            public void  onClick(DialogInterface dialog, int which) {
                                // TODO  Auto-generated method stub
                                SparseBooleanArray selected = adapter
                                        .getSelectedIds();
                                for (int i =  (selected.size() - 1); i >= 0; i--) {
                                    if  (selected.valueAt(i)) {
                                        String  selecteditem = adapter.getItem(selected.keyAt(i)).toString();
                                        String[] parts = selecteditem.split("=");
                                        final String content = (parts[0]);
                                        Log.d("ghjk",String.valueOf(content));
                                        CheckInternetConnection c=new CheckInternetConnection();
                                        if(c.haveNetworkConnection()){
                                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("Noti");
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                        Model1 m = postSnapshot.getValue(Model1.class);
                                                        String user1=m.getUser();
                                                        if(user1.equals(content)){
                                                            postSnapshot.getRef().removeValue();
                                                            Toast.makeText(getContext(), "Successfully Removed", Toast.LENGTH_SHORT).show();
                                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                            fragmentTransaction.replace(R.id.sixth, new SixthFragment());
                                                            fragmentTransaction.commit();
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
                                            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }
                                mode.finish();
                                selected.clear();

                            }
                        });
                        AlertDialog alert =  builder.create();
                        alert.setIcon(R.drawable.delete);// dialog  Icon
                        alert.setTitle("Confirmation"); // dialog  Title
                        alert.show();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.sixth, new SixthFragment());
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

    public interface OnFragmentInteractionListener {
    }
}

