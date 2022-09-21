package com.cubo.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Tweet> tweets;
    RecyclerView tweetList;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseApp.initializeApp(this);

        tweetList = findViewById(R.id.tweet_rv);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tweetRef = database.getReference("tweets");
        fab = findViewById(R.id.fab);

        tweets = new ArrayList<>();
        TweetAdapter adapter = new TweetAdapter(this, tweets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        tweetList.setLayoutManager(layoutManager);
        tweetList.setAdapter(adapter);

        //Navigation menu
        NavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        ImageButton btnMenu = findViewById(R.id.btn_menu);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        //abre y cierra el drawer menu.
        btnMenu.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            }else {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        //Muestra los twits.
        tweetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    tweets.clear();
                    for (DataSnapshot tweetSnapshot : snapshot.getChildren()) {
                        tweets.add(
                                new Tweet(tweetSnapshot.child("username").getValue().toString(),
                                        tweetSnapshot.child("display_name").getValue().toString(),
                                        tweetSnapshot.child("message").getValue().toString(),
                                        tweetSnapshot.child("time_publish").getValue().toString()));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Get Tweets", "" + e.getMessage());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        View headerView = navView.getHeaderView(0);
        TextView txtHeaderName = headerView.findViewById(R.id.text_display_name);
        TextView txtUsername = headerView.findViewById(R.id.text_username);
        txtUsername.setText(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("username", ""));
        txtHeaderName.setText(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("name", ""));


        fab.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, NewTweetActivity.class));
        });
    }

    //Cierra sesion.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        switch (id){
            case R.id.item_10:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                return true;
            default:
                return false;
        }
    }
}