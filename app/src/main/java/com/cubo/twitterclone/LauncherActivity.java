package com.cubo.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.google.firebase.FirebaseApp;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        FirebaseApp.initializeApp(getApplicationContext());

        new Handler().postDelayed(() -> {

            if (PreferenceManager.getDefaultSharedPreferences(LauncherActivity.this).getBoolean("is_user_logged", false)) {
                Intent intent = new Intent(LauncherActivity.this, HomeActivity.class);
                finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }


        }, 1200);
    }
}