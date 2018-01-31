package com.example.thedeveloper.gtsaw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;

import com.example.thedeveloper.gtsaw.Fragments.WelcomeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    Button button;
    public static String position = "";
    public static final String ADMIN_PASSWORD = "hello_admin";

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, Main2Activity.class));
            finish();
        }
    }

    @SuppressLint("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setupWindowAnimations();
//        ft.setCustomAnimations(R.anim.translate_up, R.anim.fade_out_left);

        Fragment fragment = null;
        if (savedInstanceState == null) {
            fragment = new WelcomeFragment();
            ft.add(R.id.content_frame, fragment).commit();
        }


    }

    @SuppressLint("NewApi")
    private void setupWindowAnimations() {

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }
}
