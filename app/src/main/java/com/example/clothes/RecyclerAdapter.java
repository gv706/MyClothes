package com.example.clothes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.storage.StorageReference;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.List;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>  {
    public Context context;

    private RecyclerViewInterface recyclerviewinterface;
    private  List<details> stu;

    public RecyclerAdapter(Context context, List<details> stu,RecyclerViewInterface recyclerviewinterface) {
        this.context = context;
        this.stu = stu;
        this.recyclerviewinterface=recyclerviewinterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MyViewHolder holder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.singlerowcard,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.date.setText(stu.get(position).date);
        holder.shirts.setText(stu.get(position).shirts);
        holder.pants.setText(stu.get(position).pants);
        holder.tshirts.setText(stu.get(position).tshirts);
        holder.tracks.setText(stu.get(position).tracks);
        holder.shorts.setText(stu.get(position).shorts);
        holder.towels.setText(stu.get(position).towels);
        holder.pillowcovers.setText(stu.get(position).pillowcovers);
        holder.pyjamas.setText(stu.get(position).pyjamas);
        holder.boxers.setText(stu.get(position).boxers);
        holder.blankets.setText(stu.get(position).blankets);
        holder.total.setText(String.valueOf(Integer.parseInt(stu.get(position).shirts)+Integer.parseInt(stu.get(position).pants)+Integer.parseInt(stu.get(position).tshirts)+Integer.parseInt(stu.get(position).shorts)+Integer.parseInt(stu.get(position).towels)+
                Integer.parseInt(stu.get(position).tracks)+Integer.parseInt(stu.get(position).pyjamas)+Integer.parseInt(stu.get(position).pillowcovers)+Integer.parseInt(stu.get(position).blankets)+Integer.parseInt(stu.get(position).boxers)));
        if(stu.get(position).uri.equals(""))
        {
            holder.viewimage.setVisibility(View.GONE);
            holder.showimage.setVisibility(View.GONE);
        }
        else {
            Picasso.with(context).load(stu.get(position).uri).fit().centerCrop().placeholder(R.drawable.placeholderimage).into(holder.showimage);
            holder.viewimage.setVisibility(View.VISIBLE);
            holder.viewimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.showimage.getVisibility()==View.GONE)
                    {
                        holder.showimage.setVisibility(View.VISIBLE);
                        holder.viewimage.setText("Hide Image");

                    }
                    else {
                        holder.showimage.setVisibility(View.GONE);
                        holder.viewimage.setText("View Image");
                    }

                }
            });
            holder.showimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,DisplayImage.class);
                    intent.putExtra("IMAGEURL",stu.get(position).uri);
                    context.startActivity(intent);
                }
            });

        }
       holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               setPosition(holder.getAdapterPosition());
               return false;
           }
       });
        Animation animation= AnimationUtils.loadAnimation(context,R.anim.animationforacardview);
        holder.cardView.setAnimation(animation);




    }
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemCount()
    {
        return stu.size();
    }




    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date,shirts,pants,tshirts,tracks,towels,shorts,blankets,pillowcovers,boxers,pyjamas,total,viewimage;
        PhotoView showimage;
        CardView cardView;
        LinearLayout layout;

        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.layout);
            cardView=itemView.findViewById(R.id.card);
            shirts=itemView.findViewById(R.id.shirts);
            pants=itemView.findViewById(R.id.pants);
            tshirts=itemView.findViewById(R.id.tshirts);
            tracks=itemView.findViewById(R.id.tracks);
            shorts=itemView.findViewById(R.id.shorts);
            towels=itemView.findViewById(R.id.towels);
            pillowcovers=itemView.findViewById(R.id.pillowcovers);
            boxers=itemView.findViewById(R.id.boxers);
            blankets=itemView.findViewById(R.id.blankets);
            pyjamas=itemView.findViewById(R.id.pyjamas);
            date=itemView.findViewById(R.id.date);
            total=itemView.findViewById(R.id.total);
            viewimage=itemView.findViewById(R.id.textviewimage);
            showimage=itemView.findViewById(R.id.showImage);





        }


    }
}

