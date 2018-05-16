package com.greegoapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.greegoapp.FCM.NotificationUtils;
import com.greegoapp.R;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.databinding.ActivityTripHistoryDetailBinding;
import com.greegoapp.polilineAnimator.MapHttpConnection;
import com.greegoapp.polilineAnimator.PathJSONParser;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TripHistoryDetailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = "TripHistoryDetailActivity";
    ActivityTripHistoryDetailBinding binding;
    private View snackBarView;

    TextView tvTripTravelTime, tvDrPromoCode, tvTripTotalCost, tvStartTime, tvStartPlace, tvEndTime, tvEndPlace,
            tvTripGreegoTriptime, tvTripGreegoTripAmount, tvPromotionAmount, tvTotalAmount;

    String strTripTravelTime, strDrPromoCode, strTripTotalCost, strStartTime, strFromAdd, strToAdd, strEndTime,
            strTripGreegoTriptime, strTripGreegoTripAmount, strPromotionAmount, strTotalAmount, tripTotalTime, strDriverPic, strFromLat, strFromLng, strToLat, strToLng;
    ImageView ivDriverPic;
    Button btnGetHelp;
    Context context;
    ImageButton ibback;

    GoogleMap mGoogleMap;
    View mapView;
    MapFragment mFragment;
    private GoogleApiClient mGoogleApiClient;
    private static final long MOVE_ANIMATION_DURATION = 1000;
    public static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1001;
    private final int FASTEST_INTERVAL = 900;
    private final int UPDATE_INTERVAL = 901;

    LatLng latLng;
    private List<LatLng> routeList;
    private LatLng userlatLng, dropPoint;
    String areaName, sourceLatitude, sourceLongitude;
    private LocationRequest locationRequest;
    private Location lastLocation;

    String duration, duration1;
    private Marker marker;
    int mapZoomLevel;
    Double dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_history_detail);

        snackBarView = findViewById(android.R.id.content);
        context = TripHistoryDetailActivity.this;

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


        strDriverPic = getIntent().getStringExtra("drPic");
        strDrPromoCode = getIntent().getStringExtra("promocode");
        strTripTravelTime = getIntent().getStringExtra("totalTime");
        strTripTotalCost = getIntent().getStringExtra("totalCost");

        strFromLat = getIntent().getStringExtra("fromLat");
        strFromLng = getIntent().getStringExtra("fromLnd");
        strToLat = getIntent().getStringExtra("ToLat");
        strToLng = getIntent().getStringExtra("ToLng");

        strFromAdd = getIntent().getStringExtra("fromAdd");
        strToAdd = getIntent().getStringExtra("toAdd");
        strStartTime = getIntent().getStringExtra("startTime");
        tripTotalTime = getIntent().getStringExtra("tripTotalTime");

        bindViews();
        setListner();
        setTripData();
    }

    private void setTripData() {

//        Travel Time
        String date = strTripTravelTime;
        date = date.substring(0, 16);
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatedTripTravelTime = (String) android.text.format.DateFormat.format("MMM dd,yyyy - hh:mm aa", date1);


        //Start Time and End Time
        String startTimeDate = strTripTravelTime;
        startTimeDate = startTimeDate.substring(11, 16);
//        Date startTimeDate1 = null;
//        try {
//            startTimeDate1 = new SimpleDateFormat("hh:mm").parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        String formatedStartTimeDate = (String) android.text.format.DateFormat.format("hh:mm aa", date1);


        tvTripTravelTime.setText(formatedTripTravelTime);
        tvDrPromoCode.setText(strDrPromoCode);
        tvTripTotalCost.setText("$" + strTripTotalCost);
        tvStartTime.setText(formatedStartTimeDate);
        tvStartPlace.setText(strFromAdd);
        tvEndTime.setText(formatedStartTimeDate);
        tvEndPlace.setText(strToAdd);
        tvTripGreegoTriptime.setText(tripTotalTime +"min");
        tvTripGreegoTripAmount.setText("$" + strTripTotalCost);
        tvPromotionAmount.setText("$0.0");
        tvTotalAmount.setText("$" + strTripTotalCost);

        String profilePicUrl = "http://kroslinkstech.in/greego/storage/app/" + strDriverPic;

        if (profilePicUrl != null) {
            Glide.clear(ivDriverPic);
            Glide.with(context)
                    .load(profilePicUrl)
                    .centerCrop()
                    .signature(new StringSignature(UUID.randomUUID().toString()))
                    .crossFade().skipMemoryCache(true)
                    .into(ivDriverPic);

        } else {
            ivDriverPic.setImageResource(R.mipmap.ic_driver_profile);
        }


        double toLat = Double.parseDouble(strToLat);
        double tolong = Double.parseDouble(strToLng);
        double fromLat = Double.parseDouble(strFromLat);
        double fromlong = Double.parseDouble(strFromLng);

//        createMarker(fromLat, fromlong, "User", "Show");
        userlatLng = new LatLng(toLat, tolong);
        dropPoint = new LatLng(fromLat, fromlong);


        createRoute(userlatLng, dropPoint);
//        createMarker(fromLat, fromlong, "Start", "Show");
    }

    protected void createMarker(double latitude, double longitude, String title, String snippet) {
        try {
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .title(title)
                    .snippet(snippet).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ellipse_1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setListner() {
        btnGetHelp.setOnClickListener(this);
        ibback.setOnClickListener(this);
        mFragment.getMapAsync(TripHistoryDetailActivity.this);
    }

    private void bindViews() {
        ibback = binding.ibBack;
        tvTripTravelTime = binding.tvTripTravelTime;
        tvDrPromoCode = binding.tvDrPromoCode;
        tvTripTotalCost = binding.tvTripTotalCost;
        tvStartTime = binding.tvStartTime;
        tvStartPlace = binding.tvStartPlace;
        tvEndTime = binding.tvEndTime;
        tvEndPlace = binding.tvEndPlace;
        tvTripGreegoTriptime = binding.tvTripGreegoTriptime;
        tvTripGreegoTripAmount = binding.tvTripGreegoTripAmount;
        tvPromotionAmount = binding.tvPromotionAmount;
        tvTotalAmount = binding.tvTotalAmount;
        ivDriverPic = binding.ivDriverPic;
        btnGetHelp = binding.btnGetHelp;
        mFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.tripMap);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                finish();
                break;

            case R.id.btnGetHelp:
                break;
        }
    }


    private void createRoute(LatLng pickupPoint, LatLng dropPoint) {
        String url = getMapsApiDirectionsUrl(pickupPoint, dropPoint);
        ReadTask downloadTask = new ReadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
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
                    builder.include(userlatLng);
                    builder.include(dropPoint);

                    LatLngBounds bounds = builder.build();

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int padding = (int) (width * 0.2);

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mGoogleMap.animateCamera(cameraUpdate);

//                    dist = getDistance(userlatLng.latitude, userlatLng.longitude, dropPoint.latitude, dropPoint.longitude);
//
//
//                    if (dist > 2 && dist <= 5) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
//                        mapZoomLevel = 12;
//                    } else if (dist > 5 && dist <= 10) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
//                        mapZoomLevel = 11;
//                    } else if (dist > 10 && dist <= 20) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
//                        mapZoomLevel = 11;
//                    } else if (dist > 20 && dist <= 40) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
//                        mapZoomLevel = 10;
//                    } else if (dist > 40 && dist < 100) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(9.0f));
//                        mapZoomLevel = 9;
//                    } else if (dist > 100 && dist < 200) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
//                        mapZoomLevel = 8;
//                    } else if (dist > 200 && dist < 400) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(7.0f));
//                        mapZoomLevel = 7;
//                    } else if (dist > 400 && dist < 700) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f));
//                        mapZoomLevel = 7;
//                    } else if (dist > 700 && dist < 1000) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f));
//                        mapZoomLevel = 6;
//                    } else if (dist > 1000) {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(4.0f));
//                        mapZoomLevel = 5;
//                    } else {
//                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
//                        mapZoomLevel = 14;
//                    }


                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(userlatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start_pin))
                            .title("Current Location")
                            .snippet("Home"));

                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(dropPoint)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start_pin))
                            .title("Drop point")
                            .snippet("Driver"));
                } else {
                    Log.e("TAG", "No route found");
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


    // Initialize GoogleMaps
    public void initilizeMap() {
        mFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.tripMap);
        mapView = mFragment.getView();

        mFragment.getMapAsync(this);
    }

    @SuppressLint({"MissingPermission", "LongLogTag"})
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
//        mGoogleMap.setInfoWindowAdapter(new InfoWindowCustom(TripHistoryDetailActivity.this));


        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                latLng = cameraPosition.target;
                try {
//                    getAddress(latLng.latitude, latLng.longitude);
//                    Log.e("Changing address", getAddress(latLng.latitude, latLng.longitude));
//                    Log.e("Latitude", latLng.latitude + "");
//                    Log.e("Longitude", latLng.longitude + "");
//                    sourceLatitude = latLng.latitude + "";
//                    sourceLongitude = latLng.longitude + "";


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });


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


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @SuppressLint("LongLogTag")
    private synchronized void setUpGoogleApiClient() {
        Log.d(TAG, "createGoogleApi()");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
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


    // Start location Updates
    @SuppressLint({"MissingPermission", "LongLogTag"})
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

    @SuppressLint("LongLogTag")
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;


//        writeActualLocation(location);


    }


    @SuppressLint("LongLogTag")
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
                    if (ContextCompat.checkSelfPermission(TripHistoryDetailActivity.this,
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
    @SuppressLint("LongLogTag")
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
        mGoogleApiClient.connect();
    }

    @SuppressLint("LongLogTag")
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
            ActivityCompat.requestPermissions(TripHistoryDetailActivity.this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }
        return true;
    }

    // Get last known location
    @SuppressLint("LongLogTag")
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
//                startLocationUpdates();


            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();

    }


    @SuppressLint("LongLogTag")
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("LongLogTag")
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                TripHistoryDetailActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCOUNTS
        );
    }


}
