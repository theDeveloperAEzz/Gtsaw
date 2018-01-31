package com.example.thedeveloper.gtsaw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thedeveloper.gtsaw.DB.DBHelper;
import com.example.thedeveloper.gtsaw.module.Chat;
import com.example.thedeveloper.gtsaw.module.ChatAdapter;
import com.example.thedeveloper.gtsaw.module.ListAdminChatAdapter;
import com.example.thedeveloper.gtsaw.module.ListStaffChatAdapter;
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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
/*
* today i finished 4 months from the first rial lesson in android
* it was static custom list :) >>>....17\8\2017
* * */

public class ProfileActivity extends AppCompatActivity {
    String userChoosenTask;
    ProgressDialog progressDialog;
    int REQUEST_CAMERA = 1;
    int SELECT_FILE = 2;
    String imagepath;
    String imgurl;
    UploadTask uploadTask;
    Uri selectedImage;
    StorageReference imageRef;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    ImageView imageViewProfile;
    TextView textViewName;
    TextView textViewPosition;
    TextView textViewEmail;
    ArrayList<Chat> chatArrayList;
    ArrayList<String> checkArrayList = new ArrayList<>();
    Button button;
    ListStaffChatAdapter listStaffChatAdapter;
    ListView listView;
    static String key;
    Boolean aBoolean = false;
    static String name = "name";
    DBHelper db = new DBHelper(ProfileActivity.this);
    String newName;
    RecyclerView mRecyclerView;
    ListAdminChatAdapter mAdapter;


    public ValueEventListener chatListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    chatArrayList.add(chat);
//                    mRecyclerView.scrollToPosition(checkArrayList.size() - 1);
//                    mAdapter.notifyItemInserted(checkArrayList.size() - 1);

                    mAdapter.notifyDataSetChanged();
                }
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textViewName = findViewById(R.id.name_profile);
        imageViewProfile = findViewById(R.id.profile_profile_pic);
        textViewPosition = findViewById(R.id.position);
        textViewEmail = findViewById(R.id.email_profile);

        chatArrayList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Main2Activity.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        Picasso.with(getApplicationContext()).load(Main2Activity.url).into(imageViewProfile);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                key = userInformation.getKey();
                if (key == MainActivity.ADMIN_PASSWORD) {
                    textViewPosition.setText(MainActivity.ADMIN_PASSWORD);
                } else {
                    textViewPosition.setText(key);
                }
                String p = textViewPosition.getText().toString();
                if (p.equals(MainActivity.ADMIN_PASSWORD)) {
                    databaseReference.child("Chat").child("messages").addValueEventListener(chatListener);
                    mRecyclerView = findViewById(R.id.admin_list);

                    mAdapter = new ListAdminChatAdapter(chatArrayList);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                    mRecyclerView.setHasFixedSize(true);
//                    mRecyclerView.setItemViewCacheSize(20);
//                    mRecyclerView.setDrawingCacheEnabled(true);
//                    mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    //so important
                    mRecyclerView.setNestedScrollingEnabled(false);
                    mRecyclerView.setAdapter(mAdapter);

                    //list had a problem with nestedScrollview
//                    listView = findViewById(R.id.admin_list);
//                    listStaffChatAdapter = new ListStaffChatAdapter(getApplicationContext(), R.layout.list_item_staff_chat_room, chatArrayList);
//                    listView.setAdapter(listStaffChatAdapter);
                }
                name = userInformation.getName();
                textViewName.setText(name);
                textViewEmail.setText(userInformation.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("UsersInformation").child("Users")
                .child(user.getUid()).addValueEventListener(listener);

        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("edit name");
                final EditText input = new EditText(ProfileActivity.this);
                input.setText(textViewName.getText());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newName = input.getText().toString();
                        if (newName.length() <= 2 || newName.length() >= 18) {
                            input.setError("enter First & Last name");
                            return;
                        }
                        if (TextUtils.isEmpty(newName)) {
                            Toast.makeText(getApplicationContext(), "please enter your name ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!textViewName.getText().equals(newName)) {
                            databaseReference.child("UsersInformation").child("Users")
                                    .child(firebaseAuth.getCurrentUser().getUid()).child("name").setValue(newName);
                        } else {

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "can't change email", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        Main2Activity main2Activity = new Main2Activity();
        main2Activity.offline();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public void UploadGallery() {

        imageRef = storageRef.child(firebaseAuth.getCurrentUser().getUid().toString() + ".jpg");
        uploadTask = imageRef.putFile(selectedImage);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error in uploading!  >" + e, Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgurl = taskSnapshot.getDownloadUrl().toString();
                databaseReference.child("UsersInformation").child("Users").child(user.getUid()).child("avatar").setValue(imgurl);
                Picasso.with(getApplicationContext()).load(imgurl).into(imageViewProfile);

            }
        });


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProfileActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        String[] path = {MediaStore.Images.Media.DATA};
        //show image that camera took
        if (path != null) {
            imageViewProfile.setImageBitmap(BitmapFactory.decodeFile(Arrays.toString(path), options));
//        UploadBitmap(BitmapFactory.decodeFile(Arrays.toString(path), options));
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "SelectName File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);

            }
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageViewProfile.setImageBitmap(bm);
        if (isOnline()) {
            UploadGallery();
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        imageViewProfile.setImageBitmap(imageBitmap);
        if (isOnline()) {
            UploadBitmap(imageBitmap);

        }
    }


    protected void applyBlurMaskFilter(TextView tv, BlurMaskFilter.Blur style) {

        float radius = tv.getTextSize() / 10;

        // Initialize a new BlurMaskFilter instance
        BlurMaskFilter filter = new BlurMaskFilter(radius, style);

        // Set the TextView layer type
        tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // Finally, apply the blur effect on TextView text
        tv.getPaint().setMaskFilter(filter);
    }

    @SuppressLint("NewApi")
    private void setupWindowAnimations() {

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

}


