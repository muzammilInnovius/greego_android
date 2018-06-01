package com.greegoapp.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.SnackBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {

    private final int splashTime = 1000;
    public static boolean isRemember = false;
    public static final String PACKAGE = "com.greegoapp";

    //    ProgressBar customProgressBar;
    int progressStatusCounter = 0;
    Handler progressHandler = new Handler();
    Context context;
    String TAG = SplashActivity.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    LocationManager locationManager;
    String provider;
    private View snackBarView;
//    SmsVerifyCatcher smsVerifyCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace_screen);
        context = SplashActivity.this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        snackBarView = findViewById(android.R.id.content);

        provider = locationManager.getBestProvider(new Criteria(), false);


        if (ConnectivityDetector.isConnectingToInternet(context)) {
            if (Build.VERSION.SDK_INT < 23) {
//                if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
                if (getIntent().getExtras() != null) {

                    if (getIntent().getExtras().getString("trip_id") != null) {

                        String trip_id = getIntent().getExtras().getString("trip_id");
                        String tripStatus = getIntent().getExtras().getString("status");
                        String tripAmount = getIntent().getExtras().getString("trip_amount");
                        String payment_status = getIntent().getExtras().getString("payment_status");

                        Applog.E("tripStatus====>" + tripStatus);

                        if (tripStatus != null) {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("trip_id", trip_id);
                            intent.putExtra("messageTrip", tripStatus);
                            intent.putExtra("tripAmount", tripAmount);
                            startActivity(intent);
                            finish();
                        } else if (payment_status != null) {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("trip_id", trip_id);
                            intent.putExtra("messagePayment", payment_status);
                            startActivity(intent);
                            finish();
                        }

                        Log.d("MyCustomFCMService", "Key: " + "key" + " Value: " + tripStatus + ":" +
                                getIntent().getExtras().getString("trip_id"));

                    } else {
                        startTimer();
                    }
                } else {
                    startTimer();
                }
//                }
            } else {
                if (checkAndRequestPermissions()) {

                    if (getIntent().getExtras() != null) {
                        if (getIntent().getExtras().getString("trip_id") != null) {

                            String trip_id = getIntent().getExtras().getString("trip_id");
                            String tripStatus = getIntent().getExtras().getString("status");
                            String tripAmount = getIntent().getExtras().getString("trip_amount");
                            String payment_status = getIntent().getExtras().getString("payment_status");

                            Applog.E("tripStatus====>" + tripStatus);

                            if (tripStatus != null) {
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("trip_id", trip_id);
                                intent.putExtra("messageTrip", tripStatus);
                                intent.putExtra("tripAmount", tripAmount);
                                startActivity(intent);
                                finish();
                            } else if (payment_status != null) {
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("trip_id", trip_id);
                                intent.putExtra("messagePayment", payment_status);
                                startActivity(intent);
                                finish();
                            }

                            Log.d("MyCustomFCMService", "Key: " + "key" + " Value: " + tripStatus + ":" +
                                    getIntent().getExtras().getString("trip_id"));

                        } else {
                            startTimer();
                        }
                    } else {
                        startTimer();
                    }
                }
            }

        } else

        {
            SnackBar.showInternetError(context, snackBarView);
        }

    }

    private boolean checkAndRequestPermissions() {
        int hasContactPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);
        int AccessFineLocation = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int AccessCorasLocation = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();


        if (AccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (AccessCorasLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (hasContactPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
//            ActivityCompat.requestPermissions(Context, new String[]   {Manifest.permission.RECEIVE_SMS}, PERMISSION_REQUEST_CODE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        startTimer();


                    }
                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }


    private void startTimer() {


        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {

//                    GetKeyHashValue();
//                    doProgress();

                    setSplaceScreen();

                }
            }, splashTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSplaceScreen() {

        if (SessionManager.getIsUserLoggedin(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {

            Intent in = new Intent(SplashActivity.this, MainActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            finish();

        }


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
}
