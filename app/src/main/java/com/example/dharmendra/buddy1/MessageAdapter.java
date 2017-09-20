package com.example.dharmendra.buddy1;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;

import java.util.Date;

public class MessageAdapter extends FirebaseListAdapter<ChatMessage1> {
    ClipData myClip;
    ClipboardManager clipboard ;
    Chat activity;
    String user,user1;
    DatabaseReference mDatabase,mDatabase1;
    int seen;
    boolean exist;
    int cidd;

    public MessageAdapter(Chat activity, Class<ChatMessage1> modelClass, int modelLayout, DatabaseReference ref,int cidd) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
        this.mDatabase1=ref;
        this.cidd=cidd;
        user=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    @Override
    protected void populateView(View v, final ChatMessage1 model, int position) {
        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);
        messageText.setText(model.getMessageText());
        messageUser.setText(model.getNickname());
        // Format the date before showing it
        messageTime.setText(DateFormat.format("dd/MM/yyyy HH:mm", model.getMessageTime()));
        clipboard=(ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String content =model.getMessageText();
                Log.d("ronaldo",content);
                myClip = ClipData.newPlainText("text",content);
                clipboard.setPrimaryClip(myClip);
                return true;
            }
        });
        /** v.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        String name=activity.activityname();
        AlertDialog alertDialog = new AlertDialog.Builder(v.getRootView().getContext()).create();
        alertDialog.setMessage("Send Request To this Person");
        alertDialog.setButton("Send Request", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        if(haveNetworkConnection()){
        final String content =model.getMessageText();
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference("chats").child(String.valueOf(cidd));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
        ChatMessage post1 = postSnapshot.getValue(ChatMessage.class);
        String message=post1.getMessageText();
        String uid=post1.getMessageUserId();
        if(content.equals(message)){
        if(user.equals(uid)){
        Toast.makeText(activity, "Cannot send to Yourself", Toast.LENGTH_SHORT).show();
        }
        else{
        user1=post1.getMessageUserId();
        exist=false;
        if(exist==false) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("sendTo");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
        if(postSnapshot.exists()){
        Model m = postSnapshot.getValue(Model.class);
        final String value = m.getUser();
        if (user1.equals(value)) {
        Log.d("pele", "1");
        exist = true;
        Toast.makeText(activity, "Already Sent", Toast.LENGTH_SHORT).show();
        break;
        }
        }
        }
        }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
        });
        }
        if(exist==false){
        mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user).child("receiveFrom");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
        Model m = postSnapshot.getValue(Model.class);
        final String value = m.getUser();
        if (user1.equals(value)) {
        Log.d("pele", "2");
        exist = true;
        Toast.makeText(activity, "Already Receive", Toast.LENGTH_SHORT).show();
        break;
        }
        }
        }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
        });
        }

        if(exist==false) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("connection");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
        String value = String.valueOf(postSnapshot.getValue());
        if (user1.equals(value)) {
        Log.d("pele", "2");
        exist = true;
        Toast.makeText(activity, "Already Connected", Toast.LENGTH_SHORT).show();
        break;
        }
        }
        }
        if(exist==false){
        Log.d("pele","3");
        String activityname=activity.activityname();
        mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user).child("sendTo");
        final String userId = mDatabase.push().getKey();
        Model m1 = new Model(user1,activityname);
        mDatabase.child(userId).setValue(m1);
        Toast.makeText(activity, "Succesfully Send", Toast.LENGTH_SHORT).show();


        seen=0;
        mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user1).child("receiveFrom");
        final String user_Id = mDatabase.push().getKey();
        long time = new Date().getTime();
        Model3 m = new Model3(user,activityname,seen,time);
        mDatabase.child(user_Id).setValue(m);
        mDatabase=FirebaseDatabase.getInstance().getReference("reqnoti");
        final String user__Id = mDatabase.push().getKey();
        final String to=user1;
        final String from =user;
        Model2 m2 = new Model2(to,from,activityname);
        mDatabase.child(user__Id).setValue(m2);
        }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
        });
        }

        }



        break;
        }

        }
        Log.d("ronaldo",user+"Wants to Connect to "+user1+"from activity"+activity.activityname());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
        });
        }
        else{
        Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show();
        }

        }
        });
        alertDialog.show();


        }
        });**/
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ChatMessage1 chatMessage = getItem(position);
        if (chatMessage.getMessageUserId().equals(activity.getLoggedInUserName()))
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        else
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);

        //generating view
        populateView(view, chatMessage, position);

        return view;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
}