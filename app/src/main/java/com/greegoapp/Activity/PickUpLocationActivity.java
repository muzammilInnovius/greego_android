package com.greegoapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.greegoapp.Adapter.PlaceAutocompleteAdapter;
import com.greegoapp.Fragment.MapHomeFragment;
import com.greegoapp.R;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.databinding.ActivityPickUpLocationBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PickUpLocationActivity extends AppCompatActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, OnMapReadyCallback {

    private static final String TAG = PickUpLocationActivity.class.getName();
    ActivityPickUpLocationBinding binding;
    Context context;
    private View snackBarView;
    Context mContext;
    private RecyclerView mRecyclerView;
    LinearLayoutManager llm;
    PlaceAutocompleteAdapter mAdapter;


    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-33.880490, 151.184363), new LatLng(-33.858754, 151.229596));

    EditText mEdtPickUpLocation, mEdtDetstinationLocation;
    ImageView mivBack, ivPickClear, ivDesClear;
    static int edtType;

    RelativeLayout rlSetLocationMap, rlSetCurrentLocation, rlEditextPickup;
    LinearLayout llLocationView;


    //
    String ssLocation, ddLocation, address;

    LatLng mDestinationLatLong, mSoucreLatLong;


    /*  Google Map  */
    private GoogleMap mGoogleMap;
    View mapView;
    MapFragment mFragment;
    GoogleApiClient mGoogleApiClient;

    public static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug

    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;

    private LatLng mLatLng;
    private Location lastLocation;
    Button btnSetLocation;
    public static boolean mapViewVisible = false;
    public static boolean passValue = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_up_location);

        context = PickUpLocationActivity.this;
        snackBarView = findViewById(android.R.id.content);


        initilizeMap();
        setUpGoogleApiClient();
        bindView();
        setListners();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passValue = true;
            String plat = extras.getString("pickupPointLatitud");
            String plong = extras.getString("pickupPointLongitude");
            String dlat = extras.getString("dropPointLatitude");
            String dlong = extras.getString("dropPointLongitude");
            String picLocAdd = extras.getString("picLocAddress");
            String desLocAdd = extras.getString("destLocAddress");

            mSoucreLatLong = new LatLng(Double.parseDouble(plat), Double.parseDouble(plong));
            mDestinationLatLong = new LatLng(Double.parseDouble(dlat), Double.parseDouble(dlong));
            ssLocation = getAddress(mSoucreLatLong.latitude, mSoucreLatLong.longitude);
            ddLocation = getAddress(mDestinationLatLong.latitude, mDestinationLatLong.longitude);
            mEdtPickUpLocation.setText(ssLocation);
            mEdtDetstinationLocation.setText(ddLocation);
        } else {
            passValue = false;
        }


        mEdtPickUpLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    edtType = 0;
                    ivPickClear.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    ivPickClear.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mEdtDetstinationLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    edtType = 1;
//                    llLocationView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ivDesClear.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    ivDesClear.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
//                    if (mSavedAdapter != null && mSavedAddressList.size() > 0) {
//                        mRecyclerView.setAdapter(mSavedAdapter);
//                    }
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEdtPickUpLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    edtType = 0;
                    passValue = false;
                    Log.e("TAG", "e1 focused");
                    llLocationView.setVisibility(View.VISIBLE);
                    ivPickClear.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        mAdapter.clearList();
                    }
                } else {
                    Log.e("TAG", "e1 not focused");
                }
            }
        });
        mEdtDetstinationLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    edtType = 1;
                    passValue = false;
                    llLocationView.setVisibility(View.VISIBLE);
                    ivDesClear.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        mAdapter.clearList();
                    }
                    Log.e("TAG", "e1 focused");
                } else {
                    Log.e("TAG", "e1 not focused");
                }
            }
        });


    }

    private void setListners() {
    }

    private void bindView() {
        mEdtPickUpLocation = binding.edtPickLocation;
        mEdtDetstinationLocation = binding.edtDesLocation;
        mRecyclerView = binding.listSearch;
        mivBack = binding.ivBackTop;
        ivPickClear = binding.clear;
        ivDesClear = binding.declear;
        edtType = 0;

        rlSetLocationMap = binding.rlSetLocationMap;
        llLocationView = binding.llLocationView;
        rlSetCurrentLocation = binding.rlCurrentLocation;
        btnSetLocation = binding.btnSetLocation;
        rlEditextPickup = binding.rlPick;


        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);


        mivBack.setOnClickListener(this);
        ivPickClear.setOnClickListener(this);
        ivDesClear.setOnClickListener(this);
        rlSetLocationMap.setOnClickListener(this);
        llLocationView.setOnClickListener(this);
        rlSetCurrentLocation.setOnClickListener(this);
        btnSetLocation.setOnClickListener(this);

        mEdtPickUpLocation.setOnClickListener(this);
        mEdtDetstinationLocation.setOnClickListener(this);


        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.layout_view_placesearch,
                mGoogleApiClient, BOUNDS_INDIA, null);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_backTop:
                finish();
                break;
            case R.id.clear:
                mEdtPickUpLocation.setText("");
                if (mAdapter != null) {
                    mAdapter.clearList();
                }
                llLocationView.setVisibility(View.VISIBLE);
                btnSetLocation.setVisibility(View.GONE);
                break;
            case R.id.declear:
                mEdtDetstinationLocation.setText("");
                if (mAdapter != null) {
                    mAdapter.clearList();
                }
                llLocationView.setVisibility(View.VISIBLE);
                btnSetLocation.setVisibility(View.GONE);
                break;

            case R.id.ll_LocationView:
                break;
            case R.id.rlSetLocationMap:
                KeyboardUtility.hideKeyboard(context, view);
                btnSetLocation.setVisibility(View.VISIBLE);
                llLocationView.setVisibility(View.GONE);
                mapViewVisible = true;

                break;
            case R.id.rlCurrentLocation:
                if (edtType == 0) {
                    mSoucreLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    ssLocation = address;
                    mEdtPickUpLocation.setText(ssLocation);
                } else {
                    mDestinationLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    ddLocation = address;
                    mEdtDetstinationLocation.setText(ddLocation);
                }
                break;
            case R.id.edt_pickLocation:
                edtType = 0;
                break;
            case R.id.edt_DesLocation:
                edtType = 1;
                break;

            case R.id.btnSetLocation:
                String sou = mEdtPickUpLocation.getText().toString();
                String des = mEdtDetstinationLocation.getText().toString();
                String picLocAddress = mEdtPickUpLocation.getText().toString();
                String destLocAddress = mEdtDetstinationLocation.getText().toString();

//                Date departure = new Date();  // This is the current time for testing purposes
//                String url = "http://maps.googleapis.com/maps/api/directions/xml?origin=" +
//                        mSoucreLatLong.latitude + "," + mSoucreLatLong.longitude + "&destination=" + mDestinationLatLong.latitude +
//                        "," + mDestinationLatLong.longitude + "&mode=transit&sensor=false&region=fr&departure_time=" +
//                        departure.getTime();
//
//                Applog.E("Duration Time==>" + departure.getTime() + " url=>" + url);

                if (sou != null && !sou.equals("null") && !sou.equals("") && des != null && !des.equals("null") && !des.equals("")) {

                    Intent data = new Intent();
                    data.putExtra("sourceLat", String.valueOf(mSoucreLatLong.latitude));
                    data.putExtra("sourceLng", String.valueOf(mSoucreLatLong.longitude));
                    data.putExtra("destinationLat", String.valueOf(mDestinationLatLong.latitude));
                    data.putExtra("destinationLng", String.valueOf(mDestinationLatLong.longitude));
                    data.putExtra("picLocAddress", picLocAddress);
                    data.putExtra("destLocAddress", destLocAddress);
                    data.putExtra("departure", "");
                    setResult(MapHomeFragment.PICK_CONTACT_REQUEST, data);
                    finish();
                } else {
                    llLocationView.setVisibility(View.VISIBLE);
                }

                break;

        }
    }


    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {
        if (mResultList != null) {
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);
                final String placeName = String.valueOf(mResultList.get(position).description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getCount() == 1) {
                            //Do the things here on Click.....
                            if (edtType == 0) {
                                mEdtPickUpLocation.setText(placeName);
                                mEdtPickUpLocation.clearFocus();
                                mEdtDetstinationLocation.requestFocus();
                                mSoucreLatLong = new LatLng(places.get(0).getLatLng().latitude, places.get(0).getLatLng().longitude);
                                if (mAdapter != null) {
                                    mAdapter.clearList();
                                }

                            } else {
                                mEdtDetstinationLocation.setText(placeName);
                                mEdtDetstinationLocation.clearFocus();
                                mDestinationLatLong = new LatLng(places.get(0).getLatLng().latitude, places.get(0).getLatLng().longitude);
                                if (mAdapter != null) {
                                    mAdapter.clearList();
                                }
                            }

                            String des = mEdtDetstinationLocation.getText().toString();
                            String picLocAddress = mEdtPickUpLocation.getText().toString();
                            String destLocAddress = mEdtDetstinationLocation.getText().toString();

//                            Date departure = new Date();  // This is the current time for testing purposes
//
//                            String url = "http://maps.googleapis.com/maps/api/directions/xml?origin=" +
//                                    mSoucreLatLong.latitude + "," + mSoucreLatLong.longitude + "&destination=" + mDestinationLatLong.latitude +
//                                    "," + mDestinationLatLong.longitude + "&mode=transit&sensor=false&region=fr&departure_time=" +
//                                    departure.getTime();
//
//                            Applog.E("Duration Time==>" + departure.getTime() + " url=>" + url);

                            if (des != null && !des.equals("null") && !des.equals("")) {
                                Intent data = new Intent();
                                data.putExtra("sourceLat", String.valueOf(mSoucreLatLong.latitude));
                                data.putExtra("sourceLng", String.valueOf(mSoucreLatLong.longitude));
                                data.putExtra("destinationLat", String.valueOf(mDestinationLatLong.latitude));
                                data.putExtra("destinationLng", String.valueOf(mDestinationLatLong.longitude));
                                data.putExtra("picLocAddress", picLocAddress);
                                data.putExtra("destLocAddress", destLocAddress);
                                data.putExtra("departure", "");
                                setResult(MapHomeFragment.PICK_CONTACT_REQUEST, data);
                                finish();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {

            }
        }
    }


    // Initialize GoogleMaps
    public void initilizeMap() {
        mFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mgooleMap);
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
        mGoogleMap.setTrafficEnabled(false);


        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mLatLng = cameraPosition.target;
                if (mapViewVisible == true) {
                    try {
                        KeyboardUtility.hideKeyboard(context, mapView);
                        if (edtType == 0) {
                            if (passValue == false) {
                                mEdtPickUpLocation.setText("Loading....");
                                ssLocation = getAddress(mLatLng.latitude, mLatLng.longitude);
                                mSoucreLatLong = new LatLng(mLatLng.latitude, mLatLng.longitude);
                                mEdtPickUpLocation.setText(ssLocation);
                                mEdtPickUpLocation.clearFocus();
                                ivPickClear.setVisibility(View.VISIBLE);
                            }
                        } else {

                            if (passValue == false) {
                                mEdtDetstinationLocation.setText("Loading....");
                                ddLocation = getAddress(mLatLng.latitude, mLatLng.longitude);
                                mDestinationLatLong = new LatLng(mLatLng.latitude, mLatLng.longitude);
                                mEdtDetstinationLocation.setText(ddLocation);
                                mEdtDetstinationLocation.clearFocus();
                                ivDesClear.setVisibility(View.VISIBLE);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private synchronized void setUpGoogleApiClient() {
        Log.d(TAG, "createGoogleApi()");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .enableAutoManage(this, 0 /* clientId */, this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
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


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
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

            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();

    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);

        if (passValue == false) {
            mEdtPickUpLocation.setText("Loading....");
            String add = getAddress(lastLocation.getLatitude(), lastLocation.getLongitude());
            mSoucreLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mEdtPickUpLocation.setText(add);
            ivPickClear.setVisibility(View.VISIBLE);
            mEdtPickUpLocation.clearFocus();
            mEdtPickUpLocation.setSelected(false);
            mEdtPickUpLocation.setFocusable(false);
            mEdtPickUpLocation.setFocusableInTouchMode(true);
            mEdtDetstinationLocation.clearFocus();
            mEdtDetstinationLocation.setSelected(false);
            mEdtDetstinationLocation.setFocusable(false);
            mEdtDetstinationLocation.setFocusableInTouchMode(true);
            address = getAddress(lastLocation.getLatitude(), lastLocation.getLongitude());
            mEdtDetstinationLocation.requestFocus();
            if (mAdapter != null) {
                mAdapter.clearList();
            }
        }

    }

    private void writeActualLocation(Location location) {
        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(new LatLng(location.getLatitude(), location.getLongitude())).bearing(360).
                zoom(15f).tilt(40).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

                HashMap itemAddress;
                ArrayList itemList = new ArrayList<HashMap<String, String>>();
                Log.d("Addresses", "" + "Start to print the ArrayList");
                for (int i = 0; i < addresses.size(); i++) {
                    itemAddress = new HashMap<String, String>();
                    Address address = addresses.get(i);
                    String addressline = "Addresses from getAddressLine(): ";
                    for (int n = 0; n <= address.getMaxAddressLineIndex(); n++) {
                        addressline += " index n: " + n + ": " + address.getAddressLine(n) + ", ";
                    }
                    Log.d(TAG, "Addresses: " + addressline);
                    Log.d(TAG, "Addresses getAdminArea()" + address.getAdminArea());
                    Log.d(TAG, "Addresses getCountryCode()" + address.getCountryCode());
                    Log.d(TAG, "Addresses getCountryName()" + address.getCountryName());
                    Log.d(TAG, "Addresses getFeatureName()" + address.getFeatureName());
                    Log.d(TAG, "Addresses getLocality()" + address.getLocality());
                    Log.d(TAG, "Addresses getPostalCode()" + address.getPostalCode());
                    Log.d(TAG, "" + address.getPremises());
                    Log.d(TAG, "Addresses getSubAdminArea()" + address.getSubAdminArea());
                    Log.d(TAG, "" + address.getSubLocality());
                    Log.d(TAG, "" + address.getSubThoroughfare());
                    Log.d(TAG, "Addresses getThoroughfare()" + address.getThoroughfare());
                }


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
        } catch (Exception e) {
            e.printStackTrace();
        }


        return result.toString();
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

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
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

}
