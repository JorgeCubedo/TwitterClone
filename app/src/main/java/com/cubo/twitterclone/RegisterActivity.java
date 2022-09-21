package com.cubo.twitterclone;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName, edtEmail, edtDate;
    AppCompatButton btnNext;
    Calendar calendar;
    String dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edit_name);
        edtEmail = findViewById(R.id.edit_phone_email);
        edtDate = findViewById(R.id.edit_date_of_birth);
        btnNext = findViewById(R.id.btn_next);


        setUpToolBar();

        btnNext.setOnClickListener(v -> {
            if (validate(edtName.getText().toString(), edtEmail.getText().toString(),
                    edtDate.getText().toString())) {
                Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                intent.putExtra("name", edtName.getText().toString());
                intent.putExtra("email", edtEmail.getText().toString());
                intent.putExtra("dob", dob);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Fill in all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDobEdit();
        };


        edtDate.setOnClickListener(v -> {
            new DatePickerDialog(RegisterActivity.this, date, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });


    }

    private void setUpToolBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    boolean validate(String name, String email, String dob){
        if (name.equals("") || email.equals("") || dob.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void updateDobEdit() {
        String dateFormat = "dd/MM/yy";

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        edtDate.setText(sdf.format(calendar.getTime()));
        dob = sdf.format(calendar.getTime());
    }
}