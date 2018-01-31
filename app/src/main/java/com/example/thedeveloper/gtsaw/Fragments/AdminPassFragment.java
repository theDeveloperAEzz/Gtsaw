package com.example.thedeveloper.gtsaw.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thedeveloper.gtsaw.MainActivity;
import com.example.thedeveloper.gtsaw.R;
import com.example.thedeveloper.gtsaw.module.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminPassFragment extends Fragment {
    EditText editText;
    Button button;
    String pass;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersInformation");
    ArrayList<String> stringArrayList;

//    ValueEventListener listener = ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_pass, container, false);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        stringArrayList = new ArrayList<>();
//        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue().equals(null)) {
//
//                } else {
//
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        UserInformation information = dataSnapshot1.getValue(UserInformation.class);
//                        if (information.getKey().equals(MainActivity.ADMIN_PASSWORD)) {
//
//                            stringArrayList.add(information.getKey());
//
//                        } else {
//                        }
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        editText = rootView.findViewById(R.id.et_admin_pass);
        button = rootView.findViewById(R.id.ok_pass);
        final SignUpFragment signUpFragment = new SignUpFragment();
//        if (stringArrayList.size() > 0) {
//            Toast.makeText(getContext(), "admin signed", Toast.LENGTH_SHORT).show();
//        } else {
//
//        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = editText.getText().toString().trim();
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getContext(), "enter your pass", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isValidAdminPass(pass)) {
                    ft.replace(R.id.content_frame, signUpFragment, "findThisFragment4")
                            .addToBackStack(null)
                            .commit();

                } else {
                    Toast.makeText(getContext(), "wrong pass", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return rootView;
    }

    public boolean isValidAdminPass(String pass) {

        if (pass.equals(MainActivity.ADMIN_PASSWORD)) {
            return true;
        }
        return false;
    }
}
