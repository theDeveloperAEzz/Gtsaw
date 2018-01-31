package com.example.thedeveloper.gtsaw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thedeveloper.gtsaw.module.Chat;
import com.example.thedeveloper.gtsaw.module.ChatAdapter;
import com.example.thedeveloper.gtsaw.module.Friend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private EditText metText;
    private Button mbtSent;
    TextView textView;
    String messageNameFriend;
    String messageIdFriend;
    String messageIdUser;
    private DatabaseReference mFirebaseRef;
    FirebaseAuth firebaseAuth;
    private List<Chat> mChats;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private String mId;
    String fId;
    static String idRoom = "";
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getValue().toString().contains(messageIdFriend)
                            && dataSnapshot1.getValue().toString().contains(messageIdUser)) {
                        dataSnapshot1.getRef().removeValue();
                    } else {

                    }
                }


            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                if (dataSnapshot.getValue().toString().contains(messageIdFriend)
                        && dataSnapshot.getValue().toString().contains(messageIdUser)) {
                    try {
                        Chat model = dataSnapshot.getValue(Chat.class);

                        mChats.add(model);
                        mRecyclerView.scrollToPosition(mChats.size() - 1);
                        mAdapter.notifyItemInserted(mChats.size() - 1);
                    } catch (Exception ex) {
                    }


                } else {

                }


            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Main2Activity main2Activity = new Main2Activity();
        main2Activity.online();
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        messageNameFriend = intent.getStringExtra("nameFriend");
        messageIdFriend = intent.getStringExtra("idFriend");
        messageIdUser = intent.getStringExtra("idUser");

        getSupportActionBar().setTitle(messageNameFriend);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference();
        metText = (EditText) findViewById(R.id.editWriteMessage);
        mbtSent = (Button) findViewById(R.id.btnSend);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvChat);
        mChats = new ArrayList<>();
        mId = messageIdUser;
        fId = messageIdFriend;
//        idRoom = (messageIdFriend + messageIdUser);

//        mId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new ChatAdapter(mChats, mId);
        mRecyclerView.setAdapter(mAdapter);

        mbtSent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString().trim();

                if (!message.isEmpty()) {
                    /**
                     * Firebase - Send messageNameFriend
                     */
                    Chat chat = new Chat(message, mId, fId);
                    chat.time = System.currentTimeMillis();

                    mFirebaseRef.child("Chat").child("messages").push().setValue(chat);

                }

                metText.setText("");
            }
        });

        mFirebaseRef.child("Chat").child("messages").addChildEventListener(listener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Main2Activity main2Activity = new Main2Activity();
        main2Activity.offline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            case R.id.delete_chat:
                Toast.makeText(getApplicationContext(), "chat was deleted", Toast.LENGTH_SHORT).show();
                mFirebaseRef.removeEventListener(listener);
                mFirebaseRef.child("Chat").child("messages").addValueEventListener(eventListener);
//                FirebaseDatabase.getInstance().getReference().child("Chat").child("message").removeValue();
                mChats.clear();
                mAdapter.notifyDataSetChanged();
                mFirebaseRef.addChildEventListener(listener);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

