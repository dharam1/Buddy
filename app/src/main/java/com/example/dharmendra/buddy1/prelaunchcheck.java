package com.example.dharmendra.buddy1;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;

public class prelaunchcheck extends AppCompatActivity {
    static boolean calledAlready = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        PrefManager prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            startActivity(new Intent(prelaunchcheck.this, Launcher.class));
            finish();
        }
        else{
            startActivity(new Intent(prelaunchcheck.this, WelcomeActivity.class));
            finish();
        }
    }

}
