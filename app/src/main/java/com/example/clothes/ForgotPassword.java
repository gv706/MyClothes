package com.example.clothes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextInputLayout emailinforgotpassword;
    Button sendemail;
    CardView cardView;
    Animation animation;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        firebaseAuth=FirebaseAuth.getInstance();
        setTitle("Reset Your Password");
        cardView=findViewById(R.id.card);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animationforacardview);
        cardView.setAnimation(animation);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        emailinforgotpassword=(TextInputLayout)findViewById(R.id.emailinrestpassword);
        progressDialog=new ProgressDialog(this);
        sendemail=(Button)findViewById(R.id.sendemail);
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String emailinfp=emailinforgotpassword.getEditText().getText().toString();
                Pattern pattern= Patterns.EMAIL_ADDRESS;
                if(TextUtils.isEmpty(emailinfp)){

                    emailinforgotpassword.setError("Please enter your email");
                    return;
                }
                else  if(!pattern.matcher(emailinfp).matches())
                {
                    emailinforgotpassword.setError("Please enter the valid email");
                    return;
                }
                progressDialog.setMessage("Please wait..");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                if(isNetworkAvailable()) {
                    firebaseAuth.sendPasswordResetEmail(emailinfp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(), "Reset password link is sent.Please check your email", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                                if (netInfo == null)
                                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "It seems that there is a problem with the network", Toast.LENGTH_LONG).show();

                }


            }
        });

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
