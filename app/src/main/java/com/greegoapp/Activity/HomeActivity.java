package com.greegoapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.greegoapp.Adapter.DrawerLayoutAdapter;
import com.greegoapp.AppController.AppController;
import com.greegoapp.FCM.Config;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.GetPickUpTime;
import com.greegoapp.Model.GetTripRate;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.Model.UserDriverTripDetails;
import com.greegoapp.Model.UserNearDriverList;
import com.greegoapp.Model.UserTripStatus;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.MYEditCard;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityHomeBinding;
import com.greegoapp.polilineAnimator.MapAnimator;
import com.greegoapp.polilineAnimator.MapHttpConnection;
import com.greegoapp.polilineAnimator.PathJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final String TAG = "HomeActivity.this";
    String MAIN_TAG = HomeActivity.class.getSimpleName();
    ActivityHomeBinding binding;
    Context context;
    private View snackBarView;

    private RelativeLayout navHeader, rlMain;
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

    Dialog dialog;
    ProgressDialog proDialog;

    //pp
    public static final int MY_PERMISSION_REQUEST = 1;
    LocationManager locationManager;
    public boolean isGPSEnable = false;
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static boolean location_Success = false;

    GetUserData.DataBean userDetails;
    ArrayList<GetUserData> alUserList = new ArrayList<>();
    String userName, profilePic;

    View view;

    private GoogleMap mGoogleMap;
    View mapView;
    MapFragment mFragment;
    private GoogleApiClient mGoogleApiClient;


    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

    private static final int CALL_PERMIT = 101;
    public final static int REQUEST_ADD_PAYMENT = 110;
    public final static int REQUEST_ADD_VEHICLE = 111;

    private final int FASTEST_INTERVAL = 900;
    private final int UPDATE_INTERVAL = 901;
    private final int REQ_PERMISSION = 999;

    public static final int HOME_SLIDER_PROFILE_UPDATE = 1000;
    public static final int EDIT_VEHICAL_REQUEST = 11011;
    private static final long MOVE_ANIMATION_DURATION = 100011;
    public static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1001;
    public static final int MAP_HOME_PROFILE_UPDATE = 1002;
    public static final int PICK_CONTACT_REQUEST = 1003;  // The request code

    public static final int ADD_EDIT_VEHICAL_REQUEST = 2000;  // The request code
    public static final int CHANGE_CONTACT_NO = 2001;

    private long TURN_ANIMATION_DURATION = 500;
    public static final int COMPLETE_UPDATE_USER_DETAILS = 0007;
    public static final String REQUEST_USER_TRIP = "1250";
    boolean GpsStatus;


    public static final int SLIDER_PAYMENT_METHOD = 20001;
    public static final int SLIDER_TRIP_HISTORY = 20002;
    public static final int SLIDER_FREE_TRIP = 20003;
    public static final int SLIDER_HELP = 20004;
    public static final int SLIDER_SETTING = 20005;

    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug


    private Location mylocation;
    Geocoder geocoder;
    List<LatLng> addresses;

    private Location lastLocation;
    LatLng latLng;
    TextView txtAddress;
    Button btnDone;
    String location, areaName, sourceLatitude, sourceLongitude;

    //Mujju
    RelativeLayout rlVwUpdateMain, rlUpdate, rlUpdateData;
    TextView txtVwProfile, txtVwVehicleName, txtVwpayment, txtVwUpdate, txtVwCount;
    RelativeLayout rlProfile, rlVehicleNo, rlPaymentNo;

    // pragnesh
    private List<LatLng> routeList;

    private Marker marker;
    Bitmap mMarkerIcon;
    private int mIndexCurrentPoint = 0;
    private LatLng pickupPoint, dropPoint;
    Double dist;
    int mapZoomLevel;
    ArrayList<GetUserData.DataBean.CardsBean> alUserSubDetails = new ArrayList<>();

    //Vehicle 17_4_2018
    LinearLayout llEditVehicle, llSetVehicle, llVehical;
    ImageView imgSetting, imgVwVehicle;
    TextView tvVehicalData, tvEditVehicle;
    String strManufacur, strModel, strColor;
    int strYear;
    boolean isEditVehical = false;


    LinearLayout llEditVechicleDesign, llCostDesign;
    TextView txtEstMentCost;
    MYEditCard edtCardDetail, imgVwYrCard;
    public String cardNo;
    public String statCode;
    float base_fee, time_fee, mile_fee, trip_price, over_mile_fee, tripCost;
    String duration, duration1;
    Button btnRequest;
    String duratioTime, durationTimeCalculate;
    String durationOnlyMin;

    //Mujju 19_4_18
    ArrayList<UserNearDriverList.DataBean> alUserNearDriverDtls;
    ArrayList<GetUserData.DataBean.VehiclesBean> alVehicleDetail = new ArrayList<>();

    LinearLayout llDriverPersDetailsMain;

    MyReceiver myReceiver;

    //Trip Details
    TextView tvAcceptDriverName, tvPromoCode, tvContact, tvCancel, tvShareYrTrip, tvChangeTrip, tvTripTotalCost, tvUserCardNo;
    ImageView ivDriverPro, imgVwShare;
    String userRequestId;
    int vehicalId, vehicalSelectId;

    ArrayList<UserDriverTripDetails.DataBean> userDrTripDetails = new ArrayList<>();

    //On trip Details
    RelativeLayout rlOnTripUser;
    TextView tvOnTripUser, tvOnTripTime, tvOnTripDrName, tvOnTripDrCode, tvAvanueAddress;
    String strDriverContact;
    String strTripDriverName, strTripDriverContactNo, strTripFromAddress, strTripToAddress, strTripId, strTripStatus, driverPicUrl,
            totalTripAmount, paymentTripId;

    //Priyanka
    String strVehicalData = null;
    String picLocAddress, destLocAddress, fromLat, fromLong, toLat, toLong;

    private String driver_id;
    String remainingDuration;

    private boolean mLocationPermissionGranted;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100001;


    private boolean isCheckNetworkThreadActive = false; // Flag to indicate if thread is active
    private boolean isNetworkAvaiableUIActive = false; // Flag to indicate which view is showing
    final private static int NETWORK_CHECK_INTERVAL = 5000; // Sleep interval between checking network

    TextView tvMarkerTime, tvLegal, txtNoNearByDriver;
    ArrayList<UserDriverTripDetails.DataBean> alUserDrTripDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerLayoutAdapter = new DrawerLayoutAdapter(HomeActivity.this, drawerTitle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        getLocationPermission();

        context = HomeActivity.this;
        fragmentStack = new Stack<Fragment>();
        fragmentManager = this.getSupportFragmentManager();
        snackBarView = findViewById(android.R.id.content);
        manager = getFragmentManager();

        bindView();
        setListners();

        if (ConnectivityDetector.isConnectingToInternet(context)) {
            if (Build.VERSION.SDK_INT < 23) {
                //Do not need to check the permission

                /*  Initialize Map  */
                initilizeMap();
                setUpGoogleApiClient();
                // create GoogleApiClient
//                createGoogleApi();

            } else {
                if (checkAndRequestPermissions()) {
                    //If you have already permitted the permission

                    //        initialize GoogleMaps
                    initilizeMap();
                    setUpGoogleApiClient();
                    // create GoogleApiClient
//                    createGoogleApi();

                }
            }

            if (!isGPSEnable()) {
                CheckGpsStatus();
            } else {
                //   if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
                // }
                initilizeMap();
            }

        } else {
            SnackBar.showInternetError(context, snackBarView);
        }


        if (ConnectivityDetector.isConnectingToInternet(context)) {
            callUserMeApi();
            // CheckGpsStatus();
        } else {
            SnackBar.showInternetError(context, snackBarView);
        }

        slideMenu();

        drawerlist.requestDisallowInterceptTouchEvent(true);
        navHeader.requestDisallowInterceptTouchEvent(true);

        remainingTripStatus("" + getIntent().getStringExtra("messageTrip"), getIntent().getStringExtra("trip_id")
                , getIntent().getStringExtra("tripAmount"));
//        remainingPaymentStatus("" + getIntent().getStringExtra("messagePayment"), getIntent().getStringExtra("trip_id"));


        try {

            String tripId = SessionManager.getTripId(context);
            String tripStatus = SessionManager.getTripStatus(context);

            Applog.E("tripId===>" + tripId + "tripStatus===>" + tripStatus);
//            remainingTripStatus(tripId,tripStatus);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void remainingPaymentStatus(String messagePaymen, String strTripId) {

        if (paymentStatus != null) {
            if (paymentStatus.matches("1")) {
                Intent in = new Intent(context, TripCompleteChargePayActivity.class);
                in.putExtra("totalCost", tripTotalAmount);
                in.putExtra("driverName", strTripDriverName);
                in.putExtra("driverPic", driverPicUrl);
                in.putExtra("fromAddress", strTripToAddress);
                in.putExtra("tripId", paymentTripId);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();
                SnackBar.showSuccess(context, snackBarView, "Payment successfully.");
            }
        }

    }

    public void remainingTripStatus(String tripStatus, String strTripId, String trip_amount) {

        if (tripStatus != null) {

            if (tripStatus.matches(GlobalValues.DRIVER_CANCELLED)) {
                SnackBar.showDriverRequestStatus(context, snackBarView, "Request cancel.");
            } else if (tripStatus.matches(GlobalValues.DRIVER_ASSIGNED)) {
//                callUserTripDetailsAPI(SessionManager.getTripId(context));
                SessionManager.getUserTripDtl(context);
                SessionManager.getDriverTripDtl(context);
                SessionManager.getUserDrTripLocDtl(context);
                SessionManager.getUserRequestTripDtl(context);

                txtAddress.setVisibility(View.GONE);

                setGetNotifDriverTripDetails();
            }

            //Driver Waiting
            else if (tripStatus.matches(GlobalValues.DRIVER_WAITING)) {

                SessionManager.getUserTripDtl(context);
                SessionManager.getDriverTripDtl(context);
                SessionManager.getUserDrTripLocDtl(context);
                SessionManager.getUserRequestTripDtl(context);

                setGetNotifDriverTripDetails();

                llVehical.setVisibility(View.GONE);
                txtAddress.setVisibility(View.GONE);
                llCostDesign.setVisibility(View.GONE);
                llDriverPersDetailsMain.setVisibility(View.VISIBLE);

                SnackBar.showDriverRequestStatus(context, snackBarView, "Driver waiting.");
            }
            //Driver On Going
            else if (tripStatus.matches(GlobalValues.DRIVER_ONGOING)) {
                setRemaningDriverOnGoingTripDetails();
                SnackBar.showDriverRequestStatus(context, snackBarView, "Driver on going.");
            }
            //Trip completed
            else if (tripStatus.matches(GlobalValues.DRIVER_TRIP_COMPLETED)) {
                showAddTrip(trip_amount, strTripId);
                SnackBar.showDriverRequestStatus(context, snackBarView, "Trip completed.");
            }
        }

    }

    private void setGetNotifDriverTripDetails() {
        try {
//        mGoogleMap.clear();
//            userRequestId = SessionManager.getUserTripDtl(context)  .getRequest_id();
//            driver_id = SessionManager.getUserTripDtl(context)  .getDriver_id();

            userPicUpLat = Double.parseDouble(SessionManager.getUserRequestTripDtl(context).getFrom_lat().trim());
            userPicUpLong = Double.parseDouble(SessionManager.getUserRequestTripDtl(context).getFrom_lng().trim());

            double drvLat = Double.parseDouble(SessionManager.getUserDrTripLocDtl(context).getLat().trim());
            double drvLong = Double.parseDouble(SessionManager.getUserDrTripLocDtl(context).getLng().trim());

            userDropOfLat = Double.parseDouble(SessionManager.getUserRequestTripDtl(context).getTo_lat().trim());
            userDropOfLng = Double.parseDouble(SessionManager.getUserRequestTripDtl(context).getTo_lng().trim());

            driverStatus = true;

            pickupPoint = new LatLng(userPicUpLat, userPicUpLong);
            dropPoint = new LatLng(drvLat, drvLong);

            statusPickuplocationRout = 0;
            createRoute(pickupPoint, dropPoint);

            zoomRoute(mGoogleMap, pickupPoint, dropPoint);

            llVehical.setVisibility(View.GONE);
            txtAddress.setVisibility(View.GONE);
            llCostDesign.setVisibility(View.GONE);
            llDriverPersDetailsMain.setVisibility(View.VISIBLE);

            try {
                cardNo = SessionManager.getCardNo(context);

                String strCardNo = cardNo.substring(12, 16);
                tvUserCardNo.setText(strCardNo);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }

            totalTripAmount = SessionManager.getUserRequestTripDtl(context).getTotal_estimated_trip_cost();
            driverPicUrl = SessionManager.getDriverTripDtl(context).getProfile_pic();

            if (driverPicUrl != null) {
                Glide.clear(ivDriverPro);
                Glide.with(context)
                        .load(driverPicUrl)
                        .centerCrop()
                        .signature(new StringSignature(UUID.randomUUID().toString()))
                        .crossFade().skipMemoryCache(true)
                        .into(ivDriverPro);

            } else {
                ivDriverPro.setImageResource(R.mipmap.ic_place_holder);
            }

            strTripDriverName = SessionManager.getDriverTripDtl(context).getName().trim();
            strTripDriverContactNo = SessionManager.getDriverTripDtl(context).getContact_number().trim();
            strTripFromAddress = SessionManager.getUserRequestTripDtl(context).getFrom_address().trim();
            strTripToAddress = SessionManager.getUserRequestTripDtl(context).getTo_address().trim();

            tvTripTotalCost.setText(SessionManager.getUserTripDtl(context).getTotal_trip_amount().trim());
            tvAvanueAddress.setText(SessionManager.getUserTripDtl(context).getRequest().getTo_address().trim());

            tvAcceptDriverName.setText(SessionManager.getUserTripDtl(context).getTotal_trip_amount().trim());
            tvPromoCode.setText(SessionManager.getDriverTripDtl(context).getPromocode().trim());
            strDriverContact = SessionManager.getDriverTripDtl(context).getContact_number().trim();

            SnackBar.showDriverRequestStatus(context, snackBarView, "Driver Assigned.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setRemaningDriverOnGoingTripDetails() {
        try {
            tvOnTripTime.setText(SessionManager.getUserTripDtl(context).getActual_trip_travel_time());
            tvOnTripDrName.setText(SessionManager.getDriverTripDtl(context).getName().toString());
            tvOnTripDrCode.setText(SessionManager.getDriverTripDtl(context).getPromocode().toString());

            userPicUpLat = Double.parseDouble(SessionManager.getUserRequestTripDtl(context).getFrom_lat());
            userPicUpLong = Double.parseDouble(SessionManager.getUserRequestTripDtl(context).getFrom_lng());

            userDropOfLat = Double.parseDouble(SessionManager.getUserDrTripLocDtl(context).getLat());
            userDropOfLng = Double.parseDouble(SessionManager.getUserDrTripLocDtl(context).getLng());

            pickupPoint = new LatLng(userPicUpLat, userPicUpLong);
            dropPoint = new LatLng(userDropOfLat, userDropOfLng);

            Applog.E("pickupPoint=>" + pickupPoint + " dropPoint=>" + dropPoint);
            statusPickuplocationRout = 0;
            createRoute(pickupPoint, dropPoint);

            llVehical.setVisibility(View.GONE);
            txtAddress.setVisibility(View.GONE);
            llCostDesign.setVisibility(View.GONE);
            llDriverPersDetailsMain.setVisibility(View.GONE);
            rlOnTripUser.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    boolean flagSelectVehical = false, flagSelectCard = false;

    private void setVehicleData() {
        if (alVehicleDetail.size() != 0) {
            boolean flag = false;

            for (GetUserData.DataBean.VehiclesBean vehicalData : alVehicleDetail) {
                if (vehicalData.getSelected() == 1) {
                    flag = true;
                    flagSelectVehical = flag;

                    vehicalId = vehicalData.getVehicle_id();
                    strManufacur = vehicalData.getVehicle_name();
                    strModel = vehicalData.getVehicle_model();
                    strColor = vehicalData.getColor();
                    strYear = vehicalData.getYear();
                    strVehicalData = strManufacur + " " + strModel + " " + strYear + " " + strColor;

                    imgSetting.setImageResource(R.mipmap.ellipse_6);
                    tvEditVehicle.setText(strVehicalData);
                } else {
                    imgSetting.setImageResource(android.R.color.transparent);
                    tvEditVehicle.setText("");
                }
            }
            Applog.E("vehical size check===>" + alVehicleDetail.size());

            if (flag == false) {
                if (alVehicleDetail.size() == 2) {
                    flagSelectVehical = flag;
                    callVehicleSelectApi(alVehicleDetail.get(0).getVehicle_id());
                }
            } else {
                MyProgressDialog.hideProgressDialog();
            }

        } else {
            imgSetting.setImageResource(android.R.color.transparent);
            tvEditVehicle.setText("");
        }

        tvVehicalData.setText("Edit your vehicle");
        imgVwVehicle.setImageResource(R.mipmap.setup);

    }

    private void setCardData() {
        if (alUserList != null) {

            boolean flagCard = false;

            for (GetUserData.DataBean.CardsBean cardData : alUserSubDetails) {

                if (cardData != null) {

                    if (cardData.getSelected() == 1) {
                        flagCard = true;

                        byte[] byteData = Base64.decode(cardData.getCard_number(), Base64.DEFAULT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            cardNo = new String(byteData, StandardCharsets.UTF_8);
                            String s = cardNo;
                            String s4 = s.substring(12, 16);
                            String strCardNumber = "**** **** **** " + s4;
                            if (strCardNumber != null) {
                                edtCardDetail.getCardNumber(cardNo);
                                edtCardDetail.isValid(cardNo);
                                edtCardDetail.setText(strCardNumber);

                                imgVwYrCard.getCardNumber(cardNo);
                                imgVwYrCard.isValid(cardNo);

                                edtCardDetail.getCardType();
                            } else {
                                SnackBar.showError(context, snackBarView, "Please select card first.");
                            }
                        }
                    }


                }
            }

            if (flagCard == false) {
                if (alUserSubDetails.size() == 2) {
                    flagSelectCard = flagCard;
                    callCardSelectApi(alUserSubDetails.get(0).getId());
                }
            } else {
                MyProgressDialog.hideProgressDialog();
            }
        }
    }

    private void callCardSelectApi(int id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.SELECT_DELETE_CARD.PARAM_CARD_ID, String.valueOf(id));
            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SELECT_DELETE_CARD.SELECT_CARD_MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    MyProgressDialog.hideProgressDialog();

                    if (flagSelectCard == false) {
                        callUserMeApi();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressDialog.hideProgressDialog();
                    Applog.E("Error: " + error.getMessage());


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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    private void callDriverRequestAccepted() {
        myReceiver = new MyReceiver();
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(REQUEST_USER_TRIP);
            registerReceiver(myReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String paymentStatus, tripTotalAmount;

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            UserTripStatus userTripStatus = new UserTripStatus();

            userDrTripDetails = arg1.getParcelableArrayListExtra("driverAllData");

            strTripId = arg1.getStringExtra("trip_id");
            strTripStatus = arg1.getStringExtra("tripStatus");
            tripTotalAmount = arg1.getStringExtra("trip_amount");

            SessionManager.saveTripId(context, strTripId);

            paymentTripId = arg1.getStringExtra("payment_trip_id");
            paymentStatus = arg1.getStringExtra("payment_status");


            if (strTripStatus != null) {

                if (strTripId != null) {
                    userTripStatus.setTrip_id(strTripId);
                    userTripStatus.setTrip_status(strTripStatus);

                    SessionManager.saveTripStaus(context, userTripStatus);
                }

                if (strTripStatus.matches(GlobalValues.DRIVER_CANCELLED)) {
                    SnackBar.showDriverRequestStatus(context, snackBarView, "Request cancel.");
                } else if (strTripStatus.matches(GlobalValues.DRIVER_ASSIGNED)) {
                    setDriverTripDetails(userDrTripDetails);
                }

                //Driver Waiting
                else if (strTripStatus.matches(GlobalValues.DRIVER_WAITING)) {
                    llVehical.setVisibility(View.GONE);
                    txtAddress.setVisibility(View.GONE);
                    llCostDesign.setVisibility(View.GONE);
                    llDriverPersDetailsMain.setVisibility(View.VISIBLE);

                    SnackBar.showDriverRequestStatus(context, snackBarView, "Driver waiting.");
                }
                //Driver On Going
                else if (strTripStatus.matches(GlobalValues.DRIVER_ONGOING)) {
                    setDriverOnGoingTripDetails();
                    SnackBar.showDriverRequestStatus(context, snackBarView, "Driver on going.");
                }
                //Trip completed
                else if (strTripStatus.matches(GlobalValues.DRIVER_TRIP_COMPLETED)) {
                    showAddTrip(tripTotalAmount, strTripId);
                    SnackBar.showDriverRequestStatus(context, snackBarView, "Trip completed.");
                }
            } else if (paymentStatus != null) {

                if (paymentStatus.matches("1")) {

                    Intent in = new Intent(context, TripCompleteChargePayActivity.class);
                    in.putExtra("totalCost", tripTotalAmount);
                    in.putExtra("driverName", strTripDriverName);
                    in.putExtra("driverPic", driverPicUrl);
                    in.putExtra("fromAddress", strTripToAddress);
                    in.putExtra("tripId", paymentTripId);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                    finish();
                    SnackBar.showSuccess(context, snackBarView, "Payment successfully.");
                }
            }
        }

    }

    private void setDriverOnGoingTripDetails() {

//        for (UserDriverTripDetails.DataBean tripDetails : userDrTripDetails) {
            mGoogleMap.clear();

            pickupPoint = new LatLng(userPicUpLat, userPicUpLong);
            dropPoint = new LatLng(userDropOfLat, userDropOfLng);

            Applog.E("pickupPoint=>" + pickupPoint + " dropPoint=>" + dropPoint);
            statusPickuplocationRout = 0;
            createRoute(pickupPoint, dropPoint);

            llVehical.setVisibility(View.GONE);
            txtAddress.setVisibility(View.GONE);
            llCostDesign.setVisibility(View.GONE);
            llDriverPersDetailsMain.setVisibility(View.GONE);
            rlOnTripUser.setVisibility(View.VISIBLE);
//        }
    }

    double userDropOfLat, userDropOfLng, userPicUpLat, userPicUpLong;

    private void setDriverTripDetails(ArrayList<UserDriverTripDetails.DataBean> userDrTripDetails) {

        for (UserDriverTripDetails.DataBean tripDetails : userDrTripDetails) {

            mGoogleMap.clear();

            userRequestId = tripDetails.getRequest_id();

            driver_id = tripDetails.getDriver_id();

            userPicUpLat = Double.parseDouble(tripDetails.getRequest().getFrom_lat());
            userPicUpLong = Double.parseDouble(tripDetails.getRequest().getFrom_lng());

            double drvLat = Double.parseDouble(tripDetails.getDriver_location().getLat());
            double drvLong = Double.parseDouble(tripDetails.getDriver_location().getLng());

            userDropOfLat = Double.parseDouble(tripDetails.getRequest().getTo_lat());
            userDropOfLng = Double.parseDouble(tripDetails.getRequest().getTo_lng());

            SessionManager.saveUserTripDetails(context, tripDetails);


            if (tripDetails.getStatus().matches(GlobalValues.DRIVER_ASSIGNED)) {

                driverStatus = true;

                pickupPoint = new LatLng(userPicUpLat, userPicUpLong);
                dropPoint = new LatLng(drvLat, drvLong);

//                createMarker(drvLat, drvLong, "Driver", "Show");
                statusPickuplocationRout = 0;
                createRoute(pickupPoint, dropPoint);

                zoomRoute(mGoogleMap, pickupPoint, dropPoint);

                llVehical.setVisibility(View.GONE);
                txtAddress.setVisibility(View.GONE);
                llCostDesign.setVisibility(View.GONE);
                llDriverPersDetailsMain.setVisibility(View.VISIBLE);

                try {
                    String strCardNo = cardNo.substring(12, 16);
                    tvUserCardNo.setText(strCardNo);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }

                totalTripAmount = txtEstMentCost.getText().toString();
                driverPicUrl = tripDetails.getDriver().getProfile_pic();

                if (driverPicUrl != null) {
                    Glide.clear(ivDriverPro);
                    Glide.with(context)
                            .load(driverPicUrl)
                            .centerCrop()
                            .signature(new StringSignature(UUID.randomUUID().toString()))
                            .crossFade().skipMemoryCache(true)
                            .into(ivDriverPro);

                } else {
                    ivDriverPro.setImageResource(R.mipmap.ic_place_holder);
                }

                strTripDriverName = tripDetails.getDriver().getName();
                strTripDriverContactNo = tripDetails.getDriver().getContact_number();
                strTripFromAddress = tripDetails.getRequest().getFrom_address();
                strTripToAddress = tripDetails.getRequest().getTo_address();

//                tvTripTotalCost.setText("$" + totalTripAmount);
                tvTripTotalCost.setText(totalTripAmount);
                tvAvanueAddress.setText(strTripToAddress);

                tvAcceptDriverName.setText(tripDetails.getDriver().getName());
                tvPromoCode.setText(tripDetails.getDriver().getPromocode());
                strDriverContact = tripDetails.getDriver().getContact_number();

                SnackBar.showDriverRequestStatus(context, snackBarView, "Driver Assigned.");

                tvOnTripTime.setText(duratioTime);
                tvOnTripDrName.setText(tripDetails.getDriver().getName());
                tvOnTripDrCode.setText(tripDetails.getDriver().getPromocode());
            }
        }
    }

    private void zoomRoute(GoogleMap googleMap, LatLng pickupPoint, LatLng dropPoint) {
        if (googleMap == null || pickupPoint == null || dropPoint == null) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//        for (LatLng latLngPoint : pickupPoint)
        boundsBuilder.include(pickupPoint);

        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    private void callNearDriverAPI(String currentLat, String currentLong) {
        try {

            Applog.E("Get Current Lot" + currentLat);
            Applog.E("Get Current Long" + currentLong);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(WebFields.USER_NEAR_DRIVER_LIST.PARAM_USER_LAT, currentLat);
            jsonObject.put(WebFields.USER_NEAR_DRIVER_LIST.PARAM_USER_LONG, currentLong);

            Applog.E("request: " + jsonObject.toString());
//            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.USER_NEAR_DRIVER_LIST.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());


                    UserNearDriverList nearDriver = new Gson().fromJson(String.valueOf(response), UserNearDriverList.class);
                    alUserNearDriverDtls = new ArrayList<>();

                    try {
//                        MyProgressDialog.hideProgressDialog();
                        if (nearDriver.getError_code() == 0) {


                            if (nearDriver.getData().size() != 0) {

                                if (txtNoNearByDriver.getVisibility() == View.VISIBLE) {
                                    txtNoNearByDriver.setVisibility(View.GONE);
                                    txtAddress.setVisibility(View.VISIBLE);
                                }


                                alUserNearDriverDtls.addAll(nearDriver.getData());

                                Applog.E("near array size after==>" + alUserNearDriverDtls.size());

                                if (alUserNearDriverDtls.size() != 0) {

                                    setNearDriver();
                                }
                            } else {
                                mGoogleMap.clear();
                                if (txtNoNearByDriver.getVisibility() == View.GONE) {
                                    txtNoNearByDriver.setVisibility(View.VISIBLE);
                                    txtAddress.setVisibility(View.GONE);
                                }
//                                SnackBar.showDriverRequestStatus(context, snackBarView, "No near by driver found.");
                            }

                        } else {
                            if (txtNoNearByDriver.getVisibility() == View.GONE) {
                                txtNoNearByDriver.setVisibility(View.VISIBLE);
                                txtAddress.setVisibility(View.GONE);
                            }
//                            MyProgressDialog.hideProgressDialog();
//                            SnackBar.showError(context, snackBarView, response.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
//                    MyProgressDialog.hideProgressDialog();
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

    boolean driverStatus = false;

    private void setNearDriver() {

        if (alUserNearDriverDtls.size() != 0) {


//            for (UserNearDriverList.DataBean nrDriver : alUserNearDriverDtls)

            for (int i = 0; i < alUserNearDriverDtls.size(); i++) {

                if (driverStatus == false) {

                    Applog.E("Double lat == > " + alUserNearDriverDtls.get(i).getLat());
                    double drvLat = Double.parseDouble(alUserNearDriverDtls.get(i).getLat());
                    double drvLong = Double.parseDouble(alUserNearDriverDtls.get(i).getLng());

                    createMarker(drvLat, drvLong, "Driver", "Show");

                } else {

                    if (driver_id.equals(alUserNearDriverDtls.get(i).getDriver_id())) {

                        double drvLat = Double.parseDouble(alUserNearDriverDtls.get(i).getLat());
                        double drvLong = Double.parseDouble(alUserNearDriverDtls.get(i).getLng());
//                        mGoogleMap.clear();
//                        createMarker(drvLat, drvLong, "Driver", "Show");
                        if (strTripStatus != null) {
                            if (strTripStatus.matches(GlobalValues.DRIVER_ONGOING)) {
                                dropPoint = new LatLng(Double.parseDouble(toLat), Double.parseDouble(toLong));
                            }
                        }
                        pickupPoint = new LatLng(drvLat, drvLong);

                        myCalcTime(pickupPoint.latitude, pickupPoint.longitude, dropPoint.latitude, dropPoint.longitude);
                        statusPickuplocationRout = 0;
                        createRoute(pickupPoint, dropPoint);

                    }
                }

            }

        }
    }

    protected void createMarker(double latitude, double longitude, String title, String snippet) {

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_driver)));
    }

    // Method to share either text or URL.
    private void shareUserTripDetails() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.

        String message = "Driver name:" + strTripDriverName + " Driver Contact no:" + strTripDriverContactNo
                + " From Address:" + strTripFromAddress + " To Address:" + strTripToAddress;
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "Share On Trip details!"));
    }

    private void showCallbacksCancelTrip(String msgCancelTrip) {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context)
                    .setTitle("Greego").setMessage(msgCancelTrip);


            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callCancleTripRequestAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
            });

            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCancleTripRequestAPI() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(WebFields.CANCLE_TRIP_REQUEST.PARAM_REQUEST_ID, userRequestId);

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.CANCLE_TRIP_REQUEST.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    MyProgressDialog.hideProgressDialog();

                    Intent mIntent = new Intent(context, HomeActivity.class);
                    startActivity(mIntent);
                    finish();

                    SnackBar.showSuccess(context, snackBarView, "Your Trip cancel successfully.");
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

    private void callUserRrequestAPI() {
        try {

            if (alVehicleDetail.size() != 0) {

                for (GetUserData.DataBean.VehiclesBean vehicalData : alVehicleDetail) {
                    if (vehicalData.getSelected() == 1) {
                        vehicalSelectId = vehicalData.getId();
                    }
                }
            }

            JSONObject jsonObject = new JSONObject();

            Float fromLattitude = Float.parseFloat(fromLat);
            Float fromLongitude = Float.parseFloat(fromLong);
            Float toLattitude = Float.parseFloat(toLat);
            Float toLongitude = Float.parseFloat(toLong);

            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_USER_VEHICLE_ID, vehicalSelectId);
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_FROM_ADDRESS, picLocAddress);
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_FROM_LAT, fromLattitude);
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_FROM_LNG, fromLongitude);
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_TO_ADDRESS, destLocAddress);
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_TO_LAT, toLattitude);
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_TO_LNG, toLongitude);
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_TOTAL_EST_TRAVEL_TIME, duratioTime);

//            String estCost = txtEstMentCost.getText().toString();
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_TOTAL_EST_TRIP_COST, tripCost);

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.USER_ADD_REQUEST.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    driverDetailsNotGet();
//                    UserRequestDriver userDetail = new Gson().fromJson(String.valueOf(response), UserRequestDriver.class);
//                    MyProgressDialog.hideProgressDialog();

//                        if (userDetail.getError_code() == 0) {


//                    SnackBar.showSuccess(context, snackBarView, "Request successfully sent!!!");


//                        } else {
//                            MyProgressDialog.hideProgressDialog();
//                            SnackBar.showError(context, snackBarView, response.getString("message"));
//                        }
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

    private void driverDetailsNotGet() {
        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {

//                    GetKeyHashValue();
//                    doProgress();

                    MyProgressDialog.hideProgressDialog();


                }
            }, 60000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showCheckUserUpdateData(String msg) {
        try {

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_unable_request);
            Button btnHome = (Button) dialog.findViewById(R.id.btnBackHome);
            btnHome.setOnClickListener(this);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            Intent mIntent = new Intent(context, PickUpLocationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("pickupPointLatitud", String.valueOf(pickupPoint.latitude));
            bundle.putString("pickupPointLongitude", String.valueOf(pickupPoint.longitude));
            bundle.putString("dropPointLatitude", String.valueOf(dropPoint.latitude));
            bundle.putString("dropPointLongitude", String.valueOf(dropPoint.longitude));
            mIntent.putExtras(bundle);
            startActivityForResult(mIntent, PICK_CONTACT_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private synchronized void setUpGoogleApiClient() {
        Log.d(TAG, "createGoogleApi()");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        callDriverRequestAccepted();

        setUpGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        try {
            if (myReceiver != null)
                unregisterReceiver(myReceiver);
        } catch (Exception e) {
        }

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        callDriverRequestAccepted();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();

        }
        initilizeMap();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));


        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        try {

            String tripId = SessionManager.getTripId(context);
            String tripStatus = SessionManager.getTripStatus(context);

            Applog.E("tripId===>" + tripId + "tripStatus===>" + tripStatus);

//            remainingTripStatus(tripId,tripStatus);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

    }

    @Override
    public void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }


    // Initialize GoogleMaps
    public void initilizeMap() {
        try {
            mFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mgooleMap);
            mapView = mFragment.getView();
            mFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        mGoogleMap = googleMap;


//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        MapStyleOptions mapStyle = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        mGoogleMap.setMapStyle(mapStyle);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                setUpGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            setUpGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);

        }
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.setTrafficEnabled(false);

        // Showing / hiding your current location

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("1"));
//            View locationButton = ((View) mapView.findViewById(Integer.parseInt("2")).getParent()).findViewById(Integer.parseInt("0"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

            rlp.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
            rlp.addRule(RelativeLayout.ALIGN_END, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rlp.setMargins(0, 0, 30, 40);
        } else {
            if (checkAndRequestPermissions()) {
                mGoogleMap.setMyLocationEnabled(true);
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("1"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

                rlp.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
                rlp.addRule(RelativeLayout.ALIGN_END, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                rlp.setMargins(0, 0, 30, 40);
            }
        }

        mGoogleMap.setTrafficEnabled(false);

        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);

        mGoogleMap.setOnMarkerClickListener(this);

        mGoogleMap.setTrafficEnabled(false);


        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                latLng = cameraPosition.target;
                try {
                    getAddress(latLng.latitude, latLng.longitude);
                    Log.e("Changing address", getAddress(latLng.latitude, latLng.longitude));
                    Log.e("Latitude", latLng.latitude + "");
                    Log.e("Longitude", latLng.longitude + "");
                    sourceLatitude = latLng.latitude + "";
                    sourceLongitude = latLng.longitude + "";
//                    location = getAddress(latLng.latitude, latLng.longitude);
//                    txtAddress.setText(location);

                    //After get current location get near by driver


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });


    }

    private boolean requestCallPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (result != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMIT);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMIT);
            }
            return false;
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + strDriverContact));
            context.startActivity(intent);
            // Toast.makeText(getApplicationContext(), "Permission  Granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private boolean checkAndRequestPermissions() {
        int AccessFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int AccessCorasLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (AccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (AccessCorasLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }
        return true;
    }

    public String getstatCode(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();

        System.out.println("get address");
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (addresses.size() > 0) {
                System.out.println("size====" + addresses.size());
                Address address = addresses.get(0);

                statCode = address.getAdminArea();

                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                    if (i == addresses.get(0).getMaxAddressLineIndex()) {
                        result.append(addresses.get(0).getAddressLine(i));
                    } else {
                        result.append(addresses.get(0).getAddressLine(i) + ",");
                    }
                }

                System.out.println("ad==" + address);
                System.out.println("result---" + result.toString());

//                autoComplete_location.setText(result.toString());
// Here is you AutoCompleteTextView where you want to set your string address (You can remove it if you not need it)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private float CalculateMile(String strDistance, String strTime) {
        float dis = Float.parseFloat(strDistance);
        float time = Float.parseFloat(strTime);
        if (dis <= 10) {
            trip_price = base_fee + (time * time_fee) + (dis * mile_fee);
        } else {
            trip_price = base_fee + (time * time_fee) + (10 * mile_fee) + ((dis - 10) * over_mile_fee);
        }
        return trip_price;
    }

    //Calculat googgle map timining
    private void myCalcTime(double lat1, double lng1, double lat2, double lng2) {

        String url = "" + WebFields.DIST_URL.MODE + WebFields.DIST_URL.ORIGINS + lat1 + "," + lng1 + WebFields.DIST_URL.DESTINATION + lat2 + "," + lng2 +
                "&mode=driving&key=AIzaSyDuLTaJL-tMzdBoTZtCQfCz4m66iEZ1eQc";

        try {
            JSONObject jsonObject = new JSONObject();
            Applog.E("request: " + jsonObject.toString());
//            MyProgressDialog.showProgressDialog(context);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
//                    MyProgressDialog.hideProgressDialog();

                    GetPickUpTime pickUpTime = new Gson().fromJson(String.valueOf(response), GetPickUpTime.class);
                    remainingDuration = pickUpTime.getRoutes().get(0).getLegs().get(0).getDuration().getText();

                    //    tvMarkerTime.setText(remainingDuration);
                    //tvOnTripTime.setText(remainingDuration);

                    Applog.E("success: " + response.toString());
                    Applog.E("totalDuration: " + remainingDuration);

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
//                    MyProgressDialog.hideProgressDialog();
                    error.getStackTrace();
                    Applog.E("Error: " + error.getMessage());
                    SnackBar.showError(context, snackBarView, error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(WebFields.PARAM_ACCEPT, "application/json");
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

    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCOUNTS
        );
    }

    // Start location Updates
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if (checkAndRequestPermissions()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        if (isGPSEnable()) {

            Log.d(TAG, "onLocationChanged [" + location + "]");
            lastLocation = location;

//            if (strTripStatus != null) {
//                if (strTripStatus.matches(GlobalValues.DRIVER_ASSIGNED)) {
//                    callNearDriverAPI(lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
//
//
//                } else if (strTripStatus.matches(GlobalValues.DRIVER_ONGOING)) {
//////                mGoogleMap.clear();
//
//                    callNearDriverAPI(lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
//////                pickupPoint = new LatLng(location.getLatitude(), location.getLongitude());
//////                dropPoint =    new LatLng(userDropOfLat, userDropOfLng);
//////
//////                Applog.E("pickupPoint=>" + pickupPoint + " dropPoint=>" + dropPoint);
//////                createMarker(drvLat, drvLong, "Driver", "Show");
//////                createRoute(pickupPoint, dropPoint);
//                }
//            } else if (llCostDesign.getVisibility() == View.VISIBLE) {
//                mGoogleMap.clear();
//                createRoute(pickupPoint, dropPoint);
//                callNearDriverAPI(lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
//            } else {
//                mGoogleMap.clear();
//                callNearDriverAPI(lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
//
//            }
            if (strTripStatus != null) {
//                if (strTripStatus.matches(GlobalValues.DRIVER_ASSIGNED)) {
                callNearDriverAPI(lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");

//                } else if (strTripStatus.matches(GlobalValues.DRIVER_ONGOING)) {
//                mGoogleMap.clear();
//
//                callNearDriverAPI(lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
//                pickupPoint = new LatLng(location.getLatitude(), location.getLongitude());
//                dropPoint = new LatLng(userDropOfLat, userDropOfLng);
//
//                Applog.E("pickupPoint=>" + pickupPoint + " dropPoint=>" + dropPoint);
//                createMarker(drvLat, drvLong, "Driver", "Show");
//                createRoute(pickupPoint, dropPoint);
//                }
            } else if (llCostDesign.getVisibility() == View.VISIBLE) {
//                mGoogleMap.clear();
//                createRoute(pickupPoint, dropPoint);
//                callNearDriverAPI(lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
            } else {
                mGoogleMap.clear();

                callNearDriverAPI(lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
            }


//        writeActualLocation(location);

//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(pickupPoint);
//        builder.include(dropPoint);
//
//        //Place current location marker
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
            LatLng latLng;
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
//        marker = mGoogleMap.addMarker(markerOptions);

            //move map camera
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

//        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));

            //stop location updates
//            if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
//            }
        }

        if (!isGPSEnable()) {
            CheckGpsStatus();
        } else {
            //   if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            // }
            initilizeMap();
        }

    }


    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    private void writeActualLocation(Location location) {
        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(new LatLng(location.getLatitude(), location.getLongitude())).bearing(360).
                zoom(15f).tilt(40).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }


    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {

            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());

                writeLastLocation();
                startLocationUpdates();

                double lat = lastLocation.getLatitude();
                double lng = lastLocation.getLongitude();

                if (ConnectivityDetector.isConnectingToInternet(context)) {
                    String currentLat = String.valueOf(lat);
                    String currentLong = String.valueOf(lng);

                    statCode = getstatCode(Double.parseDouble(currentLat), Double.parseDouble(currentLong));

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).run();


                    callNearDriverAPI(currentLat, currentLong);
                    callTripRateAPI(statCode);

                } else {
                    SnackBar.showInternetError(context, snackBarView);
                }

            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();

    }

    private void callTripRateAPI(String statCode) {
        try {
            JSONObject jsonObject = new JSONObject();

            if (!statCode.isEmpty()) {
                jsonObject.put(WebFields.TRIP.PARAM_STATE, statCode);
            } else {
                jsonObject.put(WebFields.TRIP.PARAM_STATE, "NY");
            }

            Applog.E("request Trip Price => " + jsonObject.toString());
//            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.TRIP.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    GetTripRate tripRateData = new Gson().fromJson(String.valueOf(response), GetTripRate.class);
                    try {
//                        MyProgressDialog.hideProgressDialog();
                        if (tripRateData.getError_code() == 0) {

                            base_fee = Float.parseFloat(tripRateData.getData().getBase_fee());
                            time_fee = Float.parseFloat(tripRateData.getData().getTime_fee());
                            mile_fee = Float.parseFloat(tripRateData.getData().getMile_fee());
                            over_mile_fee = Float.parseFloat(tripRateData.getData().getOvermile_fee());
                            //   CalculateMile(str,elapsedTime);


                        } else {
//                            MyProgressDialog.hideProgressDialog();
                            response.getString("message");
//                            SnackBar.showError(context, snackBarView, response.getString("message"));
                            SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
//                    MyProgressDialog.hideProgressDialog();
                    Applog.E("Error: " + error.getMessage());

                    SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(WebFields.PARAM_ACCEPT, "application/json");
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

    public String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();

        System.out.println("get address");
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (addresses.size() > 0) {
                System.out.println("size====" + addresses.size());
                Address address = addresses.get(0);

                areaName = address.getSubLocality();

                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                    if (i == addresses.get(0).getMaxAddressLineIndex()) {
                        result.append(addresses.get(0).getAddressLine(i));
                    } else {
                        result.append(addresses.get(0).getAddressLine(i) + ",");
                    }
                }

                System.out.println("ad==" + address);
                System.out.println("result---" + result.toString());

//                autoComplete_location.setText(result.toString()); // Here is you AutoCompleteTextView where you want to set your string address (You can remove it if you not need it)
            } else {
//                Toast.makeText(context, "Please wait rout draw", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }


    private void startAnim() {
        if (mGoogleMap != null) {
            MapAnimator.getInstance().animateRoute(mGoogleMap, routeList);
        } else {
            Toast.makeText(this, "Map not ready", Toast.LENGTH_LONG).show();
        }
    }

    public void resetAnimation(View view) {
        startAnim();
    }

    private String getMapsApiDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;


    }


    private void createRoute(LatLng pickupPoint, LatLng dropPoint) {
        String url = getMapsApiDirectionsUrl(this.pickupPoint, this.dropPoint);
        ReadTask downloadTask = new ReadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        Applog.E("===========> " + url);
    }

    private class ReadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String data = "";
            try {
                MapHttpConnection http = new MapHttpConnection();
                data = http.readUrl(url[0]);

            } catch (Exception e) {
                // TODO: handle exception
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                new ParserTask().execute(result);
            } else {
                Log.e("TAG", "Didn't get response");
            }

        }
    }


    int hours;

    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            // TODO Auto-generated method stub
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
                duration = parser.getDuration(jObject);
                String[] separated = null;
                if (!duration.isEmpty()) {
                    separated = duration.split(" ");
                }
                duration1 = separated[0]; // this will contain "Fruit"

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        String timeFormat;

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
//            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            try {
                if (routes.size() > 0) {

                    proDialog.hide();

                    for (int i = 0; i < routes.size(); i++) {
                        routeList = new ArrayList<LatLng>();
                        polyLineOptions = new PolylineOptions();
                        List<HashMap<String, String>> path = routes.get(i);

                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            routeList.add(position);
                        }


                        polyLineOptions.addAll(routeList);
                        polyLineOptions.width(8);
                        polyLineOptions.color(Color.BLACK);
                    }

                    mGoogleMap.clear();
                    mGoogleMap.setPadding(10, 200, 10, 100);
                    mGoogleMap.addPolyline(polyLineOptions);

//                    Toast.makeText(context, "Please wait rout draw few min", Toast.LENGTH_LONG).show();

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(pickupPoint);
                    builder.include(dropPoint);

                    LatLngBounds bounds = builder.build();

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int padding = (int) (width * 0.2);

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mGoogleMap.animateCamera(cameraUpdate);

                    dist = getDistance(pickupPoint.latitude, pickupPoint.longitude, dropPoint.latitude, dropPoint.longitude);
                    tripCost = CalculateMile(String.valueOf(dist), durationOnlyMin);

                    if (dist > 2 && dist <= 5) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                        mapZoomLevel = 12;
                    } else if (dist > 5 && dist <= 10) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
                        mapZoomLevel = 11;
                    } else if (dist > 10 && dist <= 20) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
                        mapZoomLevel = 11;
                    } else if (dist > 20 && dist <= 40) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
                        mapZoomLevel = 10;
                    } else if (dist > 40 && dist < 100) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(9.0f));
                        mapZoomLevel = 9;
                    } else if (dist > 100 && dist < 200) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
                        mapZoomLevel = 8;
                    } else if (dist > 200 && dist < 400) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(7.0f));
                        mapZoomLevel = 7;
                    } else if (dist > 400 && dist < 700) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f));
                        mapZoomLevel = 7;
                    } else if (dist > 700 && dist < 1000) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f));
                        mapZoomLevel = 6;
                    } else if (dist > 1000) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(4.0f));
                        mapZoomLevel = 5;
                    } else {
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
                        mapZoomLevel = 14;
                    }

                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    txtEstMentCost.setText("US $" + df.format(tripCost));

                    View customMarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                            inflate(R.layout.custom_marker_layout, null);
                    tvMarkerTime = (TextView) customMarker.findViewById(R.id.tvMarkerTime);
                    ImageView ivMarker = (ImageView) customMarker.findViewById(R.id.ivMarker);
                    tvMarkerTime.setText(duratioTime);

                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(pickupPoint)
                            .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, customMarker)))
                            .title("Current Location")
                            .snippet("Home"));

                    try {
                        String durationTime1 = duratioTime;
                        durationTime1 = durationTime1.replace("hour", ":");
                        durationTime1 = durationTime1.replace("min", "");
                        durationTime1 = durationTime1.replace("s", "");

                        if (durationTime1.contains(":")) {
                            hours = Integer.parseInt(durationTime1.substring(0, durationTime1.indexOf(":")).trim());
                        }
                        int minutes = Integer.parseInt(durationTime1.substring(durationTime1.indexOf(":") + 1).trim());
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.MINUTE, minutes);
                        calendar.add(Calendar.HOUR, hours);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm");
                        durationTimeCalculate = simpleDateFormat.format(calendar.getTime());

                        final Date dateObj = simpleDateFormat.parse(durationTimeCalculate);
                        timeFormat = new SimpleDateFormat("K:mm aa").format(dateObj);

                        tvMarkerTime.setText(timeFormat);
                        tvOnTripTime.setText(remainingDuration);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(dropPoint)
                            .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, customMarker)))
                            .title("Drop Location")
                            .snippet("Driver"));
                } else {
                    proDialog.hide();
                    if (statusPickuplocationRout == 1) {
                        SnackBar.showError(context, snackBarView, "No route found");
                    } else {

                    }
                    Log.e("TAG", "No route found");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    /**
     * calculates the distance between two locations in MILES
     */
    public double getDistance(double lat1, double lng1, double lat2, double lng2) {

//        double earthRadius = 3958.75; // in miles,
        double earthRadius = 6371; //for kilometer output
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }

    private void animateCarTurn(final Marker marker, final float startAngle,
                                final float endAngle, final long duration) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final Interpolator interpolator = new LinearInterpolator();

        final float dAndgle = endAngle - startAngle;

        Matrix matrix = new Matrix();
        matrix.postRotate(startAngle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(rotatedBitmap));

        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                Matrix m = new Matrix();
                m.postRotate(startAngle + dAndgle * t);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), m, true)));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(pickupPoint);
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    nextMoveAnimation();
                }
            }
        });
    }

    private void nextMoveAnimation() {
        if (mIndexCurrentPoint < routeList.size() - 1) {

            animateCarMove(marker, routeList.get(mIndexCurrentPoint), routeList.get(mIndexCurrentPoint + 1), MOVE_ANIMATION_DURATION);
        }
    }

    private void animateCarMove(final Marker marker, final LatLng beginLatLng,
                                final LatLng endLatLng, final long duration) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();

        final Interpolator interpolator = new LinearInterpolator();

//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(beginLatLng);
//        builder.include(endLatLng);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 48));


        // set car bearing for current part of path
        float angleDeg = (float) (180 * getAngle(beginLatLng, endLatLng) / Math.PI);
        Matrix matrix = new Matrix();
        matrix.postRotate(angleDeg);
        marker.setAnchor(0.5f, 0.5f);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));

        handler.post(new Runnable() {
            @Override
            public void run() {
                // calculate phase of animation
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                // calculate new position for marker
                double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * t + beginLatLng.longitude;
                LatLng newLoc = new LatLng(lat, lng);

                final CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(newLoc)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .build();
                marker.setPosition(newLoc);

                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // if not end of line segment of path
                if (t < 1.0) {
                    // call next marker position
                    handler.postDelayed(this, 16);
                } else {
                    // call turn animation
                    nextTurnAnimation();
                }
            }
        });
    }

    private void nextTurnAnimation() {
        mIndexCurrentPoint++;


        if (mIndexCurrentPoint < routeList.size() - 1) {
            LatLng prevLatLng = routeList.get(mIndexCurrentPoint - 1);
            LatLng currLatLng = routeList.get(mIndexCurrentPoint);
            LatLng nextLatLng = routeList.get(mIndexCurrentPoint + 1);

            float beginAngle = (float) (180 * getAngle(prevLatLng, currLatLng) / Math.PI);
            float endAngle = (float) (180 * getAngle(currLatLng, nextLatLng) / Math.PI);

            animateCarTurn(marker, beginAngle, endAngle, TURN_ANIMATION_DURATION);
        }
    }

    private double getAngle(LatLng beginLatLng, LatLng endLatLng) {
        double f1 = Math.PI * beginLatLng.latitude / 180;
        double f2 = Math.PI * endLatLng.latitude / 180;
        double dl = Math.PI * (endLatLng.longitude - beginLatLng.longitude) / 180;
        return Math.atan2(Math.sin(dl) * Math.cos(f2), Math.cos(f1) * Math.sin(f2) - Math.sin(f1) * Math.cos(f2) * Math.cos(dl));
    }


//    private static void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
//                                      final boolean hideMarker) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = myMap.getProjection();
//        final long duration = 30000;
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable() {
//            int i = 0;
//
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//                if (i < directionPoint.size())
//                    marker.setPosition(directionPoint.get(i));
//                i++;
//
//
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                } else {
//                    if (hideMarker) {
//                        marker.setVisible(false);
//                    } else {
//                        marker.setVisible(true);
//                    }
//                }
//            }
//        });
//    }

    //Mujju

    //........................
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    private void setHomeValues() {
        try {

            rlUpdateData.setVisibility(View.GONE);

            if (alUserList != null) {

                for (GetUserData userData : alUserList) {

                    alUserSubDetails.addAll(userData.getData().getCards());

                    if (userData.getData().getProfile_pic().equals("")) {
//                    if (userData.getData().getProfile_pic().matches("")) {
                        rlVwUpdateMain.setVisibility(View.VISIBLE);
                        rlUpdateData.setVisibility(View.GONE);
                        txtVwProfile.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        txtVwProfile.setTextColor(getResources().getColor(R.color.app_bg));
                    }


                    if (userData.getData().getCards().size() == 0) {
                        rlVwUpdateMain.setVisibility(View.VISIBLE);
                        rlUpdateData.setVisibility(View.GONE);
                        txtVwpayment.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        alUserSubDetails.addAll(userData.getData().getCards());
                        txtVwpayment.setTextColor(getResources().getColor(R.color.app_bg));
                        setCardData();
                    }

                    if (userData.getData().getVehicles().size() == 0) {
                        tvEditVehicle.setText(" ");
                        imgSetting.setImageResource(android.R.color.transparent);
                        rlVwUpdateMain.setVisibility(View.VISIBLE);
                        rlUpdateData.setVisibility(View.GONE);
                        txtVwVehicleName.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        alVehicleDetail.addAll(userData.getData().getVehicles());
                        txtVwVehicleName.setTextColor(getResources().getColor(R.color.app_bg));
                        setVehicleData();
                    }


//                    if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() != 0 &&
                    if (!userData.getData().getProfile_pic().equals("") && userData.getData().getVehicles().size() != 0 &&
                            userData.getData().getCards().size() != 0) {

                        Intent data = new Intent();
                        setResult(COMPLETE_UPDATE_USER_DETAILS, data);

                        rlVwUpdateMain.setVisibility(View.GONE);
                        rlUpdateData.setVisibility(View.GONE);
                        txtAddress.setVisibility(View.VISIBLE);
                        llVehical.setVisibility(View.VISIBLE);


                    } else {


//                        if (userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() == 0
                        if (userData.getData().getProfile_pic().equals("") && userData.getData().getVehicles().size() == 0
                                && userData.getData().getCards().size() == 0) {
                            txtVwCount.setText("3");
                        }

                        //Any two array null set 2
//                        if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() == 0) {
                        if (!userData.getData().getProfile_pic().equals("") && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() == 0) {
                            tvEditVehicle.setText("");
                            imgSetting.setImageResource(android.R.color.transparent);
                            txtVwCount.setText("2");
                        }
//                        else if (userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() == 0) {
                        else if (userData.getData().getProfile_pic().equals("") && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() == 0) {
                            txtVwCount.setText("2");
                        }
//                        else if (userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() != 0) {
                        else if (userData.getData().getProfile_pic().equals("") && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() != 0) {
                            tvEditVehicle.setText("");
                            imgSetting.setImageResource(android.R.color.transparent);
                            txtVwCount.setText("2");
                        }

                        //Any one array null set 1
//                        else if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() == 0) {
                        else if (!userData.getData().getProfile_pic().equals("") && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() == 0) {
                            txtVwCount.setText("1");
                        }
//                        else if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() != 0) {
                        else if (!userData.getData().getProfile_pic().equals("") && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() != 0) {
                            tvEditVehicle.setText("");
                            imgSetting.setImageResource(android.R.color.transparent);
                            txtVwCount.setText("1");
                        }
//                        else if (userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() != 0) {
                        else if (userData.getData().getProfile_pic().equals("") && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() != 0) {
                            txtVwCount.setText("1");
                        }

                        rlVwUpdateMain.setVisibility(View.VISIBLE);
                        rlUpdateData.setVisibility(View.GONE);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (ConnectivityDetector.isConnectingToInternet(this)) {
//            if (isGPSEnable()) {
//                try {
//
//                    fragmentPro = new MapHomeFragment().newInstance(alUserList, "");
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    fragmentTransaction.add(R.id.containerBody, fragmentPro);
//                    fragmentTransaction.addToBackStack(null);
////                    fragmentTransaction.commit();
//                    fragmentTransaction.commitAllowingStateLoss();
//                    mContentFragment = fragmentPro;
//                    drawer_layout.closeDrawer(drawerlist);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                displayLocationSettingsRequest();
//            }
//        } else {
//            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_SHORT).show();
//        }
    }

    private void setListners() {
        tvLegal.setOnClickListener(this);
        ivProPicHome.setOnClickListener(this);
        ivPro.setOnClickListener(this);
        tvDrawUsername.setOnClickListener(this);
        drawer_layout.setOnClickListener(this);
        //8/5/2018
        edtCardDetail.setOnClickListener(this);
        rlVwUpdateMain.setOnClickListener(this);
        txtVwUpdate.setOnClickListener(this);
        rlUpdateData.setOnClickListener(this);
        txtVwProfile.setOnClickListener(this);
        txtVwVehicleName.setOnClickListener(this);
        txtVwpayment.setOnClickListener(this);
//        mFragment.getMapAsync(this);
        txtAddress.setOnClickListener(this);
        rlProfile.setOnClickListener(this);
        rlVehicleNo.setOnClickListener(this);
        rlPaymentNo.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        llEditVehicle.setOnClickListener(this);
        llSetVehicle.setOnClickListener(this);
        btnRequest.setOnClickListener(this);

        tvContact.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvShareYrTrip.setOnClickListener(this);
        imgVwShare.setOnClickListener(this);
        tvChangeTrip.setOnClickListener(this);

//        mFragment.getMapAsync(new OnMapReadyCallback() {
//            @SuppressLint("MissingPermission")
//            @Override
//            public void onMapReady(GoogleMap googleMap1) {
//                mGoogleMap = googleMap1;
//                getLocationPermission();
//                if (mLocationPermissionGranted == true) {
//                    if (mGoogleApiClient == null) {
//                        setUpGoogleApiClient();
//                    }
//                    mGoogleMap.setMyLocationEnabled(true);
//                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
//                    mGoogleMap.getUiSettings().setCompassEnabled(false);
//                }
//
//
//                /*googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//                googleMap.addMarker(new MarkerOptions()
//                        .title("Sydney")
//                        .snippet("The most populous city in Australia.")
//                        .position(sydney));*/
//
//            }
//        });

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void bindView() {

        txtNoNearByDriver = binding.txtNoNearByDriver;
        tvLegal = binding.tvLegal;
        navHeader = binding.navHeader;
        drawer_layout = binding.drawerLayout;
        ivProPicHome = binding.ivProPicHome;
        container_body = binding.containerBody;
        drawerlist = binding.drawerlist;
        ivPro = binding.ivPro;
        tvDrawUsername = binding.tvDrawUsername;
        menuList = binding.menuList;
        tvDriveGreego = binding.tvDriveGreego;

        ivProPicHome.setVisibility(View.VISIBLE);

        //User OnTrip
        rlOnTripUser = binding.rlOnTripUser;
        tvOnTripUser = binding.tvOnTripUser;
        tvOnTripTime = binding.tvOnTripTime;
        tvOnTripDrName = binding.tvOnTripDrName;
        tvOnTripDrCode = binding.tvOnTripDrCode;

//        Driver Personal details
        tvAvanueAddress = binding.tvAvanueAddress;
        llDriverPersDetailsMain = binding.llDriverPersDetailsMain;
        tvAcceptDriverName = binding.tvAcceptDriverName;
        tvPromoCode = binding.tvPromoCode;
        tvContact = binding.tvContact;
        tvCancel = binding.tvCancel;
        tvShareYrTrip = binding.tvShareYrTrip;
        tvChangeTrip = binding.tvChangeTrip;
        tvTripTotalCost = binding.tvTripTotalCost;
        tvUserCardNo = binding.tvUserCardNo;
        ivDriverPro = binding.ivDriverPro;
        imgVwShare = binding.imgVwShare;

        btnRequest = binding.btnRequest;
        txtEstMentCost = binding.txtEstCost;
        edtCardDetail = binding.edtCardDetail;
        imgVwYrCard = binding.imgVwYrCard;
        llCostDesign = binding.llCost;

        llVehical = binding.llVehical;
        llSetVehicle = binding.llSetVehicle;
        llEditVehicle = binding.llEditVehicle;
        imgSetting = binding.imgSetting;
        imgVwVehicle = binding.imgVwVehicle;
        tvEditVehicle = binding.tvEditVehicle;
        tvVehicalData = binding.tvVehicalData;

        btnDone = binding.done;
        txtAddress = binding.txtAddress;
//        mFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mgooleMap);

        rlVwUpdateMain = binding.rlVwUpdateMain;
        rlUpdate = binding.rlUpdate;
        txtVwUpdate = binding.txtVwUpdate;
        rlUpdateData = binding.rlUpdateData;
        txtVwProfile = binding.txtVwProfile;
        txtVwVehicleName = binding.txtVwVehicleName;
        txtVwpayment = binding.txtVwpayment;
        txtVwCount = binding.txtVwCount;

        rlProfile = binding.rlProfile;
        rlVehicleNo = binding.rlVehicleNo;
        rlPaymentNo = binding.rlPaymentNo;
        rlUpdateData.setVisibility(View.GONE);
        rlVwUpdateMain.setVisibility(View.GONE);

    }

    Intent in;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvLegal:
                in = new Intent(HomeActivity.this, TermsCondiActivity.class);
                startActivity(in);
                break;
            case R.id.btnBackHome:
                dialog.dismiss();
                break;

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

            //08/05/2018
            case R.id.txtAddress:

                if (txtNoNearByDriver.getVisibility() == View.GONE) {
                    if (alUserList != null) {
                        for (GetUserData userData : alUserList) {

//                        if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size()
                            if (userData.getData().getProfile_pic() != null && userData.getData().getVehicles().size()
                                    != 0 && userData.getData().getCards().size() != 0) {

                                Intent mIntent = new Intent(context, PickUpLocationActivity.class);
//                            mIntent.putExtra("Currentlocation", location);
//                            mIntent.putExtra("latitude", sourceLatitude);
//                            mIntent.putExtra("langitutde", sourceLongitugde);
                                startActivityForResult(mIntent, PICK_CONTACT_REQUEST);

                            } else {
                                showCheckUserUpdateData("Please complete your updates before proceeding.");
                            }
                        }
                    }
                }

                break;

            case R.id.edtCardDetail:
                in = new Intent(context, PaymentActivity.class);
                startActivityForResult(in, CHANGE_CONTACT_NO);
                break;
            case R.id.btnRequest:
                String selectCard = edtCardDetail.getText().toString();
                SessionManager.saveCardDetails(context, selectCard);

                if (!selectCard.isEmpty()) {
                    showRequestAccepterAlert();

                } else {
                    SnackBar.showError(context, snackBarView, "Please select card first.");
                }


                break;

//            case R.id.llSetVehicle:
//                if (isEditVehical) {
//                    in = new Intent(context, AddEditVehicleActivity.class);
//                    startActivityForResult(in, ADD_EDIT_VEHICAL_REQUEST);
//                } else {
//
//                }
//                break;

            case R.id.llSetVehicle:
//                if (isEditVehical) {
                in = new Intent(context, UserProfileActivity.class);
                startActivityForResult(in, EDIT_VEHICAL_REQUEST);
//                } else {
//
//                }
                break;

            case R.id.rlVwUpdateMain:
                if (rlUpdateData.getVisibility() == View.VISIBLE) {
                    rlUpdateData.setVisibility(View.GONE);
                } else {
                    rlUpdateData.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.txtVwUpdate:
                if (rlUpdateData.getVisibility() == View.VISIBLE) {
                    rlUpdateData.setVisibility(View.GONE);
                } else {
                    rlUpdateData.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.txtVwProfile:
                in = new Intent(context, UserProfileActivity.class);
                startActivityForResult(in, MAP_HOME_PROFILE_UPDATE);

//                fragment = UserProfileFragment.newInstance("", "");
                break;
            case R.id.txtVwVehicleName:
                in = new Intent(context, AddEditVehicleActivity.class);
                startActivityForResult(in, REQUEST_ADD_VEHICLE);
//                        fragment = AddEditVehicleFragment.newInstance("", "");
//                fragment = new V();
                break;
            case R.id.txtVwpayment:
//                fragment = AddPaymentMethodFragment.newInstance("", "");
                in = new Intent(context, AddPaymentMethodActivity.class);
                startActivityForResult(in, REQUEST_ADD_PAYMENT);

                break;

            case R.id.rlProfile:
                in = new Intent(context, UserProfileActivity.class);
                startActivityForResult(in, MAP_HOME_PROFILE_UPDATE);
                break;
            case R.id.rlVehicleNo:
                in = new Intent(context, AddEditVehicleActivity.class);
                startActivityForResult(in, REQUEST_ADD_VEHICLE);
                break;
            case R.id.rlPaymentNo:
                in = new Intent(context, AddPaymentMethodActivity.class);
                startActivityForResult(in, REQUEST_ADD_PAYMENT);
                break;

            case R.id.tvContact:

                if (requestCallPermission()) {
                    Intent in = new Intent(Intent.ACTION_CALL);
                    in.setData(Uri.parse("tel:" + strDriverContact));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(in);
                }

                break;
            case R.id.tvCancel:
                showCallbacksCancelTrip("Are you sure you want to cancel this Trip?");
                break;
            case R.id.tvShareYrTrip:
                shareUserTripDetails();
                break;
            case R.id.imgVwShare:
                shareUserTripDetails();
                break;
            case R.id.tvChangeTrip:
                Intent mIntent = new Intent(context, PickUpLocationActivity.class);
                startActivityForResult(mIntent, PICK_CONTACT_REQUEST);
                break;


        }
    }

    private void showRequestAccepterAlert() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_sent_driver_request);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnSent = (Button) dialog.findViewById(R.id.btnSent);

        TextView tvToAddress = (TextView) dialog.findViewById(R.id.tvToAddress);
        TextView tvFromAddress = (TextView) dialog.findViewById(R.id.tvFromAddress);


        tvToAddress.setText(destLocAddress);
        tvFromAddress.setText(picLocAddress);

        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUserRrequestAPI();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in2 = new Intent(context, HomeActivity.class);
//                startActivity(in2);
//                finish();
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void callRegisterUpdateComplete() {
        if (alUserList.size() != 0) {

            for (GetUserData userData : alUserList) {
                if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size()
                        != 0 && userData.getData().getCards().size() != 0) {

                    drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                    openDrawer();
                } else {
                    drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                    showCheckUserUpdateData("Please complete your updates before proceeding.");
                }
            }
        } else {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            openDrawer();
        }
    }


    public void openDrawer() {
        try {
//            setvalues();
            drawer_layout.bringToFront();
            drawer_layout.requestLayout();

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


    private void displayView(final int pos) {
        try {
            closeDrawer();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent in;

                    switch (pos) {
                        case 0:
                            in = new Intent(context, HomeActivity.class);
                            startActivity(in);
                            finish();
                            break;
                        case 1:
                            in = new Intent(context, PaymentActivity.class);
                            startActivityForResult(in, SLIDER_PAYMENT_METHOD);
                            break;

                        case 2:
                            in = new Intent(HomeActivity.this, TripHistoryActivity.class);
                            startActivityForResult(in, SLIDER_TRIP_HISTORY);
                            break;

                        case 3:
                            in = new Intent(HomeActivity.this, FreeTripsActivity.class);
                            startActivityForResult(in, SLIDER_FREE_TRIP);
                            break;

                        case 4:
                            in = new Intent(HomeActivity.this, HelpCenterActivity.class);
                            startActivityForResult(in, SLIDER_HELP);
                            break;

                        case 5:
                            in = new Intent(HomeActivity.this, SettingActivity.class);
                            startActivityForResult(in, SLIDER_SETTING);
                            break;

                    }


                    drawer_layout.closeDrawer(drawerlist);
                    drawerLayoutAdapter.setSelectedIndex(pos);
                }
            }, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        try {
//            LinearLayout llCostDesignLayout = fragmentPro.getCostDesignLayout();
//            LinearLayout llDriverPersDetailsMainLayout = fragmentPro.getDriverPersDetailsMainLayout();
//            RelativeLayout rlOnTripLayout = fragmentPro.getOnTripLayout();
//
            if (llCostDesign.getVisibility() == View.VISIBLE) {
                showDialog("Request", "Are you sure to cancel request ?", 1);
//            finish();
            } else if (llDriverPersDetailsMain.getVisibility() == View.VISIBLE) {
                showDialog("Request", "Are you sure to cancel request ?", 1);
//                finish();
            } else if (rlOnTripUser.getVisibility() == View.VISIBLE) {
//                showDialog("Request", "Are you Sure to Exit greego ?", 1);
                dialog.cancel();
                Intent in2 = new Intent(context, HomeActivity.class);
                startActivity(in2);
                finish();
//                finish();
            } else {
                showDialog("Exit", "Are you sure to exit Greego App ?", 0);
//                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDialog(String str1, String str2, final int x) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(str1);
        alertDialogBuilder
                .setMessage(str2)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (x == 1) {
                            dialog.cancel();
                            Intent in2 = new Intent(context, HomeActivity.class);
                            startActivity(in2);
                            finish();
                        } else {
                            finish();
                        }


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();


        // show it
        alertDialog.show();


    }

//    @Override
//    public void onBackPressed(Context context) {
//        try {
//            onBackPressed();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    @Override
//    public void onFragmentCall(Context mContext, Fragment myFragment, String Tag) {
//        if (container_body.getVisibility() == View.GONE) {
//            container_body.setVisibility(View.VISIBLE);
//        }
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.containerBody, myFragment, Tag);
//        fragmentStack.push(myFragment);
//        fragmentTransaction.commit();
//    }
//
//    @Override
//    public void callUpdateVehicalMethod(ArrayList<GetUserData.DataBean.VehiclesBean> alVehicleDetail) {
//        MapHomeFragment businessContactFragment = (MapHomeFragment)
//                getSupportFragmentManager().findFragmentByTag( class.getSimpleName());
//        if (businessContactFragment != null) {
//            businessContactFragment.updateVehicalData(alVehicleDetail);
//        }
//    }


// pragnesh

    int statusPickuplocationRout = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (data != null) {
//                mGoogleMap.clear();

                fromLat = data.getStringExtra("sourceLat");
                fromLong = data.getStringExtra("sourceLng");
                toLat = data.getStringExtra("destinationLat");
                toLong = data.getStringExtra("destinationLng");
                picLocAddress = data.getStringExtra("picLocAddress");
                destLocAddress = data.getStringExtra("destLocAddress");
                duratioTime = data.getStringExtra("departure");

                durationOnlyMin = duratioTime.substring(0, 2).trim();

                llVehical.setVisibility(View.GONE);
                txtAddress.setVisibility(View.GONE);
                llCostDesign.setVisibility(View.VISIBLE);
                llDriverPersDetailsMain.setVisibility(View.GONE);

                llDriverPersDetailsMain.setElevation(-10f);


                mGoogleMap.clear();
                pickupPoint = new LatLng(Double.parseDouble(fromLat), Double.parseDouble(fromLong));
                dropPoint = new LatLng(Double.parseDouble(toLat), Double.parseDouble(toLong));

                if (pickupPoint == null) {
                    SnackBar.showError(context, snackBarView, "Pickup location not found.");
                } else if (dropPoint == null) {
                    SnackBar.showError(context, snackBarView, "Drop Point location not found.");
                } else {
                    statusPickuplocationRout = 1;
                    proDialog = new ProgressDialog(HomeActivity.this);
                    proDialog.setMessage("Drawing the route, please wait!");
                    proDialog.setIndeterminate(false);
                    proDialog.setCancelable(false);
                    proDialog.show();

                    createRoute(pickupPoint, dropPoint);
                }

                setCardData();

            }
        } else if (requestCode == ADD_EDIT_VEHICAL_REQUEST) {
            if (data != null) {

                strManufacur = SessionManager.getUpdatedVehical(context).getManufacture();
                strModel = SessionManager.getUpdatedVehical(context).getModel();
                strYear = SessionManager.getUpdatedVehical(context).getYear();
                strColor = SessionManager.getUpdatedVehical(context).getColor();

                callUserMeApi();
            }

        } else if (requestCode == EDIT_VEHICAL_REQUEST) {
            callUserMeApi();
        } else if (requestCode == REQUEST_ADD_VEHICLE) {
            callUserMeApi();
        } else if (requestCode == MAP_HOME_PROFILE_UPDATE) {
            callUserMeApi();
        } else if (requestCode == REQUEST_ADD_PAYMENT) {
            callUserMeApi();
        } else if (requestCode == HOME_SLIDER_PROFILE_UPDATE) {
            callUserMeApi();
        } else if (requestCode == CHANGE_CONTACT_NO) {

            String changeCardNo = data.getStringExtra("changeCardNo");

            try {
                if (changeCardNo != null) {
                    byte[] data1 = Base64.decode(changeCardNo, Base64.DEFAULT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        cardNo = new String(data1, StandardCharsets.UTF_8);

                        String s = cardNo;

                        String s4 = s.substring(12, 16);
                        String strCardNumber = "**** **** **** " + s4;

                        if (strCardNumber != null) {
                            edtCardDetail.getCardNumber(cardNo);
                            edtCardDetail.isValid(cardNo);

                            imgVwYrCard.getCardNumber(cardNo);
                            imgVwYrCard.isValid(cardNo);

                            edtCardDetail.setText(strCardNumber);
                            edtCardDetail.getCardType();
                        } else {
                            SnackBar.showError(context, snackBarView, "Please select card first.");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == SLIDER_PAYMENT_METHOD) {
            callUserMeApi();
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            setHomeValues();
        } else if (requestCode == RESULT_CANCELED) {
        } else if (requestCode == COMPLETE_UPDATE_USER_DETAILS) {
            callRegisterUpdateComplete();
        } else if (requestCode == 1212) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(15000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).run();

            if (!isGPSEnable()) {
                CheckGpsStatus();
            } else {
                //   if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
                // }
                initilizeMap();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {

            case MY_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Premission Granted Successfully.
                    setHomeValues();
                } else {
                    //  Did not Granted Permission.
                }
                break;

            case MY_PERMISSIONS_REQUEST_ACCOUNTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            setUpGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;


            }
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    if (mLocationPermissionGranted == true) {
                        if (mGoogleApiClient == null) {
                            setUpGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
//                        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        mGoogleMap.getUiSettings().setCompassEnabled(false);
                    }
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
                        alVehicleDetail = new ArrayList<>();

                        if (userDetail.getError_code() == 0) {

                            alUserList.add(userDetail);
                            alVehicleDetail.addAll(userDetail.getData().getVehicles());

                            Applog.E("UserUpdate==>Dg==>" + userDetail);

                            String userName = userDetail.getData().getName();
//                            ivPro.setImageURI(Uri.parse(userDetail.getData().getProfile_pic()));
//                            ivProPicHome.setImageURI(Uri.parse(userDetail.getData().getProfile_pic()));
                            tvDrawUsername.setText(userName);

                            profilePic = userDetail.getData().getProfile_pic();
                            if (profilePic != null) {
                                Glide.clear(ivPro);
                                Glide.with(getApplicationContext())
                                        .load(profilePic)
                                        .centerCrop()
                                        .signature(new StringSignature(UUID.randomUUID().toString()))
                                        .crossFade().skipMemoryCache(true)
                                        .into(ivPro);

                            } else {
                                ivPro.setImageResource(R.mipmap.ic_place_holder);
                            }

                            if (profilePic != null) {
                                Glide.clear(ivProPicHome);
                                Glide.with(getApplicationContext())
                                        .load(profilePic)
                                        .centerCrop()
                                        .signature(new StringSignature(UUID.randomUUID().toString()))
                                        .crossFade().skipMemoryCache(true)
                                        .into(ivProPicHome);

                            } else {
                                ivProPicHome.setImageResource(R.mipmap.ic_place_holder);
                            }


//                            setVehicleData();


                            if (Build.VERSION.SDK_INT < 23) {
                                setHomeValues();
                            } else {
                                if (checkSelfPermission()) {
                                    setHomeValues();
                                }
                            }

                            SessionManager.saveUserDetails(context, userDetail);

//                            SessionManager.saveUserData(context, userDetails);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//
                            //getIs_agreed = 0 new user

//
                        } else if (userDetail.getError_code() == 1) {
                            logout();
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

    private void logout() {
        SessionManager.clearAppSession(context);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void CheckGpsStatus() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (GpsStatus == true) {
//            Toast.makeText(this, "Location Services Is Enabled", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Location Services Is Disabled", Toast.LENGTH_SHORT).show();
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please enable GPS first.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 1212);
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void callVehicleSelectApi(int vehicle_id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.SELECT_VEHICLE.PARAM_VEHICLE_ID, vehicle_id);
            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SELECT_VEHICLE.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    MyProgressDialog.hideProgressDialog();
                    if (flagSelectVehical == false) {
                        callUserMeApi();
                    }
//                    notifyDataSetChanged();


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressDialog.hideProgressDialog();
                    Applog.E("Error: " + error.getMessage());


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

    private void callUserTripDetailsAPI(String tripId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.GET_USER_TRIP_DETAILS.PARAM_TRIP_ID, Integer.parseInt(tripId));

            Applog.E("request: trip " + jsonObject.toString());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.GET_USER_TRIP_DETAILS.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    try {
                        UserDriverTripDetails driverTripDetails = new Gson().fromJson(String.valueOf(response), UserDriverTripDetails.class);

                        alUserDrTripDetails = new ArrayList<>();

                        if (driverTripDetails.getError_code() == 0) {

                            alUserDrTripDetails.add(driverTripDetails.getData());

                            Applog.E("size==>" + alUserDrTripDetails);
                            MyProgressDialog.hideProgressDialog();

                            setDriverTripDetails(alUserDrTripDetails);

                        } else {
                            MyProgressDialog.hideProgressDialog();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressDialog.hideProgressDialog();
                    Applog.E("Error: " + error.getMessage());
//                    SnackBar.showError(this, snackBarView, getResources().getString(R.string.something_went_wrong));
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

            MyProgressDialog.hideProgressDialog();
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    GlobalValues.TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int diffTotelMin;
    String tripToatalTime;

    private int CalculateTime(String tripStartTime, String tripEndTime) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

            Date startDate = simpleDateFormat.parse(tripStartTime);
            Date endDate = simpleDateFormat.parse(tripEndTime);

            long difference = endDate.getTime() - startDate.getTime();

            int timeInSeconds = (int) (difference / 1000);
            int hours, minutes, seconds;
            hours = timeInSeconds / 3600;
            timeInSeconds = timeInSeconds - (hours * 3600);
            minutes = timeInSeconds / 60;
            timeInSeconds = timeInSeconds - (minutes * 60);
            seconds = timeInSeconds;

            String diffTime = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds) + " h";
            Applog.E("Total Time==>" + diffTime);
            tripToatalTime = (timeInSeconds) + "";
            double a = timeInSeconds * 0.0166667;
            Applog.E("Total Time===>" + a);
      /*      if (difference < 0) {
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
            }
            int days = (int) (difference / (1000  60  60 * 24));
            int hours = (int) ((difference - (1000  60  60  24  days)) / (1000  60  60));
            int min = (int) (difference - (1000  60  60  24  days) - (1000  60  60  hours)) / (1000  60);
            Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);
*/
            diffTotelMin = Integer.parseInt(diffTime);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return diffTotelMin;
    }

    double addTip, totalTip, tip10Per, tip15Per, tip20Per;
    double amount;

    private void showAddTrip(final String tripAmount, final String strTripId) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_trip);

        final RelativeLayout rl10PerTip = (RelativeLayout) dialog.findViewById(R.id.rl10PerTip);
        final RelativeLayout rl15PerTip = (RelativeLayout) dialog.findViewById(R.id.rl15PerTip);
        final RelativeLayout rl20PerTip = (RelativeLayout) dialog.findViewById(R.id.rl20PerTip);

        final TextView tvTotalAmount = (TextView) dialog.findViewById(R.id.tvTotalAmount);

        final TextView tv10PerTip = (TextView) dialog.findViewById(R.id.tv10PerTip);
        final TextView tv15PerTip = (TextView) dialog.findViewById(R.id.tv15PerTip);
        final TextView tv20PerTip = (TextView) dialog.findViewById(R.id.tv20PerTip);
        final EditText edtTvAddTip = (EditText) dialog.findViewById(R.id.edtTvAddTip);

        CheckBox chkNoTip = (CheckBox) dialog.findViewById(R.id.chkNoTip);
        Button btnSubmitTip = (Button) dialog.findViewById(R.id.btnSubmitTip);

        tvTotalAmount.setText("$" + new DecimalFormat("##.#").format(Double.valueOf(tripAmount)));

        try {
            if (tripAmount != null) {

                amount = Double.parseDouble(tripAmount);

                tip10Per = (amount * 10) / 100;
                tv10PerTip.setText("$" + new DecimalFormat("##.#").format(Double.valueOf(tip10Per)));

                tip20Per = (amount * 20) / 100;
                tv20PerTip.setText("$" + new DecimalFormat("##.#").format(Double.valueOf(tip20Per)));

                tip15Per = (amount * 15) / 100;
                tv15PerTip.setText("$" + new DecimalFormat("##.#").format(Double.valueOf(tip15Per)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        edtTvAddTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTip = 0.00;
                rl10PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                tv10PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                rl15PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                tv15PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                rl20PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                tv20PerTip.setTextColor(getResources().getColor(R.color.app_bg));

            }
        });

        rl10PerTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl10PerTip.setBackground(getResources().getDrawable(R.drawable.round_bg));
                tv10PerTip.setTextColor(getResources().getColor(R.color.white));

                rl15PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                tv15PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                rl20PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                tv20PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                addTip = tip10Per;
            }
        });

        rl15PerTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl15PerTip.setBackground(getResources().getDrawable(R.drawable.round_bg));
                tv15PerTip.setTextColor(getResources().getColor(R.color.white));

                rl10PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                tv10PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                rl20PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                tv20PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                addTip = tip15Per;
            }
        });

        rl20PerTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl20PerTip.setBackground(getResources().getDrawable(R.drawable.round_bg));
                tv20PerTip.setTextColor(getResources().getColor(R.color.white));

                rl10PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                tv10PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                rl15PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                tv15PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                addTip = tip20Per;
            }
        });

        chkNoTip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rl20PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                    tv20PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                    rl10PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                    tv10PerTip.setTextColor(getResources().getColor(R.color.app_bg));

                    rl15PerTip.setBackground(getResources().getDrawable(R.drawable.round_white_bg));
                    tv15PerTip.setTextColor(getResources().getColor(R.color.app_bg));
                    edtTvAddTip.setEnabled(false);
                    rl10PerTip.setEnabled(false);
                    rl15PerTip.setEnabled(false);
                    rl20PerTip.setEnabled(false);
                    addTip = 0.00;

                } else {
                    rl10PerTip.setEnabled(true);
                    rl15PerTip.setEnabled(true);
                    rl20PerTip.setEnabled(true);
                    edtTvAddTip.setEnabled(true);
                    edtTvAddTip.requestFocus();
                }
            }
        });
        btnSubmitTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addTipValues = edtTvAddTip.getText().toString();

                if (!addTipValues.isEmpty()) {
                    totalTip = Double.parseDouble(addTipValues);
                } else {
                    totalTip = addTip;
                }

                callAddTipDriver(tripAmount, totalTip, strTripId);

//                SnackBar.showSuccess(context, snackBarView, "Tip Add successfully.");
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void callAddTipDriver(final String tripAmount, final double totalTipFinal, final String strTripId) {
        try {

            JSONObject jsonObject = new JSONObject();

            if (totalTipFinal != 0) {
                jsonObject.put(WebFields.TIP_UPDATE.TIP_AMOUNT, totalTipFinal);
            } else {
                jsonObject.put(WebFields.TIP_UPDATE.TIP_AMOUNT, 0);
            }

            Applog.E("Trip Id =====>" + SessionManager.getTripId(context) + "===trip iD===>" + strTripId);

            jsonObject.put(WebFields.TIP_UPDATE.TRIP_ID, strTripId);

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.TIP_UPDATE.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    try {
                        if (response.getString("error_code").equals("0")) {

                            MyProgressDialog.hideProgressDialog();

                            double tempAmt = Double.valueOf(tripAmount);
                            double tempAmtount = tempAmt + totalTipFinal;

                            Intent in = new Intent(context, TripCompleteChargePayActivity.class);
                            in.putExtra("totalCost", new DecimalFormat("##.##").format(Double.valueOf(tempAmtount)));
                            in.putExtra("driverName", strTripDriverName);
                            in.putExtra("driverPic", driverPicUrl);
                            in.putExtra("fromAddress", strTripToAddress);
                            in.putExtra("tripId", strTripId);
                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                            finish();
                        } else {
                            MyProgressDialog.hideProgressDialog();
                            SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                        }
                    } catch (Exception e) {
//                        callUserTripDetailsAPI(strTripId);
                        MyProgressDialog.hideProgressDialog();
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



