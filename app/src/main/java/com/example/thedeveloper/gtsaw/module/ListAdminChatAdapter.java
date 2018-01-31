package com.example.thedeveloper.gtsaw.module;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thedeveloper.gtsaw.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by TheDeveloper on 31/01/2018.
 */

public class ListAdminChatAdapter extends RecyclerView.Adapter<ListAdminChatAdapter.ViewHolder> {
    Context context;
    ArrayList<Chat> objects;
    int resource;
    static String image = "null";
    static String name = "name";
    DatabaseReference databaseReference;

    public ListAdminChatAdapter(ArrayList<Chat> objects) {
        this.objects = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_staff_chat_room, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        holder.textViewChat.setText(objects.get(position).getMessage());
        String s = objects.get(position).getId1().toString();
        String s2 = objects.get(position).getId2().toString();
        databaseReference.child("UsersInformation")
                .child("Users").child(s)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                        image = userInformation.getAvatar();
                        name = userInformation.getName();
                        Picasso.with(context).load(image).into(holder.imageViewStaff1);
                        holder.textViewNameStaff1.setText(name+" sent");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        databaseReference.child("UsersInformation")
                .child("Users").child(s2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                        image = userInformation.getAvatar();
                        name = userInformation.getName();
                        Picasso.with(context).load(image).into(holder.imageViewStaff2);
                        holder.textViewNameStaff2.setText(name);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewStaff1, imageViewStaff2;
        TextView textViewNameStaff1, textViewNameStaff2, textViewChat;

        ViewHolder(View v) {
            super(v);
            imageViewStaff1 = (ImageView) itemView.findViewById(R.id.icon_staff1);
            imageViewStaff2 = (ImageView) itemView.findViewById(R.id.icon_staff2);
            textViewNameStaff1 = (TextView) itemView.findViewById(R.id.name_staff1);
            textViewNameStaff2 = itemView.findViewById(R.id.name_staff2);
            textViewChat=itemView.findViewById(R.id.txt_chat);
        }
    }

}
