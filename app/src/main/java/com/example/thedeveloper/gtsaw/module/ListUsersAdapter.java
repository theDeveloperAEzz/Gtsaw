package com.example.thedeveloper.gtsaw.module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thedeveloper.gtsaw.Main2Activity;
import com.example.thedeveloper.gtsaw.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ListUsersAdapter extends ArrayAdapter<UserInformation> {
    Context context;
    ArrayList<UserInformation> objects;
    int resource;
    DatabaseReference databaseReference;
    String idFriend;
    String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Map hashMap;

    public ListUsersAdapter(@NonNull Context context, Map hashMap, int resource, @NonNull ArrayList<UserInformation> objects) {
        super(context, R.layout.list_item_friend, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.hashMap = hashMap;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewholder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_friend, null);
            viewholder.imageView = (ImageView) convertView.findViewById(R.id.icon_avatar);
            viewholder.textViewName = (TextView) convertView.findViewById(R.id.txtName);
            viewholder.textViewTime = (TextView) convertView.findViewById(R.id.txtTime);
            viewholder.textViewMessage = (TextView) convertView.findViewById(R.id.txtMessage);


            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(objects.get(position).getAvatar()).into(viewholder.imageView);
        if (objects.get(position).getName() == Main2Activity.name) {
            viewholder.textViewName.setText("you");
        } else {
            viewholder.textViewName.setText(objects.get(position).getName());

        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ViewHolder finalViewHolder = viewholder;
        databaseReference.child("Chat").child("messages").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            if (dataSnapshot.getValue().toString().contains(hashMap.get(Main2Activity.name).toString())
                                    && dataSnapshot.getValue().toString().contains(hashMap.get(objects.get(position).getName()).toString())) {
                                try {
                                    Chat model = dataSnapshot.getValue(Chat.class);
                                    finalViewHolder.textViewMessage.setText(model.getMessage());
                                    @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("EEE, d MMM yyyy").format(model.getTime());
                                    @SuppressLint("SimpleDateFormat") String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));
                                    if (today.equals(time)) {
                                        finalViewHolder.textViewTime.setText(new SimpleDateFormat("hh:mm a").format(new Date(model.getTime())));
                                    } else {
                                        finalViewHolder.textViewTime.setText(new SimpleDateFormat("hh:mm a \nd MMM ").format(new Date(model.getTime())));
                                    }
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

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }
        );

        return convertView;
    }

}

class ViewHolder {
    ImageView imageView;
    TextView textViewName, textViewTime, textViewMessage;
}