package com.example.clothes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.clothes.MainActivity;

public class IconDisplay extends AppCompatActivity  {

    ProgressBar progressBar;
    int progressStatus=0;
    Animation animation;
    ImageView clothesicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_icon_display);
        clothesicon=findViewById(R.id.clothesicon);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animationtoicon);
        clothesicon.setVisibility(View.VISIBLE);
        clothesicon.setAnimation(animation);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_cyclic);
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 50) {
                    progressStatus += 1;
                    progressBar.setProgress(progressStatus);
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent=new Intent(IconDisplay.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();


    }

}
