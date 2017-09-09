package com.example.dharmendra.buddy1;

/**
 * Created by Dharmendra on 12-06-2017.
 */

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.example.dharmendra.buddy1.R.id.image;
import static com.example.dharmendra.buddy1.R.id.map;
import static com.example.dharmendra.buddy1.R.id.match_parent;
import static com.example.dharmendra.buddy1.R.id.thing_proto;
import static com.example.dharmendra.buddy1.R.id.wrap_content;
import static java.lang.System.out;

public class FifthFragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    TextView add_location;
    EditText editText;
    private int PLACE_PICKER_REQUEST = 1;
    String content;
    Double latitude=0.0,longitude=0.0;
    String address;
    GPSTracker gps;
    String user,user_login;
    int count=0;
    DatabaseReference mDatabase,mDatabase1;
    Boolean flag;
    int ccid;
    int status;
    GoogleMap mMap;
    Activity1 act1;
    GoogleApiClient mGoogleApiClient;
    ImageView imagev;
    LatLngBounds l;
    View layout;
    ImageButton imageButton;
    Random rand;
    ArrayList<String> x=new ArrayList<>(Arrays.asList("Barney","Ted","Marchel","Lily","Tyrion","Sersi","Rachel","Phoebe","Heisenberg","Joey","chandler","Jon Snow","Sansa"
            ,"Little Finger","Daenerys","Arya ","Joffery","Dwight","Jim","Angela","Kevin","Michael","Walter White","Jesse Pinkman","Skyler White"
            ,"Harvey Specter","Michael Ross","Rachel Zane","Jessica Pearson"));

    private OnFragmentInteractionListener listener;

    public static FifthFragment newInstance() {
        return new FifthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            /**user = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        LatLng pos=new LatLng(latitude,longitude);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.addMarker(new MarkerOptions()
                .position(pos));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,15));
        initiatePopupRequest();
    }
    private PopupWindow pwindodd;
    private void initiatePopupRequest() {
        try {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.popup,(ViewGroup) getView().findViewById(R.id.popup_element));
            pwindodd = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            pwindodd.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //pwindodd.setBackgroundDrawable(new ColorDrawable(
              //      android.graphics.Color.TRANSPARENT));
            editText=(EditText)layout.findViewById(R.id.edittext);
            Button add_activity = (Button) layout.findViewById(R.id.add_activity);
            final TextView countv=(TextView)layout.findViewById(R.id.count);
            final TextWatcher mTextEditorWatcher = new TextWatcher() {
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             public void onTextChanged(CharSequence s, int start, int before, int count) {
             countv.setText(String.valueOf(30 - s.length()));


             }

             public void afterTextChanged(Editable s) {
             }
             };
             editText.addTextChangedListener(mTextEditorWatcher);
            imageButton = (ImageButton) layout.findViewById(R.id.close);
            imageButton.setOnClickListener(cancel_button);
            add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(haveNetworkConnection()){
            content = editText.getText().toString().trim();
            if (content.equals("")) {
            Toast.makeText(getContext(), "Please Enter some Text", Toast.LENGTH_SHORT).show();
            }
            else if(content.length()>30) {
            Toast.makeText(getContext(), "Max allow 30 characters", Toast.LENGTH_SHORT).show();
            }
            else {
            //gps=new GPSTracker(getContext());
            // if (gps.canGetLocation()) {
            if(!latitude.equals(0.0)&&!longitude.equals(0.0)){
            //latitude = gps.getLatitude();
            //longitude = gps.getLongitude();
            user = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("activity");
            //flag=true;
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            /**for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Activity1 post1 = postSnapshot.getValue(Activity1.class);
            int status=post1.getStatus();
            Double lat=post1.getLatitude();
            if (lat.equals(latitude)&&status==1){
            //flag = false;
            Log.d("flag", "fs");
            break;
            }
            }**/
            /**if (flag == false) {
            Log.d("flag", "f");
            Toast.makeText(getContext(), "Please go to another location", Toast.LENGTH_SHORT).show();
            editText.setText("");
            }**/
           // else{
            mDatabase = FirebaseDatabase.getInstance().getReference("activity");
            Query lastQuery = mDatabase.orderByKey().limitToLast(1);
            lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Activity1 post1 = postSnapshot.getValue(Activity1.class);
            ccid = post1.getCcid();
            Log.d("flag",String.valueOf(ccid));
            }
            ccid=ccid+1;
            if(ccid!=0){
            Log.d("flag", "t");
            mDatabase = FirebaseDatabase.getInstance().getReference("activity");
            status=1;
            act1 = new Activity1(user, content, latitude, longitude,ccid,status,address);


                rand=new Random();
                String nickname=x.get(rand.nextInt(x.size()));
                final String userId = mDatabase.push().getKey();
                mDatabase.child(String.valueOf(ccid)).setValue(act1);
                FirebaseDatabase.getInstance()
                        .getReference("chats").child(String.valueOf(ccid))
                        .push()
                        .setValue(new ChatMessage1("Welcome to "+content+" admin here",user,nickname)
                        );
                DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("chats").child("nickname").child(String.valueOf(ccid));
                mDatabase2.child(user).setValue(nickname);




            Toast.makeText(getContext(), "Succesfully Added", Toast.LENGTH_SHORT).show();
                pwindodd.dismiss();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });
           // }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });

            }
            else{
            Toast.makeText(getContext(), "Please Select Location", Toast.LENGTH_SHORT).show();
            }

            }
            }else{
            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }

            }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private View.OnClickListener cancel_button = new View.OnClickListener() {
        public void onClick(View v) {
            pwindodd.dismiss();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);


        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_fifth, container, false);
        TextView add_activity = (TextView) rootview.findViewById(R.id.add_activity);
        String lat = this.getArguments().getString("latitude");
        String longi = this.getArguments().getString("longitude");
        address=this.getArguments().getString("address");
        Log.d("address",address);
        latitude=Double.parseDouble(lat);
        longitude=Double.parseDouble(longi);

        //add_location = (TextView) rootview.findViewById(R.id.add_location);
        //imagev=(ImageView)rootview.findViewById(R.id.mapdis);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


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
    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    public interface OnFragmentInteractionListener {
    }
}

