package com.example.dharmendra.buddy1;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Dharmendra on 24-06-2017.
 */
public class TokenService extends FirebaseInstanceIdService {
    String user;
    FirebaseAuth firebaseAuth;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.w("notificationtoken", refreshedToken);
            sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
       /** user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDatabase;
        mDatabase= FirebaseDatabase.getInstance().getReference("users").child(user).child("token");
        mDatabase.setValue(token);**/
    }
}