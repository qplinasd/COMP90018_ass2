package com.example.recommend;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.recommend.data.User;
import com.example.recommend.databinding.FragmentPasswordBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PasswordFragment extends Fragment implements View.OnClickListener, ChildEventListener {

    private FragmentPasswordBinding binding;
    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private String username;
    private String old_password;
    private String et_old_password;
    private String et_new_password;
    private String et_confirm_password;

    private ImageButton button_pass_return;
    private EditText edit_old_pass;
    private EditText edit_new_pass;
    private EditText edit_new_confirm;
    private AppCompatButton btn_pass_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPasswordBinding.inflate(inflater, container, false);

        Bundle args = getArguments();
        username = args.getString("username");
        old_password = "";
        et_old_password = "";
        et_new_password = "";
        et_confirm_password = "";

        btn_pass_submit = binding.btnPassSubmit;
        button_pass_return = binding.buttonPassReturn;
        edit_old_pass = binding.editOldPass;
        edit_new_pass = binding.editNewPass;
        edit_new_confirm = binding.editNewConfirm;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // return to previous page
        button_pass_return.setOnClickListener(this);
        // submit new password
        btn_pass_submit.setOnClickListener(this);

        // get user old password
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("users");

        databaseReference.orderByChild("username").equalTo(username).addChildEventListener(this);

        edit_old_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //get input value
                et_old_password = edit_old_pass.getText().toString().trim();
            }
        });

        edit_new_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //get input value
                et_new_password = edit_new_pass.getText().toString().trim();
            }
        });

        edit_new_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //get input value
                et_confirm_password = edit_new_confirm.getText().toString().trim();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_pass_return:
                // return to previous page
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_pass_submit:
                // submit new password
                if (et_old_password.equals(old_password)){
                    if (et_new_password.equals("")){
                        // empty new password
                        new AlertDialog.Builder(getContext())
                                .setMessage("New password can not be empty")
                                .setPositiveButton("confirm", null)
                                .show();
                    }
                    else if (et_confirm_password.equals(et_new_password)){

                        // connect to database
                        DatabaseReference databaseReference = FirebaseDatabase
                                .getInstance(FirebaseURL)
                                .getReference("users");

                        databaseReference.orderByChild("username")
                                .equalTo(username)
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        String key = snapshot.getKey();
                                        // update new password
                                        databaseReference.child(key).child("password").setValue(et_new_password);
                                        new AlertDialog.Builder(getContext())
                                                .setMessage("Update password sucussfully!")
                                                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        getActivity().getSupportFragmentManager().popBackStack();
                                                    }
                                                })
                                                .show();
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
                                });
                    }else {

                        // confirmed password is not the same as new password
                        new AlertDialog.Builder(getContext())
                                .setMessage("Inconsistent new password")
                                .setPositiveButton("confirm", null)
                                .show();
                    }
                }else {
                    // old password is wrong
                    new AlertDialog.Builder(getContext())
                            .setMessage("Wrong old password")
                            .setPositiveButton("confirm", null)
                            .show();
                }
                break;
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        User user = snapshot.getValue(User.class);

        // get user old password
        old_password = user.getPassword();
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