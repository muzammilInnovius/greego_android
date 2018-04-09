package com.greegoapp.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.greegoapp.R;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyLocationBroadcastReceiver;
import com.greegoapp.Utils.SessionManager;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.databinding.ActivitySignUpAgreementBinding;

public class SignUpAgreementActivity extends AppCompatActivity implements View.OnClickListener{
    ActivitySignUpAgreementBinding binding;
    Context context;

    NestedScrollView scrollView;
    CheckBox cbChecked;
    ImageButton ibBack;
    ImageButton ibFinish;
    private View snackBarView;
    RelativeLayout relativeLayout;

    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 108;
    public static MyLocationBroadcastReceiver.callGetClosestListner callGetClosetListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up_agreement);
        context= SignUpAgreementActivity.this;
        snackBarView = findViewById(android.R.id.content);
        bindView();
        setListners();
        checkPermissions();

//        callGetClosetListner = new MyLocationBroadcastReceiver.callGetClosestListner() {
//
//            @Override
//            public void callGetClosest(String lat, String lng) {
//                // TODO Auto-generated method stub
//
////                    Applog.e("ACTIVITY LATTTT", "" + lat + "" + lng);
//                if (SessionManager.progressDialog != null && SessionManager.progressDialog.isShowing()) {
//                    SessionManager.progressDialog.dismiss();
//                }
//            }
//        };
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        } else {


            SessionManager.setLocationService(this);
            // SessionManager.getlattitude()
//            if (!isMyServiceRunning()) {
//                Intent intent = new Intent(RegisterTabActivity.this, MyLocation.class);
//                startService(intent);
//            }

        }
    }

    private void setListners() {

         scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
             @Override
             public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                 if (scrollY > oldScrollY) {
                    relativeLayout.setVisibility(View.VISIBLE);
                     //.hide();
                 } else {
                     relativeLayout.setVisibility(View.INVISIBLE
                     );
                    // fab.show();
                 }
             }
         });
         ibFinish.setOnClickListener(this);
         ibBack.setOnClickListener(this);
    }

    private void bindView() {
        scrollView=binding.svAgreement;
        cbChecked=binding.cbAgreement;
        ibBack=binding.ibBack;
        relativeLayout=binding.rlAgreement;
        ibFinish=binding.ibFinish;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibBack:
                Intent in = new Intent(context, SignUpUserNameActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);

                break;
            case R.id.ibFinish:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callAcceptAgreementAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;
        }
    }

    private void callAcceptAgreementAPI() {
        Intent in = new Intent(context, HomeActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);
    }

    public boolean isValid() {
     //   boolean cbchecked=false;
        if(!cbChecked.isChecked())
        {
            cbChecked.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.no_checked_agreement));
            return false;
        }
                return true;
    }


}
