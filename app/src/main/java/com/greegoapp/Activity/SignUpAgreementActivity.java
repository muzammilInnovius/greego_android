package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.Model.TermsCondition;
import com.greegoapp.Model.UserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivitySignUpAgreementBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpAgreementActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySignUpAgreementBinding binding;
    Context context;

    NestedScrollView scrollView;
    CheckBox cbChecked;
    ImageButton ibBack;
    ImageButton ibFinish;
    private View snackBarView;
    RelativeLayout rlAgreement;
    String strEmail, strFName, strLName;
    boolean isChecked;
    TextView tvAgreementDetail;
//    ScrollView scrool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_agreement);

        strEmail = getIntent().getStringExtra("email");
        strFName = getIntent().getStringExtra("fName");
        strLName = getIntent().getStringExtra("lName");

        context = SignUpAgreementActivity.this;
        snackBarView = findViewById(android.R.id.content);
        bindView();
        setListners();
        callTermsCondiAPI();
    }

    private void setListners() {

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    rlAgreement.setVisibility(View.VISIBLE);
                    //.hide();
                } else {
                    rlAgreement.setVisibility(View.INVISIBLE
                    );
                    // fab.show();
                }
            }
        });
        ibFinish.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        scrollView.setOnClickListener(this);
        tvAgreementDetail.setOnClickListener(this);
    }

    private void bindView() {
        scrollView = binding.scrollView;
        cbChecked = binding.cbAgreement;
        ibBack = binding.ibBack;
        rlAgreement = binding.rlAgreement;
        ibFinish = binding.ibFinish;
        tvAgreementDetail = binding.tvAgreementDetail;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                KeyboardUtility.hideKeyboard(context, view);
                Intent in = new Intent(context, SignUpUserNameActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);
                finish();
                break;
            case R.id.ibFinish:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {
                        isChecked = true;
                        callAcceptAgreementAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;
//            case R.id.scroll:
//                rlAgreement.setVisibility(View.VISIBLE);
//                break;
            case R.id.tvAgreementDetail:
                rlAgreement.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void callTermsCondiAPI() {
        try {

            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    WebFields.BASE_URL + WebFields.TERMS_CONDITION.MODE, null, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {

                    TermsCondition userDetail = new Gson().fromJson(String.valueOf(response), TermsCondition.class);
                    try {
                        if (userDetail.getError_code() == 0) {
                            Applog.E("success: " + response.toString());
                            MyProgressDialog.hideProgressDialog();
                            tvAgreementDetail.setText(Html.fromHtml(userDetail.getData().getTerms_conditions().trim()));

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


    private void callAcceptAgreementAPI() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(WebFields.SIGN_UP.PARAM_FIRST_NAME, strFName);
            jsonObject.put(WebFields.SIGN_UP.PARAM_LAST_NAME, strLName);
            jsonObject.put(WebFields.SIGN_UP.PARAM_EMAIL, strEmail);
            jsonObject.put(WebFields.SIGN_UP.PARAM_CITY, "");
            jsonObject.put(WebFields.SIGN_UP.PARAM_PRO_PIC, "");

            if (isChecked) {
                jsonObject.put(WebFields.SIGN_UP.PARAM_IS_AGGREED, 1);
            } else {
                jsonObject.put(WebFields.SIGN_UP.PARAM_IS_AGGREED, 0);
            }

            Applog.E("request DigitCode=> " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SIGN_UP.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    UserData userDetails = new Gson().fromJson(String.valueOf(response), UserData.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {

                            Applog.E("UserDetails" + userDetails);
                            callUserMeApi();
                            SessionManager.setIsUserLoggedin(context, true);
//                            Intent in = new Intent(context, HomeActivity.class);
//                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(in);
//                            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
//                            finish();
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

                    GetUserData userDetails = new Gson().fromJson(String.valueOf(response), GetUserData.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {

                            Applog.E("UserUpdate==>Dg==>" + userDetails);
//                            SessionManager.saveUserData(context, userDetails);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//
                            //getIs_agreed = 0 new user
                            Intent in = new Intent(context, HomeActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
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

    public boolean isValid() {
        //   boolean cbchecked=false;
        if (!cbChecked.isChecked()) {
            isChecked = false;
            cbChecked.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.no_checked_agreement));
            return false;
        }
        return true;
    }
}
