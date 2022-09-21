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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register2Activity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();

    EditText edtUser, edtPass;
    AppCompatButton btnFinish;

    String name, email, dob;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        FirebaseApp.initializeApp(getApplicationContext());

        edtUser = findViewById(R.id.edit_name);
        edtPass = findViewById(R.id.edit_password);
        btnFinish = findViewById(R.id.btn_finish);

        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();

        btnFinish.setOnClickListener(v -> {
            if (validate(edtUser.getText().toString(), edtPass.getText().toString())){
                //register user
                register(email, edtPass.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Fill in all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        dob = bundle.getString("dob");


        setUpToolBar();
    }

    private void setUpToolBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private boolean validate(String name, String pass) {
        if (name.equals("") || pass.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    
    void  register(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "User successfully created", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Success");
                            nextPage(user);
                        } else {
                            try {
                                throw  task.getException();
                            } catch (FirebaseAuthUserCollisionException exception) {
                                exception.printStackTrace();
                                Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "createUserWithEmailFailure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



    private void nextPage(FirebaseUser user) {
        //change the user page after successful login
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");

        userRef.child(user.getUid()).child("name").setValue(name);
        userRef.child(user.getUid()).child("dob").setValue(dob);
        userRef.child(user.getUid()).child("email").setValue(email);
        userRef.child(user.getUid()).child("username").setValue(edtUser.getText().toString());

        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("is_user_logged", true).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("name", name).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("username", edtUser.getText().toString()).apply();

        Intent intent = new Intent(Register2Activity.this, HomeActivity.class);
        startActivity(intent);
    }
}