package com.example.thedeveloper.gtsaw.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thedeveloper.gtsaw.Main2Activity;
import com.example.thedeveloper.gtsaw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by the developer on 08/01/2018.
 */

public class SignInFragment extends Fragment {
    TextView textViewTile, textViewResetPassword;
    private EditText editTextEmail, editTextPassword;
    Button buttonSign;
    Button buttonFaceBook;
    String Email;
    String Password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        textViewTile = rootView.findViewById(R.id.title_sign_in);
        textViewResetPassword = rootView.findViewById(R.id.tv_forget_password);
        applyBlurMaskFilter(textViewResetPassword, BlurMaskFilter.Blur.SOLID);
        applyBlurMaskFilter(textViewTile, BlurMaskFilter.Blur.SOLID);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        editTextEmail = rootView.findViewById(R.id.email_l);
        editTextPassword = rootView.findViewById(R.id.password_l);
        buttonSign = rootView.findViewById(R.id.login_btn);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();



        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Email = editTextEmail.getText().toString();
                Password = editTextPassword.getText().toString();
                if (TextUtils.isEmpty(Email)) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), "please enter Username  ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), "please enter your Password ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(Email)) {
                    progressDialog.hide();
                    editTextEmail.setError("Invalid Email");
                }

                if (!isValidPassword(Email)) {
                    progressDialog.hide();
                    editTextPassword.setError("Invalid Password  ");
                }
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.hide();
                            Snackbar.make(view, "Successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            startActivity(new Intent(getContext(), Main2Activity.class));
                            getActivity().finish();
                        } else {
                            progressDialog.hide();
                            Snackbar.make(view, "Wrong password or e-mail", Snackbar.LENGTH_LONG).setAction("Action", null).show();

//                                    Toast.makeText(getApplicationContext(), " Wrong password or e-mail ", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        return rootView;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

    protected void applyBlurMaskFilter(TextView tv, BlurMaskFilter.Blur style) {

        float radius = tv.getTextSize() / 10;
        BlurMaskFilter filter = new BlurMaskFilter(radius, style);
        tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        tv.getPaint().setMaskFilter(filter);
    }
}

