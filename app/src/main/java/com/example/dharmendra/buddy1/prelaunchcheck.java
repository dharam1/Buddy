package com.example.dharmendra.buddy1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class prelaunchcheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
