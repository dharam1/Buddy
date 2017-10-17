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
import android.os.Handler;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.shimmer.ShimmerFrameLayout;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

import static com.example.dharmendra.buddy1.R.id.map;


public class FifthFragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    TextView add_location;
    EditText editText;
    private int PLACE_PICKER_REQUEST = 1;
    String content;
    Double latitude=0.0,longitude=0.0;
    String address, title;
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
    int global_position;
    Random rand;
    long xxx;
    ArrayList<String> connectionlist=new ArrayList<>();
    ArrayList<String> x=new ArrayList<>(Arrays.asList("Ted Mosby",
            "Robin Scherbatsky",
            "Barney Stinson",
            "Lily Aldrin",
            "Marshall Eriksen",
            "Tracy McConnell1",
            "Harvey Specter",
            "Mike Ross",
            "Louis Litt",
            "Rachel Zane",
            "Donna Paulsen",
            "Jessica Pearson",
            "Eddard Stark",
            "Robert Baratheon",
            "Jaime Lannister",
            "Catelyn Stark",
            "Cersei Lannister",
            "Daenerys Targaryen",
            "Jorah Mormont",
            "Petyr Baelish",
            "Viserys Targaryen",
            "Jon Snow",
            "Sansa Stark",
            "Arya Stark",
            "Robb Stark",
            "Theon Greyjoy",
            "Bran Stark",
            "Joffrey Baratheon",
            "The Hound",
            "Tyrion Lannister",
            "Khal Drogo",
            "Tywin Lannister",
            "Davos Seaworth",
            "Samwell Tarly",
            "Margaery Tyrell",
            "Stannis Baratheon",
            "Melisandre",
            "Jeor Mormont",
            "Bronn",
            "Varys",
            "Shae",
            "Ygritte",
            "Talisa Maegyr",
            "The High Sparrow",
            "Rachel Green",
            "Chandler Bing",
            "Phoebe Buffay",
            "Ross Geller",
            "Monica Geller",
            "Joey Tribbiani",
            "Batman",
            "Joker",
            "Shelock Holmes",
            "Dr Watson",
            "Pablo Escobar",
            "Javier Pena",
            "Steve Murphy",
            "Gustavo Gaviria",
            "Michael Scofiled",
            "Lincoln Burrows",
            "Veronica Donovan",
            "John Abruzzi",
            "Theodore Bagwell",
            "Sara Tancredi",
            "Diana Prince",
            "Aquaman",
            "Flash",
            "Arrow",
            "Spiderman",
            "Captain America",
            "Iron Man",
            "Deadpool",
            "Hulk",
            "Wolverine",
            "Thor",
            "Hannah Baker",
            "Clay Jensen",
            "Sheri Holland",
            "Justin",
            "Authentic Crow",
            "Mr Robot",
            "Elliot Alderson",
            "Phillip Price",
            "Susan Jacobs",
            "Morty Smith",
            "Summer Smith",
            "King Flippy Nips",
            "Rick Sanchez",
            "Mr Beard",
            "Mr Beauregard",
            "Scooby-Doo",
            "Shaggy Rogers",
            "Tom & Jerry",
            "Fred Jones",
            "Paul Walkar",
            "Dominic Toretto",
            "Roman Pearce",
            "Pikachu",
            "Rich Alvelo",
            "Rosemarie Hornstein",
            "Loyce Scurlock",
            "Kacy Leverich",
            "Karyn Mullane",
            "Vern Brogden",
            "Jacquelyne Hamann",
            "Kareen Villafuerte",
            "Valda Perone",
            "Nadia Schlicher",
            "Hassie Beckwith",
            "Dreama Pille",
            "Kenia Thieme",
            "Vincenzo Sloss",
            "Keely Studer",
            "Fredrick Wymer",
            "Roberto Norville",
            "Margaret Duquette",
            "Laverna Tewksbury",
            "Kenton Gaddis",
            "Tomeka Ader",
            "Albertha Hogan",
            "Jake Chagolla",
            "Heather Helmer",
            "Adaline Frisk",
            "Gladis Lescarbeau",
            "Precious Lunde",
            "Shaun Beason",
            "Trey Mrozek",
            "Tad Lietz",
            "Palmer Finks",
            "Adelina Farrington",
            "Delmy Parks",
            "Burma Cosgrove",
            "Chadwick Rozman",
            "Christin Johansson",
            "Maura Bledsoe",
            "Mariano Helmuth",
            "Kari Hoffmeister",
            "Elva Hannigan",
            "Phung Durand",
            "Lucile Throop",
            "Aracelis Franqui",
            "Vallie Delorme",
            "Francine Benefiel",
            "Hal Gamino",
            "Ardith Demming",
            "Myrta Buttram",
            "Trang Waybright",
            "Brice Krebsbach",
            "Maisha Gonsalez",
            "Elizabet Kantor",
            "Venus Sinegal",
            "Kiersten Hu",
            "Maida Stanek",
            "Joana Nicely",
            "Emil Guernsey",
            "Danita Vanorden",
            "Elizbeth Plemons",
            "Linwood Pabon",
            "Echo Pruitt",
            "Pablo Dechant",
            "Alexander Primeaux",
            "Millicent Ray",
            "Lorna Melgoza",
            "Yvone Sugarman",
            "Lois Diorio",
            "Rosalina Ishida",
            "Socorro Speece",
            "Irma Bass",
            "Goku",
            "Vegeta",
            "Gohan",
            "Trunks",
            "Frieza",
            "Piccolo",
            "Goten",
            "Krillin",
            "Light Yagami",
            "L",
            "Ryuk",
            "Misa Amane",
            "Near",
            "Mello",
            "Teru Mikami",
            "Harry Potter",
            "Ron Weasley",
            "Hermione Granger",
            "Lord Voldemort",
            "Albus Dumbledore",
            "Severus Snape",
            "Rubeus Hagrid",
            "Draco Malfoy"

    ));

    private OnFragmentInteractionListener listener;

    public static FifthFragment newInstance() {
        return new FifthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Add a Topic");
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey there, I am using Buddy App! Download the app now :D");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Buddy"));
                }
            }, 250);
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
            pwindodd.setOutsideTouchable(true);
            pwindodd.setFocusable(true);
            //pwindodd.setBackgroundDrawable(new ColorDrawable(
              //      android.graphics.Color.TRANSPARENT));
            editText=(EditText)layout.findViewById(R.id.edittext);

            final RadioRealButton button1 = (RadioRealButton) layout.findViewById(R.id.btn_global);
            final RadioRealButton button2 = (RadioRealButton) layout.findViewById(R.id.btn_private);

            final RadioRealButtonGroup group = (RadioRealButtonGroup) layout.findViewById(R.id.btn_group);

            // onClickButton listener detects any click performed on buttons by touch
            group.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
                @Override
                public void onClickedButton(RadioRealButton button, int position) {
                    global_position=position;
                    //Toast.makeText(getActivity(), "Clicked! Position: " + position, Toast.LENGTH_SHORT).show();
                }
            });

            // onPositionChanged listener detects if there is any change in position
            group.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
                @Override
                public void onPositionChanged(RadioRealButton button, int position, int lastPosition) {
                    global_position=position;
                    //Toast.makeText(getActivity(), "Position Changed! Position: " + position, Toast.LENGTH_SHORT).show();
                }
            });

            // onLongClickedButton detects long clicks which are made on any button in group.
            // return true if you only want to detect long click, nothing else
            // return false if you want to detect long click and change position when you release
            group.setOnLongClickedButtonListener(new RadioRealButtonGroup.OnLongClickedButtonListener() {
                @Override
                public boolean onLongClickedButton(RadioRealButton button, int position) {
                    global_position=position;
                    //Toast.makeText(getActivity(), "Long Clicked! Position: " + position, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            /**-------------------------------------------------------------------------------------------**/
            String loginuser=FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(loginuser).child("connection");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        connection_type con = postSnapshot.getValue(connection_type.class);
                        connectionlist.add(con.getUid());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /**--------------------------------------------------------------------------------------------**/

            ShimmerFrameLayout shimmer = (ShimmerFrameLayout) layout.findViewById(R.id.tv_great);
            shimmer.setDuration(1500);
            shimmer.setRepeatDelay(2000);
            shimmer.startShimmerAnimation();

            ShimmerFrameLayout shimmer1 = (ShimmerFrameLayout) layout.findViewById(R.id.tv_great2);
            shimmer1.setDuration(2000);
            shimmer1.setRepeatDelay(2000);
            shimmer1.startShimmerAnimation();

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

                CheckInternetConnection c=new CheckInternetConnection();
                if(c.haveNetworkConnection()){
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
                    mDatabase=FirebaseDatabase.getInstance().getReference("activity");
                    mDatabase.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            xxx=mutableData.getChildrenCount();
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            Log.d("POPIUY",xxx+" ");
                            ccid=(int)xxx+1;
                            if(ccid!=0){
                                Log.d("flag", "t");
                                mDatabase = FirebaseDatabase.getInstance().getReference("activity");
                                status=1;
                                String type1;
                                String mapurl="https://maps.googleapis.com/maps/api/staticmap?center="+latitude+","+longitude+"&zoom=15&size=400x400&markers=color:red%7Clabel:Topic%7C"+latitude+","+longitude+"&key=AIzaSyBjPFbHd4ZKsJwf7GfEPRBwH27oIBO8iqY";
                                act1 = new Activity1(user, content, latitude, longitude,ccid,status,address,global_position,mapurl);
                                rand=new Random();
                                String nickname=x.get(rand.nextInt(x.size()));
                                final String userId = mDatabase.push().getKey();
                                mDatabase.child(String.valueOf(ccid)).setValue(act1);
                                FirebaseDatabase.getInstance()
                                        .getReference("chats").child(String.valueOf(ccid))
                                        .push()
                                        .setValue(new ChatMessage1("Welcome to "+content+" admin here",user,nickname)
                                        );
                                final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("chats").child("nickname").child(String.valueOf(ccid));
                                mDatabase2.child(user).setValue(nickname);




                                Toast.makeText(getContext(), "Succesfully Added", Toast.LENGTH_SHORT).show();
                                /**-----------------------------------------------------------------------------------**/
                                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("activity").child(user).child(String.valueOf(ccid));
                                String type= "Created";
                                user_activity act=new user_activity(user,ccid,type,1,global_position,1);
                                mDatabase.setValue(act);


                                if(global_position==1) {
                                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                    connection_type con = postSnapshot.getValue(connection_type.class);
                                                    String user_name = con.getUid().toString();
                                                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user_name).child("activity").child(user).child(String.valueOf(ccid));
                                                    String type = "Created";
                                                    user_activity act = new user_activity(user, ccid, type,1,global_position,1);
                                                    mDatabase.setValue(act);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else{
                                    Log.d("POPKLL","POPKLL_1");
                                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                user u=postSnapshot.getValue(user.class);
                                                if(connectionlist.contains(u.getUid())) {
                                                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(u.getUid()).child("activity").child(user).child(String.valueOf(ccid));
                                                    String type = "Created";
                                                    user_activity act = new user_activity(user, ccid, type, 1,global_position,1);
                                                    mDatabase.setValue(act);
                                                }
                                                else if(!u.getUid().equals(user)){
                                                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(u.getUid()).child("activity").child(user).child(String.valueOf(ccid));
                                                    String type = "Created";
                                                    user_activity act = new user_activity(user, ccid, type,0,global_position,1);
                                                    mDatabase.setValue(act);
                                                }
                                            }
                                        }



                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                /**------------------------------------------------------------------------------------**/
                                pwindodd.dismiss();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
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

