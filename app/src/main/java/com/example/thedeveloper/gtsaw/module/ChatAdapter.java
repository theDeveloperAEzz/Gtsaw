package com.example.thedeveloper.gtsaw.module;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thedeveloper.gtsaw.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final int CHAT_END = 1;
    private static final int CHAT_START = 2;
    private DatabaseReference mFirebaseRef;
    FirebaseAuth firebaseAuth;
    UserInformation userInformation;
    private List<Chat> mDataSet;
    private String mId;

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     * @param id      Device idReceiver
     */
    public ChatAdapter(List<Chat> dataSet, String id) {
        mDataSet = dataSet;
        mId = id;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_END) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_end, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_start, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet.get(position).getId1().equals(mId)) {
            return CHAT_END;
        }

        return CHAT_START;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        final Chat chat = mDataSet.get(position);
        UserInformation userInformation1=new UserInformation();

        holder.mTextViewMessage.setText(chat.getMessage());
        mFirebaseRef.child("UsersInformation").child("Users").child(chat.getId1()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInformation = dataSnapshot.getValue(UserInformation.class);
                    holder.mTextViewName.setText(userInformation.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String time = new SimpleDateFormat("EEE, d MMM yyyy").format(chat.getTime());
        String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));
        if (today.equals(time)) {
            holder.mTextViewTime.setText(new SimpleDateFormat("hh:mm a").format(new Date(chat.getTime())));
        } else {
            holder.mTextViewTime.setText(new SimpleDateFormat("hh:mm a \nd MMM ").format(new Date(chat.getTime())));
        }


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Inner Class for a recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewMessage;
        TextView mTextViewName;
        TextView mTextViewTime;
        TextView mTextViewStatus;

        ViewHolder(View v) {
            super(v);
            mTextViewMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            mTextViewName = (TextView) itemView.findViewById(R.id.name_user);
            mTextViewTime = (TextView) itemView.findViewById(R.id.date_time);
            mTextViewStatus = itemView.findViewById(R.id.online_offline);
        }
    }
}
