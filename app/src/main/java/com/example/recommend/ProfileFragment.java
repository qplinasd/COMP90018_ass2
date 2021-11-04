package com.example.recommend;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.recommend.adapter.ProfileItemRecyclerViewAdapter;
import com.example.recommend.data.User;
import com.example.recommend.databinding.FragmentProfileListBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private FragmentProfileListBinding binding;
    private RecyclerView recyclerView;
    private ImageButton button_return_city;
    private ProfileItemRecyclerViewAdapter adapter;

    private User user;
    private ArrayList<User> userList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get arguments from previous page
        Bundle args = getArguments();
        user = new User(args.getString("username"),
                args.getString("description"),
                "",
                args.getString("email"),
                args.getString("gender"));
        userList = new ArrayList<>();
        userList.add(user);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileListBinding.inflate(inflater, container, false);

        button_return_city = binding.buttonReturnCity;
        button_return_city.setOnClickListener(this);

        // Set the adapter
        recyclerView = binding.listProfile;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProfileItemRecyclerViewAdapter(userList, new ProfileItemRecyclerViewAdapter.clickProfileItem() {
            @Override
            public void onClickProfileItem(int position) {

                // can not change username
//                if(position == 0){
//                    editProfileItem("username", user.getUsername(), position);
//                }
                if(position == 1){
                    editProfileItem("gender", user.getGender(), position);
                }
                else if(position == 2){
                    editProfileItem("email", user.getEmail(), position);
                }
                else if(position == 3){
                    editProfileItem("description", user.getDescription(), position);
                }
            }
        });
        recyclerView.setAdapter(adapter);


        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        // return to previous page
        getActivity().getSupportFragmentManager().popBackStack();

    }

    public void editProfileItem(String item, String content, int position){
        EditText inputServer = new EditText(getContext());
        inputServer.setText(content);
        // show the dialog for changing user info
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(item).setIcon(android.R.drawable.ic_menu_edit).setView(inputServer)
                .setNegativeButton("Cancel", null);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                // change user info
                DatabaseReference databaseReference = FirebaseDatabase
                        .getInstance(FirebaseURL)
                        .getReference("users");

                databaseReference.orderByChild("username")
                        .equalTo(user.getUsername())
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                String key = snapshot.getKey();
                                String updateValue = inputServer.getText().toString();
                                databaseReference.child(key).child(item).setValue(updateValue);
                                // can not change username
                                if(position == 1){
                                    user.setGender(updateValue);
                                }
                                else if(position == 2){
                                    user.setEmail(updateValue);
                                }
                                else if(position == 3){
                                    user.setDescription(updateValue);
                                }
                                userList.clear();
                                userList.add(user);
                                adapter.notifyDataSetChanged();
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
            }
        });
        builder.show();
    }
}