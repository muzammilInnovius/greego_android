package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.Adapter.TripHistoryAdapter;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.TripHistoryModel;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityTripHistoryBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TripHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityTripHistoryBinding binding;
    Context context;
    RecyclerView rvTripHistory;
    ImageButton ibback;
    ArrayList<TripHistoryModel.DataBean> alTripHistoryList;
    TripHistoryAdapter mAdapter;
    View snackBarView;
    int tripHistoryPosition;
    TextView tvNoDataFaund;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_history);

        snackBarView = findViewById(android.R.id.content);
        context = TripHistoryActivity.this;

        alTripHistoryList = new ArrayList<>();

        bindViews();
        setListner();

        if (ConnectivityDetector.isConnectingToInternet(context)) {
            callUserTripHistoryAPI();
        } else {
            SnackBar.showInternetError(context, snackBarView);
        }

    }

    private void callUserTripHistoryAPI() {
        try {
            JSONObject jsonObject = new JSONObject();

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.GET_TRIP_HISTORY.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    TripHistoryModel userDetails = new Gson().fromJson(String.valueOf(response), TripHistoryModel.class);
                    alTripHistoryList = new ArrayList<>();
                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {

                            alTripHistoryList.addAll(userDetails.getData());
                            Applog.E("TripHistory size==>" + alTripHistoryList.size());

                            if (alTripHistoryList.size() != 0) {
                                setRecyclerView();
                                tvNoDataFaund.setVisibility(View.GONE);
                                rvTripHistory.setVisibility(View.VISIBLE);

                            } else {
                                tvNoDataFaund.setVisibility(View.VISIBLE);
                                rvTripHistory.setVisibility(View.GONE);
                            }
//                            adapter.notifyDataSetChanged();
//
                        } else {
                            MyProgressDialog.hideProgressDialog();
                            SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
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

    private void setRecyclerView() {
        RecyclerViewItemClickListener mListener = new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                tripHistoryPosition = position;
                Intent in = new Intent(context, TripHistoryDetailActivity.class);
                in.putExtra("drPic", alTripHistoryList.get(tripHistoryPosition).getProfile_pic());
                in.putExtra("promocode", alTripHistoryList.get(tripHistoryPosition).getPromocode());


                String totalTime = alTripHistoryList.get(tripHistoryPosition).getCreated_at();
                float totalCost = alTripHistoryList.get(tripHistoryPosition).getTotal_estimated_trip_cost();


                double fromLat = alTripHistoryList.get(tripHistoryPosition).getFrom_lat();
                double fromLong = alTripHistoryList.get(tripHistoryPosition).getFrom_lng();

                double toLat = alTripHistoryList.get(tripHistoryPosition).getTo_lat();
                double toLong = alTripHistoryList.get(tripHistoryPosition).getTo_lng();

                String tripTotalTime = "" + alTripHistoryList.get(tripHistoryPosition).getTotal_estimated_travel_time();

                in.putExtra("totalTime", totalTime);
                in.putExtra("totalCost", String.valueOf(totalCost));

                in.putExtra("fromLat", String.valueOf(fromLat));
                in.putExtra("fromLnd", String.valueOf(fromLong));

                in.putExtra("ToLat", String.valueOf(toLat));
                in.putExtra("ToLng", String.valueOf(toLong));

                in.putExtra("fromAdd", alTripHistoryList.get(tripHistoryPosition).getFrom_address());
                in.putExtra("toAdd", alTripHistoryList.get(tripHistoryPosition).getTo_address());

                in.putExtra("startTime", alTripHistoryList.get(tripHistoryPosition).getCreated_at());
                in.putExtra("tripTotalTime", tripTotalTime);

                startActivity(in);

            }
        };

        Collections.reverse(alTripHistoryList);
        Applog.E("TripHistory size after set==>" + alTripHistoryList.size());
        mAdapter = new TripHistoryAdapter(context, alTripHistoryList, mListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rvTripHistory.setLayoutManager(mLayoutManager);
        rvTripHistory.setItemAnimator(new DefaultItemAnimator());
        rvTripHistory.setAdapter(mAdapter);
    }

    private void setListner() {
        ibback.setOnClickListener(this);
    }

    private void bindViews() {
        rvTripHistory = binding.rvTripHistory;
        ibback = binding.ibBack;
        tvNoDataFaund = binding.tvNoDataFaund;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                finish();
                break;

        }
    }
}
