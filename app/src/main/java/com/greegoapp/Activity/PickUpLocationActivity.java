package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.greegoapp.Adapter.PlaceAutocompleteAdapter;
import com.greegoapp.R;
import com.greegoapp.databinding.ActivityPickUpLocationBinding;

import java.util.ArrayList;

public class PickUpLocationActivity extends AppCompatActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface
        ,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    ActivityPickUpLocationBinding binding;
    Context context;
    private View snackBarView;


    //
    final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    Context mContext;
    GoogleApiClient mGoogleApiClient;

    private RecyclerView mRecyclerView;
    LinearLayoutManager llm;
    PlaceAutocompleteAdapter mAdapter;


    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    EditText mEdtPickUpLocation;
    ImageView  mivBack;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_pick_up_location);

        context = PickUpLocationActivity.this;
        snackBarView = findViewById(android.R.id.content);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        bindView();
        setListners();



    }

    private void setListners() {
    }

    private void bindView() {
        mEdtPickUpLocation = (EditText) findViewById(R.id.edt_pickLocation);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);




        mivBack = (ImageView) findViewById(R.id.iv_backTop);
        mivBack.setOnClickListener(this);


        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.layout_view_placesearch,
                mGoogleApiClient, BOUNDS_INDIA, null);
        mRecyclerView.setAdapter(mAdapter);


        mEdtPickUpLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    /*if (mSavedAdapter != null && mSavedAddressList.size() > 0) {
                        mRecyclerView.setAdapter(mSavedAdapter);
                    }*/
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_backTop:
                finish();
                break;
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle selectAddress = data.getExtras();
                    String lat = selectAddress.getString("lat");
                    String lon = selectAddress.getString("lng");
                    String address = selectAddress.getString("address");
                    String addressFor = selectAddress.getString("addressFor");

                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
//                    Log.i(TAG, status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {

                }
                break;
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {
        if(mResultList!=null){
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
                        if(places.getCount()==1){
                            //Do the things here on Click.....
                            Intent data = new Intent();
                            data.putExtra("lat",String.valueOf(places.get(0).getLatLng().latitude));
                            data.putExtra("lng", String.valueOf(places.get(0).getLatLng().longitude));
                            data.putExtra("address",placeName);
                            setResult(PickUpLocationActivity.RESULT_OK, data);
                            mEdtPickUpLocation.setText(placeName);
//                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (Exception e){

            }
        }
    }


}
