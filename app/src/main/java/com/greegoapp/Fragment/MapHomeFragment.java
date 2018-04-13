package com.greegoapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.greegoapp.Activity.HomeActivity;
import com.greegoapp.Activity.PickUpLocationActivity;
import com.greegoapp.AppController.AppController;
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
import com.greegoapp.databinding.FragmentMapHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class MapHomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {
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


    private Location mylocation;
    Geocoder geocoder;
    List<Address> addresses;
    GetUserData userDetails;

    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

    public static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    LocationManager locationManager;
    boolean GpsStatus;

    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int REQ_PERMISSION = 999;
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;


    private Location lastLocation;
    LatLng latLng;
    TextView txtAddress;


    //Mujju
    RelativeLayout rlVwUpdateMain, rlUpdate, rlUpdateData;
    TextView txtVwProfile, txtVwVehicleName, txtVwpayment, txtVwUpdate, txtVwCount;
    RelativeLayout rlProfile, rlVehicleNo, rlPaymentNo;
    CallFragmentInterface callMyFragment;
    BackPressedFragment backPressed;

    public MapHomeFragment() {
        // Required empty public constructor
    }


    public static MapHomeFragment newInstance(String param1, String param2) {
        MapHomeFragment fragment = new MapHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        callUserMeApi();
        setListners();

        if (ConnectivityDetector.isConnectingToInternet(context)) {
            if (Build.VERSION.SDK_INT < 23) {
                //Do not need to check the permission

                /*  Initialize Map  */
                initilizeMap();

                // create GoogleApiClient
//                createGoogleApi();

            } else {
                if (checkAndRequestPermissions()) {
                    //If you have already permitted the permission

                    //        initialize GoogleMaps
                    initilizeMap();
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

    private void setListners() {
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
        rlPaymentNo.setOnClickListener(this);;
    }


    private void bindViews() {
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
        rlVwUpdateMain.setVisibility(View.GONE);
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
    public void onClick(View view) {
        Fragment fragment = null;

        switch (view.getId()) {

            case R.id.txtAddress:
                Intent mIntent = new Intent(context, PickUpLocationActivity.class);
                startActivity(mIntent);
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

                fragment = UserProfileFragment.newInstance("", "");
                break;
            case R.id.txtVwVehicleName:
                fragment = AddEditVehicleFragment.newInstance("", "");
//                fragment = new V();
                break;
            case R.id.txtVwpayment:
                fragment = AddPaymentMethodFragment.newInstance("", "");
                break;

            case R.id.rlProfile:

                fragment = UserProfileFragment.newInstance("", "");
                break;
            case R.id.rlVehicleNo:
                fragment = AddEditVehicleFragment.newInstance("", "");
//                fragment = new V();
                break;
            case R.id.rlPaymentNo:
                fragment = AddPaymentMethodFragment.newInstance("", "");
                break;

        }

        if (fragment != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.containerBody, fragment);
            transaction.addToBackStack(null);
            transaction.commit();


        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private synchronized void setUpGoogleApiClient() {
        Log.d(TAG, "createGoogleApi()");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpGoogleApiClient();
        mGoogleApiClient.connect();
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

    // Callback called when Map is ready
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        mGoogleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Showing / hiding your current location
        if (Build.VERSION.SDK_INT < 23) {
            mGoogleMap.setMyLocationEnabled(true);
        } else {
            if (checkAndRequestPermissions()) {
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
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

        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        //or mGoogleMap.getUiSettings().setAllGesturesEnabled(true);

        mGoogleMap.setTrafficEnabled(false);

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                latLng = cameraPosition.target;
                mGoogleMap.clear();
                try {
                    getAddress(latLng.latitude, latLng.longitude);
                    Log.e("Changing address", getAddress(latLng.latitude, latLng.longitude));
                    Log.e("Latitude", latLng.latitude + "");
                    Log.e("Longitude", latLng.longitude + "");
                    String lat = latLng.latitude + "";
                    String lng = latLng.longitude + "";
                    String location = getAddress(latLng.latitude, latLng.longitude);
                    txtAddress.setText(location);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });


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


    private void CheckGpsStatus() {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (GpsStatus == true) {
//            Toast.makeText(this, "Location Services Is Enabled", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Location Services Is Disabled", Toast.LENGTH_SHORT).show();
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Pls On GPS")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }


    // Start location Updates
    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        if (checkPermission()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, (LocationListener) this);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;
//        writeActualLocation(location);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    initilizeMap();
//                    createGoogleApi();
                    getLastKnownLocation();

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
                zoom(12f).tilt(40).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        CurrentLocationMaker(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    private void CurrentLocationMaker(LatLng latLng) {
        Log.i(TAG, "CurrentmarkerLocation(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
//        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher);
//        Bitmap b = bitmapdraw.getBitmap();
//        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title)
                .draggable(false)
                .snippet("Current Address").anchor(0.5f, 0.5f).flat(true);


        if (mGoogleMap != null) {
            mGoogleMap.addMarker(markerOptions);
        }
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                if (checkPermission()) {
                    lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (lastLocation != null) {
                        Log.i(TAG, "LasKnown location. " +
                                "Long: " + lastLocation.getLongitude() +
                                " | Lat: " + lastLocation.getLatitude());
                        writeLastLocation();
                        startLocationUpdates();
                    } else {
                        Log.w(TAG, "No location retrieved yet");
                        startLocationUpdates();
                    }
                } else askPermission();
            }

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
        if (addresses.size() > 0) {
            System.out.println("size====" + addresses.size());
            Address address = addresses.get(0);

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

        return result.toString();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callMyFragment = null;
        backPressed = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
//        startLocationUpdates();
    }

    //Mujju

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

                    userDetails = new Gson().fromJson(String.valueOf(response), GetUserData.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {

                            Applog.E("UserUpdate==>Dg==>" + userDetails);
                            if (userDetails.getData().getProfile_pic() == null) {
                                rlVwUpdateMain.setVisibility(View.VISIBLE);
                                rlUpdateData.setVisibility(View.VISIBLE);
                                txtVwProfile.setTextColor(getResources().getColor(R.color.hint_color));
//                                txtVwCount.setText("2");
                            } else {
                                txtVwProfile.setTextColor(getResources().getColor(R.color.app_bg));
                            }

                            if (userDetails.getData().getCards() == null) {
                                rlVwUpdateMain.setVisibility(View.VISIBLE);
                                rlUpdateData.setVisibility(View.VISIBLE);
                                txtVwVehicleName.setTextColor(getResources().getColor(R.color.hint_color));
                            }else {
                                txtVwVehicleName.setTextColor(getResources().getColor(R.color.app_bg));
                            }

                             if (userDetails.getData().getVehicles() == null) {
                                rlVwUpdateMain.setVisibility(View.VISIBLE);
                                rlUpdateData.setVisibility(View.VISIBLE);
                                txtVwpayment.setTextColor(getResources().getColor(R.color.hint_color));
                            } else {
                                 txtVwpayment.setTextColor(getResources().getColor(R.color.app_bg));
                                rlVwUpdateMain.setVisibility(View.GONE);
                                rlUpdateData.setVisibility(View.GONE);

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
                public Map<String, String> getHeaders() throws AuthFailureError {
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
