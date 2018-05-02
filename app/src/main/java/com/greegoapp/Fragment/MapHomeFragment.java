package com.greegoapp.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.greegoapp.Activity.AddEditVehicleActivity;
import com.greegoapp.Activity.AddPaymentMethodActivity;
import com.greegoapp.Activity.HomeActivity;
import com.greegoapp.Activity.PaymentActivity;
import com.greegoapp.Activity.PickUpLocationActivity;
import com.greegoapp.Activity.TripCompleteChargePayActivity;
import com.greegoapp.Activity.UserProfileActivity;
import com.greegoapp.AppController.AppController;
import com.greegoapp.FCM.NotificationUtils;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.Model.GetTripRate;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.Model.UserDriverTripDetails;
import com.greegoapp.Model.UserNearDriverList;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.FragmentMapHomeBinding;
import com.greegoapp.polilineAnimator.MapAnimator;
import com.greegoapp.polilineAnimator.MapHttpConnection;
import com.greegoapp.polilineAnimator.PathJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static com.greegoapp.Activity.HomeActivity.HOME_SLIDER_PROFILE_UPDATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class MapHomeFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener,
        GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentMapHomeBinding binding;
    private View snackBarView;
    Context context;

    String TAG = MapHomeFragment.class.getSimpleName();

    /*  Google Map  */
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

    private static final long MOVE_ANIMATION_DURATION = 1000;
    public static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1001;
    public static final int MAP_HOME_PROFILE_UPDATE = 1002;
    public static final int PICK_CONTACT_REQUEST = 1003;  // The request code
    public static final int ADD_EDIT_VEHICAL_REQUEST = 2000;  // The request code
    public static final int CHANGE_CONTACT_NO = 2001;
    private long TURN_ANIMATION_DURATION = 500;
    public static final int COMPLETE_UPDATE_USER_DETAILS = 0007;
    public static final String REQUEST_USER_TRIP = "1250";

    LocationManager locationManager;
    boolean GpsStatus;


    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug


    private Location mylocation;
    Geocoder geocoder;
    List<Address> addresses;

    private Location lastLocation;
    LatLng latLng;
    TextView txtAddress;
    Button btnDone;
    String location, areaName, sourceLatitude, sourceLongitude;

    //Mujju
    RelativeLayout rlVwUpdateMain, rlUpdate, rlUpdateData;
    TextView txtVwProfile, txtVwVehicleName, txtVwpayment, txtVwUpdate, txtVwCount;
    RelativeLayout rlProfile, rlVehicleNo, rlPaymentNo;
    CallFragmentInterface callMyFragment;
    BackPressedFragment backPressed;

    // pragnesh
    private List<LatLng> routeList;

    private Marker marker;
    Bitmap mMarkerIcon;
    private int mIndexCurrentPoint = 0;
    private LatLng pickupPoint, dropPoint;
    Double dist;
    int mapZoomLevel;
    ArrayList<GetUserData> alUserDetails = new ArrayList<>();

    //Vehicle 17_4_2018
    LinearLayout llEditVehicle, llSetVehicle, llVehical;
    ImageView imgSetting, imgVwVehicle;
    TextView tvVehicalData, tvEditVehicle;
    String strManufacur, strModel, strColor;
    int strYear;
    boolean isEditVehical = false;


    LinearLayout llEditVechicleDesign, llCostDesign;
    TextView txtEstMentCost;
    TextView edtCardDetail;
    public String cardNo, statCode;
    float base_fee, time_fee, mile_fee, trip_price, over_mile_fee, tripCost;
    String duration, duration1;
    Button btnRequest;
    String duratioTime;

    //Mujju 19_4_18
//    ArrayList<UserNearDriverList> alUserNearDriver;
    ArrayList<UserNearDriverList.DataBean> alUserNearDriverDtls;
    ArrayList<GetUserData.DataBean.VehiclesBean> alVehicleDetail = new ArrayList<>();

    LinearLayout llDriverPersDetailsMain;

    MyReceiver myReceiver;

    //Trip Details
    TextView tvAcceptDriverName, tvPromoCode, tvContact, tvCancel, tvShareYrTrip, tvChangeTrip, tvTripTotalCost, tvUserCardNo;
    ImageView ivDriverPro, imgVwShare;
    String userRequestId;
    int vehicalId, vehicalSelectId;

    ArrayList<UserDriverTripDetails.DataBean> userDrTripDetails;

    //On trip Details
    RelativeLayout rlOnTripUser;
    TextView tvOnTripUser, tvOnTripTime, tvOnTripDrName, tvOnTripDrCode, tvAvanueAddress;
    String strDriverContact;
    String strTripDriverName, strTripDriverContactNo, strTripFromAddress, strTripToAddress, strTripId, strTripStatus, driverPicUrl, totalTripAmount;

    //Priyanka
    Dialog dialog;
    String strVehicalData = null;
    String picLocAddress, destLocAddress, fromLat, fromLong, toLat, toLong;
    public static final int EDIT_VEHICAL_REQUEST = 11011;
    private String driver_id;

    LatLng userlatLng;

    public static MapHomeFragment newInstance(ArrayList<GetUserData> userData, String param2) {
        MapHomeFragment fragment = new MapHomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("userData", userData);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            alUserDetails = getArguments().getParcelableArrayList("userData");
//            driverData = getArguments().getString("driverData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_home, container, false);
        View view = binding.getRoot();
        snackBarView = getActivity().findViewById(android.R.id.content);
        context = getActivity();

        bindViews();

        //Mujju
//        callUserMeApi();
        setListners();

        setFirstTimeRegister();

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
//            CheckGpsStatus();
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void setVehicleData() {
        if (alVehicleDetail.size() != 0) {

            for (GetUserData.DataBean.VehiclesBean vehicalData : alVehicleDetail) {
                if (vehicalData.getSelected().matches("1")) {
                    vehicalId = Integer.parseInt(vehicalData.getVehicle_id());
                    strManufacur = vehicalData.getVehicle_name();
                    strModel = vehicalData.getVehicle_model();
                    strColor = vehicalData.getColor();
                    strYear = Integer.parseInt(vehicalData.getYear());
                    strVehicalData = strManufacur + " " + strModel + " " + strYear + " " + strColor;

                    imgSetting.setImageResource(R.mipmap.ellipse_6);
                    tvEditVehicle.setText(strVehicalData);
                }
            }
        } else {
            imgSetting.setImageResource(android.R.color.transparent);
            tvEditVehicle.setText("");
        }

        tvVehicalData.setText("Edit your vehicle");
        imgVwVehicle.setImageResource(R.mipmap.setup);

    }


    private void callDriverRequestAccepted() {
        myReceiver = new MyReceiver();
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(REQUEST_USER_TRIP);
            getActivity().registerReceiver(myReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            userDrTripDetails = new ArrayList<>();
            userDrTripDetails = arg1.getParcelableArrayListExtra("driverAllData");

            strTripId = arg1.getStringExtra("tripId");
            strTripStatus = arg1.getStringExtra("tripStatus");


            if (strTripStatus.matches(GlobalValues.DRIVER_ASSIGNED)) {
                setDriverTripDetails(userDrTripDetails);
            }
//            else if (strTripStatus.matches(GlobalValues.DRIVER_ON_WAY)) {
////                llVehical.setVisibility(View.GONE);
////                txtAddress.setVisibility(View.GONE);
////                llCostDesign.setVisibility(View.GONE);
////                llDriverPersDetailsMain.setVisibility(View.GONE);
////                rlOnTripUser.setVisibility(View.VISIBLE);
//                SnackBar.showSuccess(context, snackBarView, "Driver on way.");
//            }
            else if (strTripStatus.matches(GlobalValues.DRIVER_WAITING)) {
                SnackBar.showSuccess(context, snackBarView, "Driver waiting.");
            } else if (strTripStatus.matches(GlobalValues.DRIVER_ONGOING)) {
                llVehical.setVisibility(View.GONE);
                txtAddress.setVisibility(View.GONE);
                llCostDesign.setVisibility(View.GONE);
                llDriverPersDetailsMain.setVisibility(View.GONE);
                rlOnTripUser.setVisibility(View.VISIBLE);
                SnackBar.showSuccess(context, snackBarView, "Driver on going.");
            } else if (strTripStatus.matches(GlobalValues.DRIVER_COMPLETED)) {


                Intent in = new Intent(context, TripCompleteChargePayActivity.class);
                in.putExtra("totalCost", totalTripAmount);
                in.putExtra("driverName", strTripDriverName);
                in.putExtra("driverPic", driverPicUrl);
                in.putExtra("fromAddress", strTripToAddress);
                in.putExtra("tripId", strTripId);
                startActivity(in);
                getActivity().finish();

                SnackBar.showSuccess(context, snackBarView, "Trip completed.");
            }

        }

    }


    private void setDriverTripDetails(ArrayList<UserDriverTripDetails.DataBean> userDrTripDetails) {

        for (UserDriverTripDetails.DataBean tripDetails : userDrTripDetails) {

            userRequestId = tripDetails.getRequest_id();


            driver_id = tripDetails.getDriver_id();
            double userLat = Double.parseDouble(tripDetails.getRequest().getFrom_lat());
            double userLong = Double.parseDouble(tripDetails.getRequest().getFrom_lng());
            double drvLat = Double.parseDouble(tripDetails.getRequest().getTo_lat());
            double drvLong = Double.parseDouble(tripDetails.getRequest().getFrom_lng());


            dropPoint = new LatLng(drvLat, drvLong);
            userlatLng = new LatLng(userLat, userLong);

            if (tripDetails.getStatus().matches(GlobalValues.DRIVER_CANCELLED)) {
                SnackBar.showSuccess(context, snackBarView, "Request cancel.");
            } else if (tripDetails.getStatus().matches(GlobalValues.DRIVER_ASSIGNED)) {

                mGoogleMap.clear();

                driverStatus = true;

                createMarker(drvLat, drvLong, "Driver", "Show");
                createRoute(userlatLng, dropPoint);

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


//            for (UserDriverTripDetails.DataBean tripDetails : userDrTripDetails) {


                driverPicUrl = tripDetails.getDriver().getProfile_pic();
                totalTripAmount = tripDetails.getTotal_trip_amount();

                if (driverPicUrl != null) {
                    Glide.clear(ivDriverPro);
                    Glide.with(context)
                            .load(driverPicUrl)
                            .centerCrop()
                            .signature(new StringSignature(UUID.randomUUID().toString()))
                            .crossFade().skipMemoryCache(true)
                            .into(ivDriverPro);

                } else {
                    ivDriverPro.setImageResource(R.mipmap.ic_user_profile);
                }

                strTripDriverName = tripDetails.getDriver().getName();
                strTripDriverContactNo = tripDetails.getDriver().getContact_number();
                strTripFromAddress = tripDetails.getRequest().getFrom_address();
                strTripToAddress = tripDetails.getRequest().getTo_address();

                tvTripTotalCost.setText("$" + totalTripAmount);
                tvAvanueAddress.setText(strTripToAddress);
                tvAcceptDriverName.setText(tripDetails.getDriver().getName());
                tvPromoCode.setText(tripDetails.getDriver().getPromocode());
                strDriverContact = tripDetails.getDriver().getContact_number();

                SnackBar.showSuccess(context, snackBarView, "Driver Assigned.");

                tvOnTripTime.setText(tripDetails.getActual_trip_travel_time());
                tvOnTripDrName.setText(tripDetails.getDriver().getName());
                tvOnTripDrCode.setText(tripDetails.getDriver().getPromocode());

            }

        }

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
//                    UserNearDriverList.DataBean nrDriverData = new Gson().fromJson(String.valueOf(response), UserNearDriverList.DataBean.class);
                    alUserNearDriverDtls = new ArrayList<>();
                    try {
                        MyProgressDialog.hideProgressDialog();
                        if (nearDriver.getError_code() == 0) {

                            if (nearDriver.getData().size() != 0) {
                                alUserNearDriverDtls.addAll(nearDriver.getData());
//                            alUserNearDriverDtls.add(nrDriverData);
                                Applog.E("near array size after==>" + alUserNearDriverDtls.size());


                                setNearDriver();
                            }

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

                        createMarker(drvLat, drvLong, "Driver", "Show");

                        dropPoint = new LatLng(drvLat, drvLong);

                        createRoute(userlatLng, dropPoint);
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


    private void setFirstTimeRegister() {
        try {

            rlUpdateData.setVisibility(View.GONE);

            if (alUserDetails != null) {

                for (GetUserData userData : alUserDetails) {


                    if (userData.getData().getProfile_pic() == null) {
//                    if (userData.getData().getProfile_pic().matches("")) {
                        rlVwUpdateMain.setVisibility(View.VISIBLE);
                        rlUpdateData.setVisibility(View.GONE);
                        txtVwProfile.setTextColor(getResources().getColor(R.color.hint_color));
                    } else {
                        txtVwProfile.setTextColor(getResources().getColor(R.color.app_bg));
                    }


                    if (userData.getData().getCards().size() == 0) {
                        rlVwUpdateMain.setVisibility(View.VISIBLE);
                        rlUpdateData.setVisibility(View.GONE);
                        txtVwpayment.setTextColor(getResources().getColor(R.color.hint_color));
                    } else {
                        txtVwpayment.setTextColor(getResources().getColor(R.color.app_bg));
                    }

                    if (userData.getData().getVehicles().size() == 0) {
                        tvEditVehicle.setText(" ");
                        rlVwUpdateMain.setVisibility(View.VISIBLE);
                        rlUpdateData.setVisibility(View.GONE);
                        txtVwVehicleName.setTextColor(getResources().getColor(R.color.hint_color));
                    } else {
                        alVehicleDetail.addAll(userData.getData().getVehicles());
                        txtVwVehicleName.setTextColor(getResources().getColor(R.color.app_bg));
                        setVehicleData();
                    }


//                    if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() != 0 &&
                    if (userData.getData().getProfile_pic() != null && userData.getData().getVehicles().size() != 0 &&
                            userData.getData().getCards().size() != 0) {

                        Intent data = new Intent();
                        getActivity().setResult(COMPLETE_UPDATE_USER_DETAILS, data);

                        rlVwUpdateMain.setVisibility(View.GONE);
                        rlUpdateData.setVisibility(View.GONE);
                        txtAddress.setVisibility(View.VISIBLE);
                        llVehical.setVisibility(View.VISIBLE);


                    } else {
//                        if (userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() == 0
                        if (userData.getData().getProfile_pic() == null && userData.getData().getVehicles().size() == 0
                                && userData.getData().getCards().size() == 0) {
                            txtVwCount.setText("3");
                        }

                        //Any two array null set 2
//                        if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() == 0) {
                        if (userData.getData().getProfile_pic() != null && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() == 0) {
                            tvEditVehicle.setText("");
                            txtVwCount.setText("2");
                        }
//                        else if (userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() == 0) {
                        else if (userData.getData().getProfile_pic() == null && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() == 0) {
                            txtVwCount.setText("2");
                        }
//                        else if (userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() != 0) {
                        else if (userData.getData().getProfile_pic() == null && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() != 0) {
                            tvEditVehicle.setText("");
                            txtVwCount.setText("2");
                        }

                        //Any one array null set 1
//                        else if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() == 0) {
                        else if (userData.getData().getProfile_pic() != null && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() == 0) {
                            txtVwCount.setText("1");
                        }
//                        else if (!userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() != 0) {
                        else if (userData.getData().getProfile_pic() != null && userData.getData().getVehicles().size() == 0 && userData.getData().getCards().size() != 0) {
                            tvEditVehicle.setText("");
                            txtVwCount.setText("1");
                        }
//                        else if (userData.getData().getProfile_pic().matches("") && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() != 0) {
                        else if (userData.getData().getProfile_pic() == null && userData.getData().getVehicles().size() != 0 && userData.getData().getCards().size() != 0) {
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
    }

    private void setListners() {
        edtCardDetail.setOnClickListener(this);
        rlVwUpdateMain.setOnClickListener(this);
        txtVwUpdate.setOnClickListener(this);
        rlUpdateData.setOnClickListener(this);
        txtVwProfile.setOnClickListener(this);
        txtVwVehicleName.setOnClickListener(this);
        txtVwpayment.setOnClickListener(this);
        mFragment.getMapAsync(this);
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
    }

    private void bindViews() {
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
        mFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mgooleMap);

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

//        strManufacur = SessionManager.getUpdatedVehical(context).getManufacture();
//        strModel = SessionManager.getUpdatedVehical(context).getModel();
//        strYear = SessionManager.getUpdatedVehical(context).getYear();
//        strColor = SessionManager.getUpdatedVehical(context).getColor();
//
//        if (strManufacur.matches("Edit your vehicle")) {
//            tvVehicalData.setText("Edit your vehicle");
//            imgVwVehicle.setImageResource(R.mipmap.setup);
//
//            tvEditVehicle.setText("");
//            isEditVehical = false;
//        } else {
//            isEditVehical = true;
//            tvVehicalData.setText("Edit your vehicle");
//            imgVwVehicle.setImageResource(R.mipmap.setup);
//
//            if (strYear == 0) {
//                tvEditVehicle.setText("");
//            } else {
//                tvEditVehicle.setText(strManufacur + " " + strModel + " " + strYear + " " + strColor);
//                imgSetting.setImageResource(R.mipmap.ellipse_1);
//            }
//
//
//        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallFragmentInterface) {
            callMyFragment = (CallFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CallFragmentInterface");
        }

        if (context instanceof BackPressedFragment) {
            backPressed = (BackPressedFragment) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CallFragmentInterface");
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                llVehical.setVisibility(View.GONE);
                txtAddress.setVisibility(View.GONE);
                llCostDesign.setVisibility(View.VISIBLE);
                llDriverPersDetailsMain.setVisibility(View.GONE);

                pickupPoint = new LatLng(Double.parseDouble(fromLat), Double.parseDouble(fromLong));
                dropPoint = new LatLng(Double.parseDouble(toLat), Double.parseDouble(toLong));

                createRoute(pickupPoint, dropPoint);
            }
        } else if (requestCode == ADD_EDIT_VEHICAL_REQUEST) {
            if (data != null) {

                strManufacur = SessionManager.getUpdatedVehical(context).getManufacture();
                strModel = SessionManager.getUpdatedVehical(context).getModel();
                strYear = SessionManager.getUpdatedVehical(context).getYear();
                strColor = SessionManager.getUpdatedVehical(context).getColor();

                String manu = data.getStringExtra("manufacture");
                String model = data.getStringExtra("model");
                String yr = data.getStringExtra("year");
                String clo = data.getStringExtra("color");
               /* tvVehicalData.setText(strManufacur + " " + strModel + " " + strYear + " " + strColor);
                tvEditVehicle.setText("Edit your vehicle");
                imgSetting.setImageResource(R.mipmap.setup);
                imgVwVehicle.setImageResource(R.mipmap.ellipse_1);*/
                callUserMeApi();
//                setVehicleData();
//                if (strManufacur.matches("Edit your vehicle") && strYear == 0) {
//                    tvVehicalData.setText("Edit your vehicle");
//                    imgVwVehicle.setImageResource(R.mipmap.setup);
//
////                    tvEditVehicle.setText("");
//                    isEditVehical = false;
//                } else {
//                    isEditVehical = true;
//                    tvVehicalData.setText("Edit your vehicle");
//                    imgVwVehicle.setImageResource(R.mipmap.setup);
//
////                    tvEditVehicle.setText(manu + " " + model + " " + yr + " " + clo);
//                    tvEditVehicle.setText(strManufacur + " " + strModel + " " + strYear + " " + strColor);
//                    imgSetting.setImageResource(R.mipmap.ellipse_1);
//                }

            }

        } else if (requestCode == EDIT_VEHICAL_REQUEST) {
            callUserMeApi();
//            setVehicleData();
        } else if (requestCode == REQUEST_ADD_VEHICLE) {
            callUserMeApi();
//            setVehicleData();
        } else if (requestCode == MAP_HOME_PROFILE_UPDATE) {
            callUserMeApi();
//            setVehicleData();
        } else if (requestCode == REQUEST_ADD_PAYMENT) {
            callUserMeApi();
//            setVehicleData();
        } else if (requestCode == HOME_SLIDER_PROFILE_UPDATE) {
            callUserMeApi();
//            setVehicleData();
        } else if (requestCode == CHANGE_CONTACT_NO) {
            if (data != null) {
                String changeCardNo = data.getStringExtra("changeCardNo");

                String s4 = changeCardNo.substring(12, 16);
                String strCardNumber = "**** **** **** " + s4;
                edtCardDetail.setText(strCardNumber);
            }
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        Intent in;
        switch (view.getId()) {

            //Dialog box
            case R.id.btnBackHome:
                dialog.dismiss();
                break;

            case R.id.txtAddress:

                if (alUserDetails != null) {
                    for (GetUserData userData : alUserDetails) {

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

                break;

            case R.id.edtCardDetail:
                in = new Intent(context, PaymentActivity.class);
                startActivityForResult(in, CHANGE_CONTACT_NO);
                break;
            case R.id.btnRequest:
                callUserRrequestAPI();
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
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + strDriverContact));
                    context.startActivity(intent);
                }

                break;
            case R.id.tvCancel:
                showCallbacksCancelTrip("Are you sure you want to Cancel Trip?");
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

        if (fragment != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.containerBody, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }

    // Method to share either text or URL.
    private void shareUserTripDetails() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
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

                    Intent mIntent = new Intent(context, HomeActivity.class);
                    startActivity(mIntent);
                    getActivity().finish();

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
                    if (vehicalData.getSelected().matches("1")) {
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
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_TOTAL_EST_TRAVEL_TIME, 12);
            int estTripCost = Math.round(tripCost);
            jsonObject.put(WebFields.USER_ADD_REQUEST.PARAM_TOTAL_EST_TRIP_COST, estTripCost);


            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.USER_ADD_REQUEST.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

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

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.####");
        return Double.valueOf(twoDForm.format(d));
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
        Intent mIntent = new Intent(context, PickUpLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("pickupPointLatitud", String.valueOf(pickupPoint.latitude));
        bundle.putString("pickupPointLongitude", String.valueOf(pickupPoint.longitude));
        bundle.putString("dropPointLatitude", String.valueOf(dropPoint.latitude));
        bundle.putString("dropPointLongitude", String.valueOf(dropPoint.longitude));
        mIntent.putExtras(bundle);
        startActivityForResult(mIntent, PICK_CONTACT_REQUEST);
        return false;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private synchronized void setUpGoogleApiClient() {
        Log.d(TAG, "createGoogleApi()");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
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
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        callDriverRequestAccepted();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        NotificationUtils.clearNotifications(context);
    }

    @Override
    public void onPause() {
        super.onPause();
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
        mFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mgooleMap);
        mapView = mFragment.getView();

        mFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        mGoogleMap = googleMap;

      /*  googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.map_style));*/
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Showing / hiding your current location

        if (Build.VERSION.SDK_INT < 23) {
            mGoogleMap.setMyLocationEnabled(true);
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
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
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
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


        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        //or mGoogleMap.getUiSettings().setAllGesturesEnabled(true);

        mGoogleMap.setOnMarkerClickListener(this);

        mGoogleMap.setTrafficEnabled(false);
//        mGoogleMap.setInfoWindowAdapter(new InfoWindowCustom(getActivity()));


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
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMIT);

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMIT);
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
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }
        return true;
    }


    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
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
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;


//        writeActualLocation(location);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getActivity(),
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

                    callNearDriverAPI(currentLat, currentLong);
                    callTripRateAPI(statCode);

                } else {
                    Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_SHORT).show();
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

            jsonObject.put(WebFields.TRIP.PARAM_STATE, statCode);

            Applog.E("request Trip Price => " + jsonObject.toString());
//            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.TRIP.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    GetTripRate tripRateData = new Gson().fromJson(String.valueOf(response), GetTripRate.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
                        if (tripRateData.getError_code() == 0) {

                            base_fee = Float.parseFloat(tripRateData.getData().getBase_fee());
                            time_fee = Float.parseFloat(tripRateData.getData().getTime_fee());
                            mile_fee = Float.parseFloat(tripRateData.getData().getMile_fee());
                            over_mile_fee = Float.parseFloat(tripRateData.getData().getOvermile_fee());
                            //   CalculateMile(str,elapsedTime);


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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }


    private void createRoute(LatLng pickupPoint, LatLng dropPoint) {
        String url = getMapsApiDirectionsUrl(pickupPoint, dropPoint);
        ReadTask downloadTask = new ReadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private void startAnim() {
        if (mGoogleMap != null) {
            MapAnimator.getInstance().animateRoute(mGoogleMap, routeList);
        } else {
            Toast.makeText(getContext(), "Map not ready", Toast.LENGTH_LONG).show();
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
                String[] separated = duration.split(" ");
                duration1 = separated[0]; // this will contain "Fruit"

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
//            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            try {

                if (routes.size() > 0) {
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
                        polyLineOptions.width(6);
                        polyLineOptions.color(Color.BLACK);
                    }


                    mGoogleMap.addPolyline(polyLineOptions);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(pickupPoint);
                    builder.include(dropPoint);

                    LatLngBounds bounds = builder.build();

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int padding = (int) (width * 0.2);

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mGoogleMap.animateCamera(cameraUpdate);

                    dist = getDistance(pickupPoint.latitude, pickupPoint.longitude, dropPoint.latitude, dropPoint.longitude);
                    tripCost = CalculateMile(String.valueOf(dist), duration1);


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
                    txtEstMentCost.setText("US$" + df.format(tripCost));

                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(pickupPoint)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ellipse2))
                            .title("Current Location")
                            .snippet("Home"));

                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(dropPoint)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ellipse2))
                            .title("Drop Location")
                            .snippet("Driver"));
                } else {
                    Log.e("TAG", "No route found");
                }


                if (alUserDetails != null) {
                    for (GetUserData userData : alUserDetails) {
                        if (userData.getData().getCards().get(0).getCard_number() != null) {
                            byte[] data = Base64.decode(userData.getData().getCards().get(0).getCard_number(), Base64.DEFAULT);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                cardNo = new String(data, StandardCharsets.UTF_8);

//                                edtCardDetail.setSelected(false);
//                                edtCardDetail.setFocusable(false);
//                                edtCardDetail.setFocusableInTouchMode(false);
//                                String cn=cardNo.substring(11,15);
                                String s = cardNo;

                                String s4 = s.substring(12, 16);
                                String strCardNumber = "**** **** **** " + s4;
                                edtCardDetail.setText(strCardNumber);

                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

    private void callUserMeApi() {
        try {
            JSONObject jsonObject = new JSONObject();

            Applog.E("request: " + jsonObject.toString());
//            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.USER_ME.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());


//                   GetUserData.DataBean userDtl = new Gson().fromJson(String.valueOf(response), GetUserData.DataBean.class);
                    GetUserData userDetail = new Gson().fromJson(String.valueOf(response), GetUserData.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
                        alUserDetails = new ArrayList<>();
                        alVehicleDetail = new ArrayList<>();

                        if (userDetail.getError_code() == 0) {


                            alUserDetails.add(userDetail);

                            alVehicleDetail.addAll(userDetail.getData().getVehicles());

//                            setFirstTimeRegister();

//                            SessionManager.saveUserData(context, userDetails);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//
                            //getIs_agreed = 0 new user
                            //priyanka


                            //   Toast.makeText(context, strVehicalData, Toast.LENGTH_LONG).show();

                            setFirstTimeRegister();
                            setVehicleData();
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


    public LinearLayout getCostDesignLayout() {

        return llCostDesign;
    }

    public LinearLayout getDriverPersDetailsMainLayout() {

        return llDriverPersDetailsMain;
    }

    public RelativeLayout getOnTripLayout() {

        return rlOnTripUser;
    }
}
