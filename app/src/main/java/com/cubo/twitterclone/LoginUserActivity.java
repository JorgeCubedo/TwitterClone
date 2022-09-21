package com.cubo.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginUserActivity extends AppCompatActivity {
    final String TAG = this.getClass().getName();
    FirebaseAuth mAuth;
    AppCompatButton btnLogin;
    EditText edtEmail, edtPass;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("tweets");

        btnLogin = findViewById(R.id.btn_login);
        edtEmail = findViewById(R.id.edit_email);
        edtPass = findViewById(R.id.edit_password);

        btnLogin.setOnClickListener(v -> {
            if (validate(edtEmail.getText().toString(), edtPass.getText().toString())){
                login(edtEmail.getText().toString(), edtPass.getText().toString());
                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Fill in all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void login(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "User successfully signed in", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Success");
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("is_user_logged", true).apply();
                            String displayName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("name", "");
                            String username = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("username", "");
                            usersRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try {
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("name", displayName).apply();
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("username", username).apply();

                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(new Intent(LoginUserActivity.this, HomeActivity.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Log.e("Login", "Authentication failed.", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validate(String name, String pass) {
        return !name.equals("") && !pass.equals("");
    }
}