package com.example.dharmendra.buddy1;

import android.app.Application;
import android.content.Context;

public class TheApp extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getAppContext() {
        return sContext;
    }
}
