package com.cubo.twitterclone;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class Init extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }
}
