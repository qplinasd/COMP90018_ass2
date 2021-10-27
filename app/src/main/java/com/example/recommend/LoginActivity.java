package com.example.recommend;
/**
 * Created by Haoran Lin on 2021/10/26.
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recommend.data.CityBrief;
import com.example.recommend.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private Button btn_registered;
    private EditText et_name;
    private EditText et_password;
    private Button btnLogin;

    private String name;
    private String password;
    private String databasePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {

        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        databasePassword = "";

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //get input value
                name = et_name.getText().toString().trim();
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //get input value
                password = et_password.getText().toString().trim();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_registered:
                startActivity(new Intent(this, RegisteredActivity.class));
                break;
            case R.id.btnLogin:

                DatabaseReference databaseReference = FirebaseDatabase
                        .getInstance(FirebaseURL)
                        .getReference("users");

                databaseReference.addValueEventListener(this);

        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
            User user = dataSnapshot.getValue(User.class);

            if(user.getUsername().equals(name)){
                databasePassword = user.getPassword();

                if(databasePassword.equals("")){
                    new AlertDialog.Builder(this)
                            .setMessage("User does not exist")
                            .setPositiveButton("confirm", null)
                            .show();
                }
                else if(!databasePassword.equals(password)){
                    new AlertDialog.Builder(this)
                            .setMessage("Wrong password")
                            .setPositiveButton("confirm", null)
                            .show();
                }
                else{
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("username", name);
                    startActivity(intent);
                }

                break;
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}