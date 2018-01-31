package com.example.thedeveloper.gtsaw.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.thedeveloper.gtsaw.ChatActivity;
import com.example.thedeveloper.gtsaw.R;
import com.example.thedeveloper.gtsaw.module.ListUsersAdapter;
import com.example.thedeveloper.gtsaw.module.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UsersFragment extends Fragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ListUsersAdapter listUsersAdapter;
    ArrayList<UserInformation> userInformationArrayList = new ArrayList<UserInformation>();
    Map mapUidFriend = new HashMap();
    Map mapNameFriend = new HashMap();
    Map mapIdRoomFriend = new HashMap();
    Map mapIdRoomUser = new HashMap();
    String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String uIdFriend;
    String uIdUser;
    UserInformation friendInformation;
    Map hashMap = new HashMap();
    ValueEventListener mFriendsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userInformationArrayList.clear();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                String name = dataSnapshot1.child("name").getValue().toString();
                hashMap.put(name, dataSnapshot1.getKey());
                listUsersAdapter.notifyDataSetChanged();
            }
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                if (dataSnapshot1.getKey() != Uid) {

                    friendInformation = dataSnapshot1.getValue(UserInformation.class);
                    mapUidFriend.put(friendInformation.getName(), dataSnapshot1.getKey());
                    mapNameFriend.put(friendInformation.getName(), friendInformation.getName());
                    userInformationArrayList.add(friendInformation);
                } else {

                }
                listUsersAdapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_users, container, false);
        listUsersAdapter = new ListUsersAdapter(getContext(), hashMap, R.layout.list_item_friend, userInformationArrayList);
        databaseReference.child("UsersInformation").child("Users").removeEventListener(mFriendsListener);
        databaseReference.child("UsersInformation").child("Users").addValueEventListener(mFriendsListener);
        final ListView listViewUsers = rootView.findViewById(R.id.list_of_users);
        listViewUsers.setAdapter(listUsersAdapter);
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(new Intent(getActivity(), ChatActivity.class));
                String uIdFriend = mapUidFriend.get(userInformationArrayList.get(i).getName()).toString();
                String uIdUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String name = mapNameFriend.get(userInformationArrayList.get(i).getName()).toString();

                mapIdRoomFriend.put(uIdFriend, uIdFriend);
                mapIdRoomUser.put(uIdUser, uIdUser);
                UsersFragment.this.uIdFriend = mapIdRoomFriend.get(uIdFriend).toString();
                UsersFragment.this.uIdUser = mapIdRoomUser.get(uIdUser).toString();
                intent.putExtra("idFriend", UsersFragment.this.uIdFriend);
                intent.putExtra("idUser", UsersFragment.this.uIdUser);
                intent.putExtra("nameFriend", name);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


}

