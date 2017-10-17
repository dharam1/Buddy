package com.example.dharmendra.buddy1;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Cnf on 8/3/2016.
 */
public class Facebook_login extends AppCompatActivity{

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    String fid;
    String name;
    String email;
    Boolean exists;
    String profilePicUrl;
    TextView textView,textView1;
    ArrayList<String> friends = new ArrayList<>();
    ArrayList<String> fbblockeduser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook_login);

        callbackManager = CallbackManager.Factory.create();
        textView1 =(TextView)findViewById(R.id.terms);
        textView1.setText("By Continuing you agree with ");
        textView =(TextView)findViewById(R.id.term);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://budddy-f974a.firebaseapp.com/'> terms and conditions </a>";
        textView.setText(Html.fromHtml(text));

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                loginButton.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                Log.v("Main1", object.toString());
                                if(!object.has("email")){
                                    try{
                                        object.put("email","N.A.");
                                    }   catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    fid=object.getString("id");
                                    name=object.getString("name");
                                    email=object.getString("email");
                                    profilePicUrl = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large";
                                    Log.d("POP",name);
                                    Log.d("POP",email);
                                    Log.d("POP","POP");
                                    Log.d("POP",profilePicUrl);
                                    myNewGraphReq();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("POP","POP12");
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,friendlists");
                request.setParameters(parameters);
                request.executeAsync();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void handleFacebookAccessToken(final AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),R.string.firebase_error_login, Toast.LENGTH_LONG).show();
                }
                else{
                    for(String friend:friends)
                        Log.d("POPKL",friend);
                    final String token= FirebaseInstanceId.getInstance().getToken();
                    exists =false;
                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                    final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(user_id)) {
                                mDatabase.child(user_id).child("token").setValue(token);
                                exists = true;

                                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            final user use = postSnapshot.getValue(user.class);
                                            final String fid = use.getFid();
                                            mDatabase = FirebaseDatabase.getInstance().getReference("fbblockeduser").child(user_id);
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    //if(dataSnapshot.exists()){
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                        String fidd = postSnapshot.getValue().toString();
                                                        fbblockeduser.add(fidd);

                                                    }
                                                        if (friends.contains(fid)&&(!fbblockeduser.contains(fid))){

                                                            Log.d("POPKLll", "POPKLll");
                                                            String uid = use.getUid();
                                                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("connection").child(uid);
                                                            connection_type con = new connection_type(uid, "facebook");
                                                            mDatabase.setValue(con);
                                                            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(uid).child("connection").child(user_id);
                                                            connection_type conn = new connection_type(user_id, "facebook");
                                                            mDatabase.setValue(conn);

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
                                /**-----------------------------------------------------------------------------------------**/
                                /**mDatabase = FirebaseDatabase.getInstance().getReference("activity");
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Activity1 act=postSnapshot.getValue(Activity1.class);
                                            if(act.getType()==0){
                                                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("activity").child(String.valueOf(act.getCcid()));
                                                user_activity a=new user_activity(act.getUser(),act.getCcid(),"not created",0,0);
                                                mDatabase.setValue(a);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });**/



                                /**-----------------------------------------------------------------------------------------**/
                            }
                            if(exists.equals(false)){

                                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                //String userId = mDatabase.push().getKey();
                                final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                user use = new user(user_id, fid, name, email,profilePicUrl);
                                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                mDatabase.child(user_id).setValue(use);
                                mDatabase.child(user_id).child("token").setValue(token);
                                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            final user use = postSnapshot.getValue(user.class);
                                            final String fid = use.getFid();
                                            mDatabase = FirebaseDatabase.getInstance().getReference("fbblockeduser").child(user_id);
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                   // if(dataSnapshot.exists()){
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                        String fidd = postSnapshot.getValue().toString();
                                                        fbblockeduser.add(fidd);

                                                    }

                                                    if (friends.contains(fid)&&(!fbblockeduser.contains(fid))){

                                                        Log.d("POPKLll", "POPKLll");
                                                        String uid = use.getUid();
                                                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("connection").child(uid);
                                                        connection_type con = new connection_type(uid, "facebook");
                                                        mDatabase.setValue(con);
                                                        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(uid).child("connection").child(user_id);
                                                        connection_type conn = new connection_type(user_id, "facebook");
                                                        mDatabase.setValue(conn);

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
                                /**-----------------------------------------------------------------------------------------**/
                                mDatabase = FirebaseDatabase.getInstance().getReference("activity");
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Activity1 act=postSnapshot.getValue(Activity1.class);
                                            if(act.getType()==0){
                                                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("activity").child(act.getUser()).child(String.valueOf(act.getCcid()));
                                                initial_user_activity a=new initial_user_activity(act.getUser(),act.getCcid(),"Created",0,0,1,act.getActdate());
                                                mDatabase.setValue(a);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                /**-----------------------------------------------------------------------------------------**/

                            }
                        }
                        @Override
                        public   void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Facebook_login.this, "\"The read failed: \"" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListner = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    loginButton.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);
                    goMainScreen();
                }
            }
        };
        firebaseAuth.addAuthStateListener(firebaseAuthListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListner);
    }

    public void goMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void myNewGraphReq() {
        final String graphPath = "/me/friends";
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = new GraphRequest(token,graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                    JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
                    for(int i=0;i<object.length();i++) {
                        JSONObject user = arrayOfUsersInFriendList.getJSONObject(i);
                        String usersName = user.getString("name");
                        String friendId = user.getString("id");
                        friends.add(friendId);
                        Log.d("POPNAME", friendId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("fields", "name");
        request.setParameters(param);
        request.executeAsync();
    }

}