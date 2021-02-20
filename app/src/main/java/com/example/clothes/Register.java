package com.example.clothes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Pattern;
public class Register extends AppCompatActivity {
    TextInputLayout n,em,pa,cp,ph;
    RadioGroup radioGroup;
    RadioButton genderradioButton;
    Button button;
    String id;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    String gender,name,email,pass,cpass,phone;
    DatabaseReference db;
    CardView cardView;
    Animation animation;
    FirebaseAuth firebaseAuth;
    androidx.appcompat.widget.Toolbar toolbar;
    ProgressDialog progressDoalog;
    Pattern pattern;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        n=(TextInputLayout)findViewById(R.id.name);
        em=(TextInputLayout)findViewById(R.id.email);
        builder=new AlertDialog.Builder(this);
        radioGroup=(RadioGroup)findViewById(R.id.rg);


setTitle("Register Yourself");
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
getSupportActionBar().setHomeButtonEnabled(true);
        db=FirebaseDatabase.getInstance().getReference("clothes");
        cardView=findViewById(R.id.card);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animationforacardview);
        cardView.setAnimation(animation);
        pa=(TextInputLayout)findViewById(R.id.pass);
        pattern=Patterns.EMAIL_ADDRESS;
        firebaseAuth=FirebaseAuth.getInstance();
        progressDoalog=new ProgressDialog(this);
        cp=(TextInputLayout)findViewById(R.id.cpass);
        ph=(TextInputLayout)findViewById(R.id.phone);
        button=(Button)findViewById(R.id.register);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                genderradioButton = (RadioButton) findViewById(selectedId);

                name=n.getEditText().getText().toString().trim();
                email=em.getEditText().getText().toString().trim();
                pass=pa.getEditText().getText().toString();
                cpass=cp.getEditText().getText().toString();
                phone=ph.getEditText().getText().toString().trim();
                if(name.isEmpty())
                {
                    n.setError("Please enter the name");
                    n.requestFocus();
                    return;
                }

                if(selectedId==-1){
                    Toast.makeText(getApplicationContext(),"Please select the gender",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    gender=genderradioButton.getText().toString();
                }

                if(email.isEmpty())
                {
                    em.setError("Please enter the name");
                    em.requestFocus();
                    return;
                }
                else if(!pattern.matcher(email).matches())
                {
                    em.setError("Please provide the valid email");

                    em.requestFocus();
                    return;
                }

                if(pass.isEmpty())
                {
                    pa.setError("Please enter The Password");

                    pa.requestFocus();
                    return;
                }
                else if(pass.length()<8)
                {
                    pa.setError("Password should be atleast 8 characters");
                    pa.requestFocus();
                    return;
                }
                if(cpass.isEmpty())
                {
                    cp.setError("Please enter The Password");

                    cp.requestFocus();
                    return;
                }
                else  if(!cpass.equals(pass) && (cpass.length()!=pass.length()) )
                {
                    cp.setError("Both Passwords should match");
                    cp.requestFocus();
                    return;
                }
                if(phone.isEmpty())
                {
                    ph.setError("Please enter the phone");

                    ph.requestFocus();
                    return;
                }
                else if(phone.length()!=10)
                {
                    ph.setError("Please enter the valid phone number");
                    ph.requestFocus();
                    return;
                }
                builder.setTitle("Alert").setCancelable(false).setMessage("Are you sure want to register the account with '"+email+"' and '"+phone+"'").setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
dialogInterface.cancel();
                            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDoalog.setTitle("Registering..");
                        progressDoalog.show();
                        progressDoalog.setCancelable(false);
                        progressDoalog.setCanceledOnTouchOutside(false);
                        id = db.child("PersonalDetails").child(email.substring(0, email.indexOf("@"))).push().getKey();
                        Personal personal = new Personal(id, name, gender, email, phone);
                        if (isNetworkAvailable())
                        {
                        db.child("PersonalDetails").child(email.substring(0, email.indexOf("@"))).child(id).setValue(personal).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            progressDoalog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Register.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                            progressDoalog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Email is  already registered ", Toast.LENGTH_LONG).show();
                                            db.child("PersonalDetails").child(email.substring(0, email.indexOf("@"))).child(id).removeValue();

                                        } else {
                                            progressDoalog.dismiss();
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                            db.child("PersonalDetails").child(email.substring(0, email.indexOf("@"))).child(id).removeValue();

                                        }


                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDoalog.cancel();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                        else
                        {
                            progressDoalog.dismiss();
                            Toast.makeText(getApplicationContext(),"It seems that there is a problem with the network ",Toast.LENGTH_LONG).show();

                        }
                }
                }).setNegativeButton("No",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
                alertDialog=builder.create();
                alertDialog.show();





            }
        });





    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful())
            {
                Toast.makeText(getApplicationContext(),"Please check your mail to activate your account",Toast.LENGTH_LONG).show();
                if(firebaseAuth.getCurrentUser().isEmailVerified())
                {
                    Personal personal=new Personal(first,last,gender,email,pass);
                    db.child("PersonalDetails").child(email.substring(0,email.indexOf("@"))).setValue(personal);
                    Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Register.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not Verified", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });*/
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }





}
