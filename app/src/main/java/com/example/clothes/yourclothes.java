package com.example.clothes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class yourclothes extends AppCompatActivity {
    String shirt,pant,tshirt,track,towel,boxer,pillowcover,blanket,pyjama,shor,email;
    Button b;
    Uri ringtoneuri;
    ImageButton b2;
    List<Personal>list;

    DatabaseReference db;
    Context context=this;
    String username;
    String id1,photostring;
PhotoView chooseimage;
    AlertDialog alert1;
    String cur;
    StorageReference storageReference;
    Uri filepath;
    AlertDialog.Builder builder;
    TextView textflag;
    ProgressDialog progressDialog;

    int PICKIMAGEREQUEST=0;
    Calendar calendar = Calendar.getInstance();

    CardView cardView;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourclothes);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Clothes");
        cardView=findViewById(R.id.card);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animationforacardview);
        cardView.setAnimation(animation);
        b=(Button)findViewById(R.id.sub);
        builder=new AlertDialog.Builder(this);
        storageReference= FirebaseStorage.getInstance().getReference("clothes");
        textflag=(TextView)findViewById(R.id.textflag);

        chooseimage=(PhotoView) findViewById(R.id.chooseImage);
        list= new ArrayList<>();
        Intent intent=getIntent();
        email=intent.getStringExtra("EMAIL");
        username=email.substring(0,email.indexOf('@'));
        if(isNetworkAvailable())
        {
            db=FirebaseDatabase.getInstance().getReference("clothes");
            db.child("PersonalDetails").child(username).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Personal personal=dataSnapshot.getValue(Personal.class);
                    list.add(personal);

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
            } );

        }
        else {
            Toast.makeText(getApplicationContext(),"Network seems to be down",Toast.LENGTH_LONG).show();

            finish();
        }

        b2=(ImageButton)findViewById(R.id.choosefile);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select the file"),PICKIMAGEREQUEST);

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    shirt = ((EditText) findViewById(R.id.shirts)).getText().toString().trim();
                pant = ((EditText) findViewById(R.id.pants)).getText().toString().trim();
                tshirt = ((EditText) findViewById(R.id.tshirts)).getText().toString().trim();
                towel = ((EditText) findViewById(R.id.towels)).getText().toString().trim();
                pillowcover = ((EditText) findViewById(R.id.pillowcovers)).getText().toString().trim();
                shor = ((EditText) findViewById(R.id.shorts)).getText().toString().trim();
                blanket = ((EditText) findViewById(R.id.blankets)).getText().toString().trim();
                pyjama = ((EditText) findViewById(R.id.pyjamas)).getText().toString().trim();
                track = ((EditText) findViewById(R.id.tracks)).getText().toString().trim();
                boxer = ((EditText) findViewById(R.id.boxers)).getText().toString().trim();
                if (shirt.equals("") && pant.equals("") && tshirt.equals("") && towel.equals("") && pillowcover.equals("") && shor.equals("") && blanket.equals("") && pyjama.equals("") && track.equals("") && boxer.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter the data", Toast.LENGTH_LONG).show();
                    return;
                }

                if (shirt.equals("")) {
                    shirt = "0";
                }

                if (pant.equals("")) {
                    pant = "0";
                }
                if (tshirt.equals("")) {
                    tshirt = "0";
                }
                if (track.equals("")) {
                    track = "0";
                }
                if (shor.equals("")) {
                    shor = "0";
                }
                if (towel.equals("")) {
                    towel = "0";
                }
                if (blanket.equals("")) {
                    blanket = "0";
                }
                if (pillowcover.equals("")) {
                    pillowcover = "0";
                }
                if (boxer.equals("")) {
                    boxer = "0";
                }
                if (pyjama.equals("")) {
                    pyjama = "0";
                }

                builder.setTitle("Alert");
                if (filepath == null) {
                    builder.setMessage("Are you sure want to add your clothes without attaching the file?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            String id = db.child(username).push().getKey();
                            cur = DateFormat.getDateInstance().format(calendar.getTime());
                            details d = new details(id, cur, list.get(0).name, list.get(0).phonenumber, shirt, pant, tshirt, track, boxer, towel, shor, blanket, pillowcover, pyjama, "");
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            progressDialog.setTitle("Uploading..");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            if(isNetworkAvailable()) {

                                db.child("Clothes History").child(username).child(id).setValue(d).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @SuppressLint("IntentReset")
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Clothes Added Successfully", Toast.LENGTH_LONG).show();
                                        //sendingmessage;
                                       /* NotificationCompat.BigTextStyle bigTextStyle=new NotificationCompat.BigTextStyle();
                                        bigTextStyle.bigText("You had given the clothes to dhobi on "+cur);
                                        bigTextStyle.setSummaryText("Android");*/
                                        ringtoneuri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone ringtone=RingtoneManager.getRingtone(getApplicationContext(),ringtoneuri);
                                        ringtone.play();
                                        Intent intent=new Intent(yourclothes.this,ClothesHistory.class);
                                        intent.putExtra("EMAIL",email);
                                        PendingIntent pendingIntent=PendingIntent.getActivity(yourclothes.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            NotificationChannel notificationChannel = new NotificationChannel("Clothes_01", "Clothes Notifications", NotificationManager.IMPORTANCE_DEFAULT);

                                            // Configure the notification channel.
                                            notificationChannel.setDescription("Channel description");
                                            notificationChannel.enableLights(true);
                                            notificationChannel.setLightColor(Color.RED);
                                            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                                            notificationChannel.enableVibration(true);
                                            notificationManager.createNotificationChannel(notificationChannel);
                                        }
                                        NotificationCompat.Builder builder=new NotificationCompat.Builder(yourclothes.this,"Clothes_01")
                                                .setSmallIcon(R.mipmap.clothimg)
                                                .setContentText("You have added the clothes on "+cur)
                                                .setSound(ringtoneuri)
                                                .setDefaults(0)
                                                .setAutoCancel(true)
                                                .addAction(R.mipmap.clothimg,"Show Activity",pendingIntent);
                                        builder.setContentIntent(pendingIntent);
                                        notificationManager.notify(0,builder.build());
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                      finish();

                                    }
                                });
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"It seems that there is a problem with the network ",Toast.LENGTH_LONG).show();

                            }


                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();

                        }
                    });
                    alert1 = builder.create();
                    alert1.show();

                } else {
                    builder.setMessage("Are you sure want to add your clothes with attached file?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            progressDialog.setTitle("Uploading..");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            if (isNetworkAvailable())
                            {
                                id1 = db.child(username).push().getKey();
                            cur = DateFormat.getDateInstance().format(calendar.getTime());
                            final StorageReference riversRef = storageReference.child(username + "/" + cur + "/" + id1 + "/Clothesimage." + getFileExension(filepath));
                            riversRef.putFile(filepath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Get a URL to the uploaded content
                                            final Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    photostring = uri.toString();
                                                    progressDialog.dismiss();
                                                    details d = new details(id1, cur, list.get(0).name, list.get(0).phonenumber, shirt, pant, tshirt, track, boxer, towel, shor, blanket, pillowcover, pyjama, photostring);
                                                    db.child("Clothes History").child(username).child(id1).setValue(d);
                                                    Toast.makeText(getApplicationContext(), "Clothes Added Successfully", Toast.LENGTH_LONG).show();
                                                    ringtoneuri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                    Ringtone ringtone=RingtoneManager.getRingtone(getApplicationContext(),ringtoneuri);
                                                    ringtone.play();
                                                    final NotificationCompat.BigPictureStyle bigPictureStyle=new NotificationCompat.BigPictureStyle();
                                                    Picasso.with(context).load(filepath).into(new Target() {
                                                        @Override
                                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                            // Set it in the ImageView
                                                            bigPictureStyle.bigPicture(bitmap);
                                                            Intent intent=new Intent(yourclothes.this,ClothesHistory.class);
                                                            intent.putExtra("EMAIL",email);
                                                            PendingIntent pendingIntent=PendingIntent.getActivity(yourclothes.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                                            NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                NotificationChannel notificationChannel = new NotificationChannel("Clothes_01", "Clothes Notifications", NotificationManager.IMPORTANCE_DEFAULT);

                                                                // Configure the notification channel.
                                                                notificationChannel.setDescription("Channel description");
                                                                notificationChannel.enableLights(true);
                                                                notificationChannel.setLightColor(Color.RED);
                                                                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                                                                notificationChannel.enableVibration(true);
                                                                notificationManager.createNotificationChannel(notificationChannel);
                                                            }
                                                            NotificationCompat.Builder builder=new NotificationCompat.Builder(yourclothes.this,"Clothes_01")
                                                                    .setSmallIcon(R.mipmap.clothimg)
                                                                    .setContentText("You have added the Clothes on "+cur)
                                                                    .setSound(ringtoneuri).setDefaults(0)
                                                                    .setStyle(bigPictureStyle)
                                                                    .setAutoCancel(true)
                                                                    .addAction(R.mipmap.clothimg,"Show Activity",pendingIntent);
                                                            builder.setContentIntent(pendingIntent);
                                                            notificationManager.notify(0,builder.build());


                                                        }
                                                        @Override
                                                        public void onBitmapFailed(Drawable errorDrawable) {
                                                        }
                                                        @Override
                                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                        }
                                                    });
                                                              finish();

                                                }
                                            });

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            // ...
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                            finish();

                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        }
                                    });

                        }
                        else

                        {
                            progressDialog.cancel();
                            Toast.makeText(getApplicationContext(),"It seems that there is a problem with the network ",Toast.LENGTH_LONG).show();

                        }
                    }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();

                        }
                    });
                    alert1 = builder.create();
                    alert1.show();
                }


            }
        });


    }

    private void sendingmessage() {
        int p=ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);
        if(p==PackageManager.PERMISSION_GRANTED)
        {
            message();

        }
        else
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);


    }

    public void message()
    {
System.out.println("hello hi");
       String me="Hello "+list.get(0).name+"\nYou Have Given the Following Clothes to Dhobi\nShirts:"+shirt+"\nTshirts:"+tshirt+
               "\nTracks:"+track+"\nTowels:"+towel+"\nShorts:"+shor+"\nPants:"+pant+"\nPillowCovers:"+pillowcover+"\nBlankets:"+blanket+"\nBoxers:"+boxer+"\nPyjamas"+pyjama;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("+91"+list.get(0).phonenumber, null, me, null, null);
        Toast.makeText(this, "Message Sent to your Mobile Number "+list.get(0).phonenumber, Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICKIMAGEREQUEST&&data!=null&&data.getData()!=null&&resultCode==RESULT_OK)
        {
            filepath=data.getData();
            chooseimage.setVisibility(View.VISIBLE);
            Picasso.with(this).load(filepath).into( chooseimage);
                textflag.setText("File chosen");
        }
        else {
            chooseimage.setVisibility(View.GONE);
            textflag.setText("File not choosen");
        }
    }
    public String getFileExension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

     return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
