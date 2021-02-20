package com.example.clothes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Person;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothes.Personal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShowProfile extends AppCompatActivity {
    DatabaseReference db;
    ImageView editprofile;
    TextView firstlastnames,gender,email,phone;
    ProgressDialog progressDialog;
    EditText name,mobileno;
    Button ok,cancel;
    CardView cardView;
    Animation animation;
    Context context=this;
    List<Personal> list=new ArrayList<>();
    AlertDialog.Builder builder,builder1;
    AlertDialog alertDialog,alertDialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        firstlastnames=(TextView)findViewById(R.id.fl);
        gender=(TextView)findViewById(R.id.g);
        email=(TextView)findViewById(R.id.e);
        phone=(TextView)findViewById(R.id.p);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Profile");
        editprofile=(ImageView)findViewById(R.id.editprofile);
        cardView=findViewById(R.id.card);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animationforacardview);
        cardView.setAnimation(animation);
        if(isNetworkAvailable()) {
        builder=new AlertDialog.Builder(this);
        builder1=new AlertDialog.Builder(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading profile..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Intent intent=getIntent();
        String username=intent.getStringExtra("EMAIL");

            db = FirebaseDatabase.getInstance().getReference("clothes").child("PersonalDetails").child(username);
            db.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    progressDialog.cancel();
                    Personal personal = dataSnapshot.getValue(Personal.class);
                    list.add(personal);
                    firstlastnames.setText(personal.name);
                    gender.setText(personal.gender);
                    email.setText(personal.email);
                    phone.setText(personal.phonenumber);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            editprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    builder.setCancelable(false).setMessage("Want to edit the profile?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            View view1 = getLayoutInflater().inflate(R.layout.editprofiledetails, null);
                            builder1.setView(view1);
                            ok = (Button) view1.findViewById(R.id.ok);
                            cancel = (Button) view1.findViewById(R.id.cancel);
                            name = (EditText) view1.findViewById(R.id.name);
                            mobileno = (EditText) view1.findViewById(R.id.mobileno);
                            alertDialog1 = builder1.create();
                            alertDialog1.show();
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String f = name.getText().toString().trim();
                                    final String l = mobileno.getText().toString().trim();
                                    if (f.length() == 0) {
                                        name.setError("Enter the name");
                                        return;
                                    } else if (l.length() == 0) {
                                        mobileno.setError("Enter the mobile number");
                                        return;
                                    } else if (l.length() != 10) {
                                        mobileno.setError("Enter the valid mobile number");
                                        return;
                                    } else {
                                        alertDialog1.cancel();
                                        if(isNetworkAvailable()){
                                            progressDialog=new ProgressDialog(context);
                                            progressDialog.setMessage("Updating profile..");
                                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            progressDialog.setCancelable(false);
                                            progressDialog.setCanceledOnTouchOutside(false);
                                            progressDialog.show();
                                            db.child(list.get(0).id).child("name").setValue(f).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    db.child(list.get(0).id).child("phonenumber").setValue(l).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getApplicationContext(), "Successfully updated your profile", Toast.LENGTH_LONG).show();

                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                                                }
                                            });

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"Can't update your profile data.It seems that there is a problem with the network",Toast.LENGTH_LONG).show();

                                        }}

                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog1.cancel();

                                }
                            });


                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();


                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Can't load your profile data.It seems that there is a problem with the network",Toast.LENGTH_LONG).show();
finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
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
