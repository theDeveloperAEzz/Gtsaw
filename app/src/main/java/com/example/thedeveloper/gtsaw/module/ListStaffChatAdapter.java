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

import com.example.thedeveloper.gtsaw.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ListStaffChatAdapter extends ArrayAdapter<Chat>  {
    Context context;
    ArrayList<Chat> objects;
    int resource;
    static String image = "null";
    static String name = "name";
    DatabaseReference databaseReference;

//    public ValueEventListener staffListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
//            image = userInformation.getAvatar();
//            name = userInformation.getName();
//
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };
////    public ValueEventListener staff2listener = new ValueEventListener() {
////        @Override
////        public void onDataChange(DataSnapshot dataSnapshot) {
////            UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
////
////        }
////
////        @Override
////        public void onCancelled(DatabaseError databaseError) {
////
////        }
////    };

    public ListStaffChatAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Chat> objects) {
        super(context, R.layout.list_item_staff_chat_room, objects);
        this.context = context;
        this.objects = objects;
        this.resource = resource;

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewholder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_staff_chat_room, null);
            viewholder.imageViewStaff1 = (ImageView) convertView.findViewById(R.id.icon_staff1);
            viewholder.imageViewStaff2 = (ImageView) convertView.findViewById(R.id.icon_staff2);
            viewholder.textViewNameStaff1 = (TextView) convertView.findViewById(R.id.name_staff1);
            viewholder.textViewNameStaff2 = (TextView) convertView.findViewById(R.id.name_staff2);
            viewholder.textViewChat = convertView.findViewById(R.id.txt_chat);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.textViewChat.setText(objects.get(position).getMessage());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String s = objects.get(position).getId1().toString();
        String s2 = objects.get(position).getId2().toString();
        final ViewHolder finalViewHolder1 = viewholder;
        databaseReference.child("UsersInformation")
                .child("Users").child(s)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                        image = userInformation.getAvatar();
                        name = userInformation.getName();
                        Picasso.with(context).load(image).into(finalViewHolder1.imageViewStaff1);
                        finalViewHolder1.textViewNameStaff1.setText(name);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final ViewHolder finalViewHolder2 = viewholder;
        databaseReference.child("UsersInformation")
                .child("Users").child(s2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                        image = userInformation.getAvatar();
                        name = userInformation.getName();
                        Picasso.with(context).load(image).into(finalViewHolder2.imageViewStaff2);
                        finalViewHolder2.textViewNameStaff2.setText(name);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return convertView;


    }

    class ViewHolder {
        ImageView imageViewStaff1, imageViewStaff2;
        TextView textViewNameStaff1, textViewNameStaff2, textViewChat;
    }
}