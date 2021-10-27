package com.example.recommend;
/**
 * Created by Haoran Lin on 2021/10/26.
 */

import static com.example.recommend.R.id.btn_login;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recommend.data.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisteredActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ChildEventListener {

    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private EditText et_user;
    private EditText et_desc;
    private RadioGroup mRadioGroup;
    private EditText et_pass;
    private EditText et_password;
    private EditText et_email;
    private Button btnRegistered;
    private Button btn_login;

    private String gender;
    private TextView tv_inconsistent;
    private TextView tv_wrong_name;
    private TextView tv_wrong_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        initView();
    }

    private void initView() {

        et_user = (EditText) findViewById(R.id.et_user);
        et_desc = (EditText) findViewById(R.id.et_desc);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        btnRegistered = (Button) findViewById(R.id.btnRegistered);
        btnRegistered.setOnClickListener(this);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(this);
        tv_inconsistent = (TextView) findViewById(R.id.text_inconsistent);
        tv_wrong_name = (TextView) findViewById(R.id.text_wrong_name);
        tv_wrong_pass = (TextView) findViewById(R.id.text_wrong_pass);

        checkEditTextNull(et_user, tv_wrong_name);
        checkEditTextNull(et_pass, tv_wrong_pass);
        passwordConfirm(et_pass, et_password);
        passwordConfirm(et_password, et_pass);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistered:

                DatabaseReference databaseReference = FirebaseDatabase
                        .getInstance(FirebaseURL)
                        .getReference("users");

                String name = et_user.getText().toString().trim();
                databaseReference.orderByChild("username").equalTo(name).addChildEventListener(this);

                break;
            case R.id.btn_login:
                startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId){
        RadioButton mRadioButton = group.findViewById(checkedId);
        gender = mRadioButton.getText().toString().trim();
    }

    public void passwordConfirm(EditText password, EditText confirmPass){
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!confirmPass.getText().toString().equals(s.toString())){
                    tv_inconsistent.setVisibility(View.VISIBLE);
                }
                else{
                    tv_inconsistent.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void checkEditTextNull(EditText editText, TextView textView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if (editText.getText().toString().trim().equals("")){
                   textView.setVisibility(View.VISIBLE);
               }
               else{
                   textView.setVisibility(View.INVISIBLE);
               }
            }
        });
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        //get input value
        String name = et_user.getText().toString().trim();
        String desc = et_desc.getText().toString().trim();
        String pass = et_pass.getText().toString().trim();
        String email = et_email.getText().toString().trim();

        if (tv_inconsistent.getVisibility() == View.INVISIBLE &&
                tv_wrong_name.getVisibility() == View.INVISIBLE &&
                tv_wrong_pass.getVisibility() == View.INVISIBLE){

            if(snapshot.exists()){
                new AlertDialog.Builder(this).setMessage("User already exists")
                        .setPositiveButton("confirm", null)
                        .show();
            }
            else{
                DatabaseReference databaseReference = FirebaseDatabase
                        .getInstance(FirebaseURL)
                        .getReference("users");
                String key = databaseReference.push().getKey();

                User newUser = new User(name, desc, pass, email, gender);
                Map<String, Object> userValues = newUser.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, userValues);

                databaseReference.updateChildren(childUpdates);

                startActivity(new Intent(this, LoginActivity.class));
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
