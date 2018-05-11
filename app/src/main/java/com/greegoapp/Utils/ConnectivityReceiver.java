package com.greegoapp.Utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.greegoapp.Activity.HomeActivity;

public class ConnectivityReceiver extends BroadcastReceiver {
    AlertDialog dialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("app", "Network connectivity change");

        if (intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
//                Intent i = new Intent(context, HomeActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("app", "There's no network connectivity");
//                Intent i = new Intent(context, HomeActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);

//                Toast.makeText(context, "Please check internet connection.", Toast.LENGTH_LONG).show();

            }

        }
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            // Make an action or refresh an already managed state.
            Toast.makeText(context, "connect GPS", Toast.LENGTH_LONG).show();
        }
    }


}
