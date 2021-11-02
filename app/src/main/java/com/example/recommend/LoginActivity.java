package com.example.recommend;
/**
 * Created by Haoran Lin on 2021/10/26.
 * * stuId:1019019
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recommend.application.MyApplication;
import com.example.recommend.data.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener {

    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private Button btn_registered;
    private EditText et_name;
    private EditText et_password;
    private Button btnLogin;

    private String name;
    private String password;
    private String databasePassword;
    private MyApplication app;

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

                databaseReference.orderByChild("username").equalTo(name).addChildEventListener(this);

        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        if (snapshot.exists()){
            User user = snapshot.getValue(User.class);

            databasePassword = user.getPassword();

            if (databasePassword.equals("")) {
                new AlertDialog.Builder(this)
                        .setMessage("User does not exist")
                        .setPositiveButton("confirm", null)
                        .show();
            } else if (!databasePassword.equals(password)) {
                new AlertDialog.Builder(this)
                        .setMessage("Wrong password")
                        .setPositiveButton("confirm", null)
                        .show();
            } else {
                app = (MyApplication) getApplication();
                app.setUsername(name);

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("username", name);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
