package com.example.thedeveloper.gtsaw.Fragments;

import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thedeveloper.gtsaw.MainActivity;
import com.example.thedeveloper.gtsaw.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WelcomeFragment extends Fragment {
    TextView textViewTile;
    Button buttonSignIn;
    Button buttonSignUp;
    RadioGroup radioGroupAdminStaff;
    RadioButton radioButton;
    Boolean aBoolean=false;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        textViewTile = rootView.findViewById(R.id.welcome);
        final SignInFragment signInFragment = new SignInFragment();
        final SignUpFragment signUpFragment = new SignUpFragment();
        final AdminPassFragment adminPassFragment = new AdminPassFragment();
        buttonSignIn = rootView.findViewById(R.id.signIn);
        buttonSignUp = rootView.findViewById(R.id.signUp);
        radioGroupAdminStaff = rootView.findViewById(R.id.radio_admin_staff);
        applyBlurMaskFilter(textViewTile, BlurMaskFilter.Blur.SOLID);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroupAdminStaff.getCheckedRadioButtonId();
                radioButton = rootView.findViewById(selectedId);
                if (selectedId == -1) {
                    Toast.makeText(getContext(), "please check your position ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Hello " + radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
                    ft.replace(R.id.content_frame, signInFragment, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }

            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroupAdminStaff.getCheckedRadioButtonId();
                radioButton = rootView.findViewById(selectedId);
                if (selectedId == -1) {
                    Toast.makeText(getContext(), "please check your position ", Toast.LENGTH_SHORT).show();
                } else if (selectedId != -1 && radioButton.getText().toString().equals("Admin")) {
                    MainActivity.position = radioButton.getText().toString();
                    Toast.makeText(getContext(), "Hello " + radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
                    ft.replace(R.id.content_frame, adminPassFragment, "findThisFragment2")
                            .addToBackStack(null)
                            .commit();


                } else {
                    MainActivity.position = radioButton.getText().toString();
                    Toast.makeText(getContext(), "Hello " + radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
                    ft.replace(R.id.content_frame, signUpFragment, "findThisFragment3")
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

        return rootView;
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

}
