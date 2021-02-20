package com.example.clothes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextInputLayout email,password;
    Button login;
    TextInputEditText e,p;
    TextView textView,navtosignup,forgotpassword;
    FirebaseAuth.AuthStateListener fire;

    CardView cardView;
    Animation animation;
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        cardView=findViewById(R.id.card);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animationforacardview);
        cardView.setAnimation(animation);
        firebaseAuth=FirebaseAuth.getInstance();
        textView=(TextView)findViewById(R.id.textautherror);
        forgotpassword=(TextView)findViewById(R.id.forgotpassword);
        email=(TextInputLayout) findViewById(R.id.email);
        password=(TextInputLayout) findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        progressDoalog=new ProgressDialog(this);

        e=(TextInputEditText)findViewById(R.id.e);
        p=(TextInputEditText)findViewById(R.id.p);
        navtosignup=(TextView)findViewById(R.id.signup);
        navtosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.getEditText().setText("");
                password.getEditText().setText("");
                textView.setText("");
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                String em=email.getEditText().getText().toString().trim();
                Pattern pattern=Patterns.EMAIL_ADDRESS;
                String pa=password.getEditText().getText().toString();
                if(em.isEmpty()) {
                    email.setError("Please enter the Email");
                    password.setError(null);
                    email.requestFocus();
                    return;
                }
                else if(!pattern.matcher(em).matches())
                {
                    email.setError("Please provide the valid email");
                    password.setError(null);
                    email.requestFocus();
                    return;
                }

                if(pa.isEmpty())
                {
                    password.setError("Please enter The Password");
                    email.setError(null);
                    password.requestFocus();
                    return;
                }
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.setTitle("Signing in..");
                progressDoalog.show();
                progressDoalog.setCancelable(false);
                progressDoalog.setCanceledOnTouchOutside(false);
                if(isNetworkAvailable()) {
                    firebaseAuth.signInWithEmailAndPassword(em, pa).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                progressDoalog.dismiss();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressDoalog.dismiss();
                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                                if (netInfo == null)
                                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                                else {
                                    textView.setText("*email or password is incorrect");
                                }
                            }

                        }
                    });
                }
                else {
                    progressDoalog.dismiss();
                    Toast.makeText(getApplicationContext(), "It seems that there is a problem with the network", Toast.LENGTH_LONG).show();

                }
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.getEditText().setText("");
                password.getEditText().setText("");
                textView.setText("");
                Intent intent=new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onBackPressed() {

        ActivityCompat.finishAffinity(this);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
