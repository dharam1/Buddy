package com.example.dharmendra.buddy1;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


public class Chat extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 111;
    private MessageAdapter adapter;
    private ListView listView;
    private String loggedInUserName = "";
    FirebaseAuth mauth;
    ClipData myClip;
    ClipboardManager clipboard ;
    int cidd;
    Bundle b;
    DatabaseReference mDatabase,mDatabase1,mDatabase2;
    String activityName;
    Random rand;
    String nname,nickname,user;
    TextView t;
    Boolean exist=false;
    ArrayList<String> message_list=new ArrayList<>();
    ArrayList<Long> message_time=new ArrayList<>();
    ArrayList<String> message_nick=new ArrayList<>();
    ArrayList<String> message_user=new ArrayList<>();
    ArrayList<Long> search_time_list=new ArrayList<>();
    ArrayList<String> search_user_list=new ArrayList<>();
    ArrayList<String> search_list=new ArrayList<>();
    ArrayList<String> search_nick=new ArrayList<>();
    HashMap<String,String> connection_list=new HashMap<>();
    HashMap<String,String> connectionlist=new HashMap<>();
    TopicChatAdapterSearch searchadapter;
    FirebaseAuth firebaseAuth;
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
    EmojIconActions emojIcon;
    EmojiconEditText input, emojiconEditText2;
    EmojiconTextView textView;
    ImageView emojiButton;
    View rootView;
    int global_buddies;
    TextView dateView;
    int flag=0;
    //EditText input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        dateView = (TextView) findViewById(R.id.tv_date);
        Intent startingIntent = getIntent();
        if (startingIntent != null) {
            try {
                cidd = Integer.parseInt(startingIntent.getStringExtra("chatid"));
                Log.d("MJKL",String.valueOf(cidd));
                flag=1;
                String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(postSnapshot.getKey()).child("name");
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            connectionlist.put(postSnapshot.getKey(), dataSnapshot.getValue().toString());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }catch (NumberFormatException e){
                System.out.println("not a number");
            }
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rootView = findViewById(R.id.root_view);
        emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        input=(EmojiconEditText) findViewById(R.id.input);
        //input.setUseSystemDefault(true);
        getWindow().setBackgroundDrawableResource(R.drawable.background) ;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /**-----------------------------emoji----------------------------------**/
        emojIcon = new EmojIconActions(this, rootView,input, emojiButton);
        emojIcon.setIconsIds(R.drawable.ic_keyboard, R.drawable.ic_emoji);
        emojIcon.ShowEmojIcon();
        //emojIcon.setUseSystemEmoji(true);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });

        /**--------------------------------------------------------------**/
        ImageView fab = (ImageView) findViewById(R.id.fab);
        //final EditText input = (EditText) findViewById(R.id.input);
        listView = (ListView) findViewById(R.id.list);
        t=(TextView) findViewById(R.id.activity_name);
        if(flag==0) {
            b = getIntent().getExtras();
            if (b != null) {
                cidd = b.getInt("int_key");
                connectionlist=(HashMap<String, String>)getIntent().getSerializableExtra("con_list");
            }
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Chat.this, manageuser.class);
                i.putExtra("int_key", cidd);
                i.putExtra("con_list",connection_list);
                startActivity(i);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });

        rand=new Random();
        user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        exist=assignNickName();
        Log.d("MJKL",String.valueOf(cidd));
        mDatabase=FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(cidd));
        Log.d("MJKL",String.valueOf(cidd));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("MJKL",String.valueOf(cidd));
                Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                activityName=post1.getName();
                toolbar.setTitle(activityName);
                toolbar.setSubtitle("Tap here for group info");
                global_buddies=post1.getType();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(postSnapshot.getKey()).child("name");
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    connection_list.put(postSnapshot.getKey(), dataSnapshot.getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        showAllOldMessages(cidd);
        mDatabase=FirebaseDatabase.getInstance().getReference("chats").child(String.valueOf(cidd));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                message_list.clear();
                message_time.clear();
                message_user.clear();
                message_nick.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ChatMessage1 m = postSnapshot.getValue(ChatMessage1.class);
                    message_list.add(m.getMessageText());
                    message_time.add(m.getMessageTime());
                    message_user.add(m.getMessageUserId());
                    message_nick.add(m.getNickname());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user=FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase=FirebaseDatabase.getInstance().getReference("chats").child("kick").child(String.valueOf(cidd)).child(user);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(Chat.this, "You have been Blocked by Admin you can only View", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            CheckInternetConnection c=new CheckInternetConnection();
                            if(c.haveNetworkConnection()){
                                if (input.getText().toString().trim().equals("")) {
                                    Toast.makeText(Chat.this, "Please enter some texts!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if(!exist) {
                                        mDatabase2 = FirebaseDatabase.getInstance().getReference("chats").child("nickname").child(String.valueOf(cidd));
                                        mDatabase2.child(user).setValue(nickname);
                                    }
                                    String newInput = " " + input.getText();
                                    Log.d("lol",String.valueOf(cidd));
                                    FirebaseDatabase.getInstance()
                                            .getReference("chats").child(String.valueOf(cidd))
                                            .push()
                                            .setValue(new ChatMessage1(newInput,
                                                    FirebaseAuth.getInstance().getCurrentUser().getUid(),nickname)
                                            );
                                    /**-----------------------------------------------------------------------------------------**/
                                    /**mDatabase = FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(cidd)).child("users");
                                     mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                    dataSnapshot.getRef().removeValue();
                                    }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                    });**/

                                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                    Log.d("POIUY","POIUY");
                                                    connection_type con=postSnapshot.getValue(connection_type.class);
                                                    final String user_name = con.getUid();
                                                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user_name).child("activity").child(user).child(String.valueOf(cidd));
                                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            Log.d("POIUY","users/"+user_name+"activity/"+user+"/"+cidd);
                                                            if(!dataSnapshot.exists()){
                                                                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user_name).child("activity").child(user).child(String.valueOf(cidd));
                                                                String type="not created";
                                                                user_activity act=new user_activity(user,cidd,type,1,global_buddies,1);
                                                                mDatabase.setValue(act);
                                                            }
                                                            else{
                                                                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user_name).child("activity").child(user).child(String.valueOf(cidd)).child("fromconnection");
                                                                mDatabase.setValue(1);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });




                                    /**-----------------------------------------------------------------------------------------**/
                                    FirebaseDatabase.getInstance()
                                            .getReference("actnoti")
                                            .push()
                                            .setValue(new ChatMessage3(input.getText().toString(),
                                                    FirebaseAuth.getInstance().getCurrentUser().getUid(),cidd)
                                            );
                                    input.setText("");
                                }
                            }
                            else{
                                Toast.makeText(Chat.this, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }


    private void goLoginScreen() {
        Intent intent = new Intent(this, Facebook_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    private Boolean assignNickName(){
        mDatabase1=FirebaseDatabase.getInstance().getReference("chats").child("nickname").child(String.valueOf(cidd)).child(user);
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("pele","ronaldo");
                if(dataSnapshot.exists()){
                    nickname=dataSnapshot.getValue().toString();
                    Log.d("pele","true");
                    exist=true;
                }
                else{
                    nickname=x.get(rand.nextInt(x.size()));
                    Log.d("pele","false");
                    exist=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return exist;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main1, menu);
        final MenuItem search =  menu.findItem(R.id.search);
        MenuItemCompat.setOnActionExpandListener(search,
                new MenuItemCompat.OnActionExpandListener()
                {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item)
                    {
                        adapter = new MessageAdapter(Chat.this, ChatMessage1.class, R.layout.item_in_message,
                                FirebaseDatabase.getInstance().getReference("chats").child(String.valueOf(cidd)),cidd,connectionlist);
                        listView.setAdapter(adapter);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item)
                    {
                        // Toast.makeText(Chat.this, "Expand", Toast.LENGTH_SHORT).show();
                        SearchManager searchManager =
                                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                        SearchView searchView =
                                (SearchView) menu.findItem(R.id.search).getActionView();
                        searchView.setSearchableInfo(
                                searchManager.getSearchableInfo(getComponentName()));
                        searchView.setSubmitButtonEnabled(true);
                        searchView.setQueryHint(getString(R.string.search_hint));
                        //searchView.setIconifiedByDefault(false);
                        searchView.onActionViewExpanded();
                        searchView.setIconified(false);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query)
                            {
                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String query) {
                                search_list.clear();
                                search_time_list.clear();
                                search_user_list.clear();
                                search_nick.clear();
                                for (int i = 0; i < message_list.size(); i++) {
                                    String element = message_list.get(i);
                                    if (query.length() <= element.length()) {
                                        //String sub = element.substring(0, query.length());
                                        if (element.contains(query)) {
                                            search_list.add(element);
                                            Long time = message_time.get(i);
                                            search_time_list.add(time);
                                            String user = message_user.get(i);
                                            search_user_list.add(user);
                                            String nick=message_nick.get(i);
                                            search_nick.add(nick);
                                        }
                                    }
                                    searchadapter = new TopicChatAdapterSearch(query,search_list, search_time_list, search_user_list,search_nick, Chat.this, ChatMessage.class, R.layout.item_in_message,connectionlist);
                                    listView.setAdapter(searchadapter);
                                }
                                return true;
                            }
                        });
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }


    private void showAllOldMessages(int cidd) {
        loggedInUserName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("lol",String.valueOf(cidd));
        adapter = new MessageAdapter(Chat.this, ChatMessage1.class, R.layout.item_in_message,
                FirebaseDatabase.getInstance().getReference("chats").child(String.valueOf(cidd)),cidd,connectionlist);
        listView.setAdapter(adapter);

        TextView view = (TextView)findViewById(R.id.tv_date);

        listView.setOnScrollListener(new ChatOnScrollListener(view, adapter));
    }



    public String getLoggedInUserName() {
        return loggedInUserName;
    }
}