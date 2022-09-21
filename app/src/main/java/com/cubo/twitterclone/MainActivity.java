package com.cubo.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    TextView tvLogin;
    AppCompatButton btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(getApplicationContext());

        tvLogin = findViewById(R.id.text_login);
        btnCreateAccount = findViewById(R.id.btn_create_account);

        btnCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginUserActivity.class));
        });
    }
}