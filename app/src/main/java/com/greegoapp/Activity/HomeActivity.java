package com.greegoapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.greegoapp.Adapter.DrawerLayoutAdapter;
import com.greegoapp.AppController.AppController;
import com.greegoapp.Fragment.MapHomeFragment;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.greegoapp.Fragment.MapHomeFragment.COMPLETE_UPDATE_USER_DETAILS;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, CallFragmentInterface, BackPressedFragment {

    String MAIN_TAG = HomeActivity.class.getSimpleName();
    ActivityHomeBinding binding;
    Context context;
    private View snackBarView;

    private RelativeLayout navHeader;
    private ImageView ivPro, ivProPicHome;
    private DrawerLayoutAdapter drawerLayoutAdapter;
    private DrawerLayout drawer_layout;
    private ListView menuList;
    private static RelativeLayout drawerlist;
    android.app.FragmentManager manager;
    private FrameLayout container_body;
    TextView tvDrawUsername, tvDriveGreego;
    private Fragment mContentFragment = null;
    private String[] drawerTitle = {"Home", "Payment", "Your Trips", "Free Rides", "Help", "Settings"};
    public static int index = 0;
    private Stack<Fragment> fragmentStack;
    FragmentManager fragmentManager;

    //Pregnesh
    public static final int MY_PERMISSION_REQUEST = 1;
    LocationManager locationManager;
    public boolean isGPSEnable = false;
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static boolean location_Success = false;

    GetUserData.DataBean userDetails;
    ArrayList<GetUserData> alUserList;
    String userName;

    public static final int HOME_SLIDER_PROFILE_UPDATE = 1000;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerLayoutAdapter = new DrawerLayoutAdapter(HomeActivity.this, drawerTitle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        context = HomeActivity.this;
        fragmentStack = new Stack<Fragment>();
        fragmentManager = this.getSupportFragmentManager();
        snackBarView = findViewById(android.R.id.content);

        manager = getFragmentManager();

        bindView();
//        setHeaderbar();
        setListners();
        if (ConnectivityDetector.isConnectingToInternet(context)) {
            callUserMeApi();
//            CheckGpsStatus();
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }


        slideMenu();
        drawerlist.requestDisallowInterceptTouchEvent(true);
        navHeader.requestDisallowInterceptTouchEvent(true);

    }


//    private HeaderBar headerBar;
//
//    private void setHeaderbar() {
//        try {
//            headerBar = binding.headerBar;
//            headerBar.ivLeft.setImageResource(R.mipmap.ic_profile);
//            headerBar.ivLeft.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    openDrawer();
//                }
//            });
//            headerBar.rrHomeBtn.setVisibility(View.GONE);
//
//            headerBar.ivRight.setVisibility(View.GONE);
//            headerBar.ivRightOfLeft.setVisibility(View.GONE);
//
//            headerBar.tvTitle.setVisibility(View.VISIBLE);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    private void setHomeValues() {

        if (ConnectivityDetector.isConnectingToInternet(this)) {
            if (isGPSEnable()) {
                try {

                    Fragment fragmentPro = null;
                    fragmentPro = new MapHomeFragment().newInstance(alUserList, "");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.replace(R.id.containerBody, fragmentPro);
                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
                    fragmentTransaction.commitAllowingStateLoss();
                    mContentFragment = fragmentPro;
                    drawer_layout.closeDrawer(drawerlist);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                displayLocationSettingsRequest();
            }
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setListners() {
        ivProPicHome.setOnClickListener(this);
        ivPro.setOnClickListener(this);
        tvDrawUsername.setOnClickListener(this);
    }

    private void bindView() {

        navHeader = binding.navHeader;
        drawer_layout = binding.drawerLayout;
        ivProPicHome = binding.ivProPicHome;
//        headerBar = binding.headerBar;
        container_body = binding.containerBody;
        drawerlist = binding.drawerlist;
        ivPro = binding.ivPro;
        tvDrawUsername = binding.tvDrawUsername;
        menuList = binding.menuList;
        tvDriveGreego = binding.tvDriveGreego;

        ivProPicHome.setVisibility(View.VISIBLE);
    }

    Intent in;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivProPicHome:
                callRegisterUpdateComplete();
                break;

            case R.id.ivPro:
                in = new Intent(HomeActivity.this, UserProfileActivity.class);
                startActivityForResult(in, HOME_SLIDER_PROFILE_UPDATE);
                break;

            case R.id.tvDrawUsername:
                in = new Intent(HomeActivity.this, UserProfileActivity.class);
                startActivityForResult(in, HOME_SLIDER_PROFILE_UPDATE);
                break;


        }
    }

    private void callRegisterUpdateComplete() {
        if (COMPLETE_UPDATE_USER_DETAILS != 0007) {
            if (alUserList.size() != 0) {

                for (GetUserData userData : alUserList) {
                    if (userData.getData().getProfile_pic() != null && userData.getData().getVehicles().size()
                            != 0 && userData.getData().getCards().size() != 0) {
                        openDrawer();
                    } else {
                        showCheckUserUpdateData("Please complete your updates before proceeding.");
                    }
                }
            }
        } else {
            openDrawer();
        }
    }

    private void showCheckUserUpdateData(String msg) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("Greego").setMessage(msg);


            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        dialog.dismiss();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
            });

//            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.dismiss();
//                }
//            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openDrawer() {
        try {
//            setvalues();

            container_body.setClickable(false);
            drawerlist.setClickable(true);
            navHeader.setClickable(true);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            menuList.invalidateViews();
            drawer_layout.openDrawer(drawerlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeDrawer() {
        try {
            if (drawer_layout.isDrawerOpen(drawerlist))
                drawer_layout.closeDrawer(drawerlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void slideMenu() {
        try {
            DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) drawerlist.getLayoutParams();
            drawerlist.setLayoutParams(layoutParams);

            menuList.setAdapter(drawerLayoutAdapter);
            if (drawer_layout.isDrawerOpen(drawerlist)) {
                drawer_layout.closeDrawer(drawerlist);
            }

            menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                    drawer_layout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            super.onDrawerClosed(drawerView);
                        }
                    });

                    drawer_layout.closeDrawer(drawerlist);
                    index = pos;

                    displayView(pos);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void displayView(final int pos) {
        try {
            closeDrawer();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Fragment fragment = null;
                    Intent in;
                    switch (pos) {
                        case 0:
                            in = new Intent(context, HomeActivity.class);
                            startActivity(in);
                            finish();
                            break;
                        case 1:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, PaymentActivity.class);
                            startActivity(in);
                            break;

                        case 2:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, TripHistoryActivity.class);
                            startActivity(in);
//                            fragment = new TripHistoryFragment().newInstance("", "");
                            break;

                        case 3:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, FreeTripsActivity.class);
                            startActivity(in);
//                            fragment = new FreeTripsFragment().newInstance("", "");
                            break;

                        case 4:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, HelpActivity.class);
                            startActivity(in);
//                            fragment = new HelpFragment().newInstance("", "");
                            break;

                        case 5:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, SettingActivity.class);
                            startActivity(in);
//                            fragment = new SettingFragment().newInstance("", "");
                            break;

//                        default:
//                            break;
                    }

//                    if (fragment != null) {
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.containerBody, fragment);
//                        fragmentTransaction.commit();
//                        mContentFragment = fragment;
//                    }

                    drawer_layout.closeDrawer(drawerlist);
                    drawerLayoutAdapter.setSelectedIndex(pos);
                }
            }, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed(Context context) {
        try {
            onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 2) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();

        } else {
            finish();
        }


//        if (container_body.getChildCount() == 1) {
//            super.onBackPressed();
//            if (container_body.getChildCount() == 0) {
//               finish();
//                // load your first Fragment here
//            }
//        } else if (container_body.getChildCount() == 0) {
//            setHomeValues();
//            // load your first Fragment here
//        } else {
//            super.onBackPressed();
//        }
    }


    @Override
    public void onFragmentCall(Context mContext, Fragment myFragment, String Tag) {
        if (container_body.getVisibility() == View.GONE) {
            container_body.setVisibility(View.VISIBLE);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerBody, myFragment, Tag);
        fragmentStack.push(myFragment);
        fragmentTransaction.commit();
    }


// pragnesh


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                setHomeValues();
                break;
            case Activity.RESULT_CANCELED:
                break;

            case HOME_SLIDER_PROFILE_UPDATE:
                userName = data.getStringExtra("name");
                tvDrawUsername.setText(userName);
                break;
            case COMPLETE_UPDATE_USER_DETAILS:
                callRegisterUpdateComplete();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Premission Granted Successfully.
                    setHomeValues();
                } else {
                    //  Did not Granted Permission.
                }
                break;
        }
    }

    public boolean isGPSEnable() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnable;
    }


    public boolean displayLocationSettingsRequest() {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(MAIN_TAG, "All location settings are satisfied.");
                        location_Success = true;
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(MAIN_TAG, "Location settings are not satisfied. " +
                                "Show the user a Dialog to upgrade location settings ");
                        location_Success = false;
                        try {
                            // Show the Dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(MAIN_TAG, "PendingIntent unable to execute request.");
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(MAIN_TAG, "Location settings are inadequate, " +
                                "and cannot be fixed here. Dialog not created.");
                        location_Success = false;
                        break;
                }
            }
        });
        return location_Success;
    }


    public boolean checkSelfPermission() {
        int AccessCorasLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int AccessFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> permissionNeeded = new ArrayList<>();

        if (AccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (AccessCorasLocation != PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }


        if (!permissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionNeeded.toArray(new String[permissionNeeded.size()]), MY_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

    private void callUserMeApi() {
        try {
            JSONObject jsonObject = new JSONObject();

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.USER_ME.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());


                    userDetails = new Gson().fromJson(String.valueOf(response), GetUserData.DataBean.class);
                    GetUserData userDetail = new Gson().fromJson(String.valueOf(response), GetUserData.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
                        alUserList = new ArrayList<>();

                        if (userDetail.getError_code() == 0) {


                            alUserList.add(userDetail);

                            Applog.E("UserUpdate==>Dg==>" + userDetail);

                            String userName = userDetail.getData().getName();
                            tvDrawUsername.setText(userName);

                            if (Build.VERSION.SDK_INT < 23) {
                                setHomeValues();
                            } else {
                                if (checkSelfPermission()) {
                                    setHomeValues();
                                }
                            }

//                            SessionManager.saveUserData(context, userDetails);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//
                            //getIs_agreed = 0 new user

//
                        } else {
                            MyProgressDialog.hideProgressDialog();
                            SnackBar.showError(context, snackBarView, response.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressDialog.hideProgressDialog();
                    Applog.E("Error: " + error.getMessage());

                    SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put(WebFields.PARAM_ACCEPT, "application/json");
                    Applog.E("Token==>" + SessionManager.getToken(context));
                    params.put(WebFields.PARAM_AUTHOTIZATION, GlobalValues.BEARER_TOKEN + SessionManager.getToken(context));

                    return params;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    GlobalValues.TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}



