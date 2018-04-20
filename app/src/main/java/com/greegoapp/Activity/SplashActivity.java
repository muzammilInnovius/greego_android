package com.greegoapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.greegoapp.Fragment.MapHomeFragment;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.greegoapp.Fragment.MapHomeFragment.MY_PERMISSIONS_REQUEST_ACCOUNTS;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace_screen);
        context = SplashActivity.this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        getIds();

        if (ConnectivityDetector.isConnectingToInternet(context)) {
            if (Build.VERSION.SDK_INT < 23) {
                if (checkAndRequestPermissions()) {
                    //If you have already permitted the permission
                    startTimer();
                    //        initialize GoogleMaps
                    // create GoogleApiClient
//                    createGoogleApi();

                }

            } else {
                if (checkAndRequestPermissions()) {
                    //If you have already permitted the permission
                    startTimer();
                    //        initialize GoogleMaps
                    // create GoogleApiClient
//                    createGoogleApi();

                }
            }
//            CheckGpsStatus();
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkAndRequestPermissions() {
        int AccessFineLocation = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int AccessCorasLocation = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (AccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (AccessCorasLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
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
//                        if (mGoogleApiClient == null) {
//                            setUpGoogleApiClient();
//                        }
//                        mGoogleMap.setMyLocationEnabled(true);
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



    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                SplashActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCOUNTS
        );
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

    private void getIds() {

    }

    private void doProgress() {
        try {
            while (progressStatusCounter < 100) {
                progressStatusCounter += 2;
                progressHandler.post(new Runnable() {
                    public void run() {
//                        customProgressBar.setProgress(progressStatusCounter);
                    }
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void GetKeyHashValue() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

//                ((TextView) findViewById(R.id.hashKey)).setText(Base64.encodeToString(md.digest(),Base64.NO_WRAP));

                Applog.e("HAsh", "Key:::" + Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP));

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.e("Error", e.getMessage(), e);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
