package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityTripCompleteChargePayBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class TripCompleteChargePayActivity extends AppCompatActivity implements View.OnClickListener {


    ActivityTripCompleteChargePayBinding binding;
    private View snackBarView;
    Context context;
    ImageButton ibBack;
    TextView tvTotalCost, tvTripDriverName, tvUserFrmAddress;
    RatingBar rtgBarDriver;
    ImageView ivDriverProPic;
    Button btnSubmit;
    String totalCost, driverName, driverPic, fromAddress,tripId;
    float userRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_complete_charge_pay);

        snackBarView = findViewById(android.R.id.content);
        context = TripCompleteChargePayActivity.this;

        totalCost = getIntent().getStringExtra("totalCost");
        driverName = getIntent().getStringExtra("driverName");
        driverPic = getIntent().getStringExtra("driverPic");
        fromAddress = getIntent().getStringExtra("fromAddress");
        tripId = getIntent().getStringExtra("tripId");
        Applog.E("Trip id ===> " +tripId);

        bindViews();
        setListners();

        tvTotalCost.setText(totalCost);
        tvTripDriverName.setText(driverName + "?");
        Applog.E("tripId==>" + tripId);
        tvUserFrmAddress.setText(fromAddress);

//        driverPic = "http://kroslinkstech.in/greego/storage/app/profile_pic/9Ad7LDsTSFdWUno5kQ6h3ZQHrzohKJ9zpSpODQpW.png";

        if (driverPic != null) {
            Glide.clear(ivDriverProPic);
            Glide.with(context)
                    .load(driverPic)
                    .centerCrop()
                    .signature(new StringSignature(UUID.randomUUID().toString()))
                    .crossFade().skipMemoryCache(true)
                    .into(ivDriverProPic);

        } else {
            ivDriverProPic.setImageResource(R.mipmap.ic_driver_profile);
        }

    }

    private void setListners() {
        btnSubmit.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }

    private void bindViews() {
        ibBack = binding.ibBack;
        btnSubmit = binding.btnSubmit;
        tvTotalCost = binding.tvTotalCost;
        tvTripDriverName = binding.tvTripDriverName;
        tvUserFrmAddress = binding.tvUserFrmAddress;
        rtgBarDriver = binding.rtgBarDriver;
        ivDriverProPic = binding.ivDriverProPic;

        rtgBarDriver.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                userRating = rating;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (ConnectivityDetector.isConnectingToInternet(context)) {
                    callDriverRatingAPI();
                } else {
                    SnackBar.showInternetError(context, snackBarView);
                }

                break;

            case R.id.ibBack:
                Intent in = new Intent(context, HomeActivity.class);
                startActivity(in);
                finish();
                break;
        }
    }

    private void callDriverRatingAPI() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(WebFields.DRIVER_RATING.PARAM_TRIP_ID, tripId);
            jsonObject.put(WebFields.DRIVER_RATING.PARAM_RATING, userRating);

            Applog.E("request Driver rating =>" + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.DRIVER_RATING.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    MyProgressDialog.hideProgressDialog();

                    Intent in = new Intent(context, HomeActivity.class);
                    startActivity(in);
                    finish();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
