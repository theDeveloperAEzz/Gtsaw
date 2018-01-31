package com.example.thedeveloper.gtsaw;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thedeveloper.gtsaw.DB.DBHelper;
import com.example.thedeveloper.gtsaw.Fragments.UsersFragment;
import com.example.thedeveloper.gtsaw.Fragments.WelcomeFragment;
import com.example.thedeveloper.gtsaw.module.ListUsersAdapter;
import com.example.thedeveloper.gtsaw.module.UserInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textViewName;
    TextView textViewEmail;
    ImageView imageViewProfile;
    public static String name = "name";
    public static String email = "name";
    public static String url = "";
    DBHelper db = new DBHelper(Main2Activity.this);
    ProgressDialog progressDialog;
    String imgurl;
    UploadTask uploadTask;
    StorageReference imageRef;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    UserInformation userInformation;

    ValueEventListener mListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            userInformation = dataSnapshot.getValue(UserInformation.class);
            name = userInformation.getName();
            email = userInformation.getEmail();
            textViewName.setText(name);
            textViewEmail.setText(email);
            url = userInformation.getAvatar();
            db.clearTable();
            Picasso.with(getApplicationContext()).load(url).into(imageViewProfile);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    Button button;

    @Override
    protected void onStart() {
        super.onStart();
        online();
        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            FirebaseAuth.getInstance().getCurrentUser().reload();
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.facebook_default);
            imageViewProfile.setImageBitmap(icon);
            if (isOnline()) {
                UploadBitmap(icon);
            }
            new AlertDialog.Builder(Main2Activity.this)
                    .setTitle("Check your email address to complete verification")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }

                    }).show();

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        progressDialog = new ProgressDialog(Main2Activity.this);
        progressDialog.setMessage("");
//


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        textViewName = header.findViewById(R.id.textViewNameNav);
        textViewEmail = header.findViewById(R.id.textViewEmailNav);
        imageViewProfile = header.findViewById(R.id.nav_profile_pic);
        databaseReference.child("UsersInformation").child("Users").child(user.getUid()).addValueEventListener(mListener);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        if (savedInstanceState == null) {
            fragment = new UsersFragment();
            ft.add(R.id.content_frame2, fragment).commit();
        }

    }

    public void online() {
        if (firebaseAuth.getCurrentUser() != null) {
            UserInformation userInformation = new UserInformation();
            userInformation.setIsOnline(true);
            databaseReference.child("UsersInformation").child("Users")
                    .child(firebaseAuth.getCurrentUser().getUid()).child("isOnline").setValue(userInformation.isOnline());

        }
    }

    public void offline() {
        UserInformation userInformation = new UserInformation();
        userInformation.setIsOnline(false);
        databaseReference.child("UsersInformation").child("Users")
                .child(firebaseAuth.getCurrentUser().getUid()).child("isOnline").setValue(userInformation.isOnline());
    }

    @Override
    protected void onPause() {
        super.onPause();
        offline();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void UploadBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        imageRef = storageRef.child(firebaseAuth.getCurrentUser().getUid().toString() + ".jpg");
        uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgurl = taskSnapshot.getDownloadUrl().toString();
                databaseReference.child("UsersInformation").child("Users").child(user.getUid()).child("avatar").setValue(imgurl);
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Picasso.with(getApplicationContext()).load(imgurl).into(imageViewProfile);
            }
        });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(Main2Activity.this, ProfileActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
