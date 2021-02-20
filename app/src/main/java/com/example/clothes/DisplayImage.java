package com.example.clothes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class DisplayImage extends AppCompatActivity {
String url;
Uri uri;
PhotoView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        image=(PhotoView) findViewById(R.id.displayimage);
        getSupportActionBar().hide();

        url=getIntent().getStringExtra("IMAGEURL");
        String filename=url.substring(url.lastIndexOf('/')+1);
        setTitle("Clothes Image");
        Picasso.with(this).load(url).fit().centerCrop().placeholder(R.drawable.placeholderimage).into(image);

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
}
