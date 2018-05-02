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
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityTripHistoryBinding;

import org.json.JSONObject;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_history);

        snackBarView = findViewById(android.R.id.content);
        context = TripHistoryActivity.this;

        alTripHistoryList = new ArrayList<>();

        bindViews();
        setListner();

        callUserTripHistoryAPI();

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
                            setRecyclerView();
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

                Bundle bundle=new Bundle();
                bundle.putParcelable("tripHistoryDetails", alTripHistoryList.get(position));
                in.putExtras(bundle);

                startActivity(in);

            }
        };

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
