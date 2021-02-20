package com.example.clothes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Context;

public class MainActivity extends AppCompatActivity {
Button b,b1;
String email;
CardView cardView;
Animation animation;
FirebaseAuth.AuthStateListener fire;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardView=findViewById(R.id.card);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animationforacardview);
        cardView.setAnimation(animation);
        firebaseAuth = FirebaseAuth.getInstance();
        fire = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                } else
                    email = firebaseAuth.getCurrentUser().getEmail();

            }
        };


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loginmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.account);
        if (email != null) {

            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    Intent intent=new Intent(MainActivity.this,ShowProfile.class);
                    intent.putExtra("EMAIL",email.substring(0, email.indexOf('@')));
                    startActivity(intent);
                    return false;
                }
            });
            b=(Button)findViewById(R.id.b2);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(MainActivity.this, yourclothes.class);
                    i.putExtra("EMAIL",email);
                    startActivity(i);
                }
            });
            b1=(Button)findViewById(R.id.b1);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,ClothesHistory.class);
                    intent.putExtra("EMAIL",email);
                    startActivity(intent);
                }
            });
        }


        return super.onCreateOptionsMenu(menu);
    }

    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(fire);

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.signout: {
                FirebaseAuth.getInstance().signOut();

            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);

    }
}
