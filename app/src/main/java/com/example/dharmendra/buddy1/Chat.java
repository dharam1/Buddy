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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


public class Chat extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 111;
    private FirebaseListAdapter<ChatMessage1> adapter;
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
    AdapterSearch1 searchadapter;
    FirebaseAuth firebaseAuth;
    ArrayList<String> x=new ArrayList<>(Arrays.asList("Barney","Ted","Marchel","Lily","Tyrion","Sersi","Rachel","Phoebe","Heisenberg","Joey","chandler","Jon Snow","Sansa"
            ,"Little Finger","Daenerys","Arya ","Joffery","Dwight","Jim","Angela","Kevin","Michael","Walter White","Jesse Pinkman","Skyler White"
            ,"Harvey Specter","Michael Ross","Rachel Zane","Jessica Pearson"));
    EmojIconActions emojIcon;
    EmojiconEditText input, emojiconEditText2;
    EmojiconTextView textView;
    ImageView emojiButton;
    View rootView;
    //EditText input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rootView = findViewById(R.id.root_view);
        emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        input=(EmojiconEditText) findViewById(R.id.input);
        getWindow().setBackgroundDrawableResource(R.drawable.background) ;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Chat.this, manageuser.class);
                i.putExtra("int_key", cidd);
                startActivity(i);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        /**-----------------------------emoji----------------------------------**/
        emojIcon = new EmojIconActions(this, rootView,input, emojiButton);
        emojIcon.setIconsIds(R.drawable.ic_keyboard, R.drawable.ic_emoji);
        emojIcon.ShowEmojIcon();
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
        b= getIntent().getExtras();
        if (b!= null){
            cidd=b.getInt("int_key");
        }
        else{
          //  Toast.makeText(this, "Not Passed", Toast.LENGTH_SHORT).show();
        }

        rand=new Random();
        user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        exist=assignNickName();
        mDatabase=FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(cidd));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                activityName=post1.getName();
                t.setText(activityName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        showAllOldMessages(cidd);
        mDatabase=FirebaseDatabase.getInstance().getReference("chats").child(String.valueOf(cidd));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                mDatabase=FirebaseDatabase.getInstance().getReference("chats").child("kick").child(String.valueOf(cidd)).child(user);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(Chat.this, "You have been Blocked by Admin you can only View", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(haveNetworkConnection()){
                                if (input.getText().toString().trim().equals("")) {
                                    Toast.makeText(Chat.this, "Please enter some texts!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if(exist==false) {
                                        mDatabase2 = FirebaseDatabase.getInstance().getReference("chats").child("nickname").child(String.valueOf(cidd));
                                        mDatabase2.child(user).setValue(nickname);
                                    }
                                    Log.d("lol",String.valueOf(cidd));
                                    FirebaseDatabase.getInstance()
                                            .getReference("chats").child(String.valueOf(cidd))
                                            .push()
                                            .setValue(new ChatMessage1(input.getText().toString(),
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
                                                        connection_type con=postSnapshot.getValue(connection_type.class);
                                                        String user_name = con.getUid().toString();
                                                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user_name).child("activity").child(String.valueOf(cidd));
                                                        String type="not created";
                                                        user_activity act=new user_activity(user,cidd,type);
                                                        mDatabase.setValue(act);
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
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
        MenuItem search =  menu.findItem(R.id.search);
        MenuItemCompat.setOnActionExpandListener(search,
                new MenuItemCompat.OnActionExpandListener()
                {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item)
                    {
                      //  Toast.makeText(Chat.this, "Collapse", Toast.LENGTH_SHORT).show();
                        adapter = new MessageAdapter(Chat.this, ChatMessage1.class, R.layout.item_in_message,
                                FirebaseDatabase.getInstance().getReference("chats").child(String.valueOf(cidd)),cidd);
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
                                for (int i = 0; i < message_list.size(); i++) {
                                    String element = message_list.get(i);
                                    if (query.length() <= element.length()) {
                                        //String sub = element.substring(0, query.length());
                                        if (element.toLowerCase().contains(query.toLowerCase())) {
                                            search_list.add(element);
                                            Long time = message_time.get(i);
                                            search_time_list.add(time);
                                            String user = message_user.get(i);
                                            search_user_list.add(user);
                                            String nick=message_nick.get(i);
                                            search_nick.add(nick);
                                        }
                                    }
                                    searchadapter = new AdapterSearch1(search_list, search_time_list, search_user_list,search_nick, Chat.this, ChatMessage.class, R.layout.item_in_message);
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
                FirebaseDatabase.getInstance().getReference("chats").child(String.valueOf(cidd)),cidd);
        listView.setAdapter(adapter);
        /**clipboard=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
         listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String content =listView.getItemAtPosition(position).toString();
        Log.d("ronaldo",content);
        myClip = ClipData.newPlainText("text",content);
        clipboard.setPrimaryClip(myClip);
        return false;
        }
        });**/
    }
    public String activityname(){
        mDatabase=FirebaseDatabase.getInstance().getReference("activity").child(String.valueOf(cidd));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity1 post1 = dataSnapshot.getValue(Activity1.class);
                activityName=post1.getName();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return activityName;
    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }
}
