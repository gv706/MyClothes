package com.example.clothes;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

public class NetworkChangeReceiver extends BroadcastReceiver {

    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    @Override
    public void onReceive(final Context context, Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (!wifi.isAvailable() && !mobile.isAvailable()) {
            builder=new AlertDialog.Builder(context);
                            builder.setTitle("Alert");
                           builder.setCancelable(false);

                            builder.setMessage("Internet Connection Failed");
                            builder.setPositiveButton("Open Settings",new DialogInterface.OnClickListener(){
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {

                                  dialogInterface.cancel();
                                   Intent intent=new  Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                   context.startActivity(intent);


                               }
                            });
                            builder.setNegativeButton("Exit",new DialogInterface.OnClickListener(){
                               @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                   MainActivity m=new MainActivity();
                                   m.finish();
                                   dialogInterface.cancel();


                               }
                           });
                          alertDialog=builder.create();
                            alertDialog.show();
    }

    }
}
