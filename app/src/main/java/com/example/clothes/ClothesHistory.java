package com.example.clothes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClothesHistory extends AppCompatActivity implements RecyclerViewInterface {
    List<details> arrayList = new ArrayList<>();
    ArrayAdapter<String> ar;
    StorageReference storageReference;
    RecyclerView recyclerView;
    DatabaseReference db;
RecyclerViewInterface recyclerViewInterface;
    Calendar calendar;
AlertDialog.Builder builder;
AlertDialog alertDialog;
    RecyclerAdapter myAdapter;
    String username, cur;
    LinearLayout linearLayout;
    ProgressDialog progressDialogstatistics;
    ProgressDialog progressDialog;
    String sh, tsh, pa, tr, sho, to, pc, bl, date, na, ph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothdetails);
        recyclerView = (RecyclerView) findViewById(R.id.re);
        storageReference = FirebaseStorage.getInstance().getReference("clothes");
        calendar = Calendar.getInstance();
       recyclerViewInterface=this;
        setTitle("Clothes History");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");
        username = email.substring(0, email.indexOf('@'));
        progressDialogstatistics = new ProgressDialog(this);
        progressDialogstatistics.setMessage("Loading history..");
        progressDialogstatistics.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialogstatistics.setCancelable(false);
        progressDialogstatistics.setCanceledOnTouchOutside(false);
        progressDialogstatistics.show();
        if(isNetworkAvailable())
        {
            db = FirebaseDatabase.getInstance().getReference("clothes");
            db.child("Clothes History").child(username).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    details det = dataSnapshot.getValue(details.class);
                    progressDialogstatistics.dismiss();
                    arrayList.add(0, det);
                    adapter();


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
            db.child("Clothes History").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialogstatistics.dismiss();
                    if (!dataSnapshot.exists()) {

                        linearLayout.setVisibility(View.VISIBLE);
                    } else {
                        linearLayout.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            progressDialogstatistics.dismiss();
            Toast.makeText(getApplicationContext(),"It seems that there is a problem with the network ",Toast.LENGTH_LONG).show();
            finish();
        }

    }
    private void adapter() {
        myAdapter=new RecyclerAdapter(this,arrayList,this);
        recyclerView.setAdapter(myAdapter);
        registerForContextMenu(recyclerView);


    }

    @Override
    public void onDeleteItemClick(final int position){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String uri = arrayList.get(position).uri;
        if (uri.equals("")) {
            db.child("Clothes History").child(username).child(arrayList.get(position).id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Selected card deleted", Toast.LENGTH_LONG).show();
                    arrayList.remove(position);
                    myAdapter.notifyDataSetChanged();
                    myAdapter.notifyItemChanged(position);
                    myAdapter.notifyItemRemoved(position);
                    progressDialog.dismiss();
                    if (arrayList.size() == 0) {
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        } else {
            cur = DateFormat.getDateInstance().format(calendar.getTime());
            StorageReference ImageReference = FirebaseStorage.getInstance().getReferenceFromUrl(arrayList.get(position).uri);
            ImageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    db.child("Clothes History").child(username).child(arrayList.get(position).id).removeValue();
                    Toast.makeText(getApplicationContext(), "Selected card deleted", Toast.LENGTH_LONG).show();
                    arrayList.remove(position);
                    myAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    myAdapter.notifyItemChanged(position);
                    myAdapter.notifyItemRemoved(position);
                    if (arrayList.size() == 0) {
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        }



    }
    public boolean isNetworkAvailable() {
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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.deletemenu,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.delete)
        {
            final int position=myAdapter.getPosition();
            builder=new AlertDialog.Builder(this);
            builder.setTitle("Alert");
            builder.setCancelable(true);
            builder.setMessage("Want to delete the selected card?");
            builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recyclerViewInterface.onDeleteItemClick(position);
                    dialogInterface.cancel();
                }
            });
            builder.setNegativeButton("No",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alertDialog=builder.create();
            alertDialog.show();
        }
        return super.onContextItemSelected(item);
    }
}
