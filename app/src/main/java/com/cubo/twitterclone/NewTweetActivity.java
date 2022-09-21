package com.cubo.twitterclone;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewTweetActivity extends AppCompatActivity {
    AppCompatButton btnTweet;
    ImageButton btnBack;
    EditText edt_tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);
        FirebaseApp.initializeApp(this);
        btnTweet = findViewById(R.id.btn_tweet);
        btnBack = findViewById(R.id.btn_back);
        edt_tweet = findViewById(R.id.edit_tweet_content);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tweetRef = database.getReference("tweets");

        String displayName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("name", "");
        String username = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("username", "");

        System.out.println("name = " + displayName);
        System.out.println("username = " + username);

        btnTweet.setOnClickListener(v -> {
            if (!btnTweet.isEnabled()) {
                Log.e("btnTweet", "Disabled");
            } else
                Log.e("btnTweet", "Enabled");

            if (!edt_tweet.getText().toString().equals("") || edt_tweet.getText().toString().isEmpty()) {
                try {
                    String key = tweetRef.push().getKey();
//                if (key != null) {
                    tweetRef.child(key).child("username").setValue(username);
                    tweetRef.child(key).child("display_name").setValue(displayName);
                    tweetRef.child(key).child("message").setValue(edt_tweet.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mma MM/dd/yy", Locale.US);
                    String currentDnT = sdf.format(new Date());
                    tweetRef.child(key).child("time_publish").setValue(currentDnT)
                            .addOnCompleteListener(task -> {
                                onBackPressed();
                                btnTweet.setEnabled(false);
                            }).addOnCanceledListener(() -> {
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            });
//                }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(getApplicationContext(), "Enter your tweet first", Toast.LENGTH_SHORT).show();
            }

        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }
}