package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.UserData;
import com.greegoapp.Model.VerifyOtp;
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

public class SignUpAgreementActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySignUpAgreementBinding binding;
    Context context;

    NestedScrollView scrollView;
    CheckBox cbChecked;
    ImageButton ibBack;
    ImageButton ibFinish;
    private View snackBarView;
    RelativeLayout relativeLayout;
    String strEmail, strFName, strLName;
    boolean isChecked;

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

    }

    private void setListners() {

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    //.hide();
                } else {
                    relativeLayout.setVisibility(View.INVISIBLE
                    );
                    // fab.show();
                }
            }
        });
        ibFinish.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }

    private void bindView() {
        scrollView = binding.svAgreement;
        cbChecked = binding.cbAgreement;
        ibBack = binding.ibBack;
        relativeLayout = binding.rlAgreement;
        ibFinish = binding.ibFinish;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                Intent in = new Intent(context, SignUpUserNameActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);

                break;
            case R.id.ibFinish:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callAcceptAgreementAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;
        }
    }

    private void callAcceptAgreementAPI() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(WebFields.SIGN_UP.PARAM_ACCEPT, "application/json");
            Applog.E("Token===>" + SessionManager.getToken(context));
            jsonObject.put(WebFields.SIGN_UP.PARAM_AUTHOTIZATION, GlobalValues.BEARER_TOKEN + SessionManager.getToken(context));

            jsonObject.put(WebFields.SIGN_UP.PARAM_FIRST_NAME, strFName);
            jsonObject.put(WebFields.SIGN_UP.PARAM_LAST_NAME, strLName);
            jsonObject.put(WebFields.SIGN_UP.PARAM_EMAIL, strEmail);
            jsonObject.put(WebFields.SIGN_UP.PARAM_CITY, "");
            jsonObject.put(WebFields.SIGN_UP.PARAM_PRO_PIC, "");
            jsonObject.put(WebFields.SIGN_UP.PARAM_IS_AGGREED, isChecked);


            Applog.E("request DigitCode=> " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SIGN_UP.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    UserData userDetails= new Gson().fromJson(String.valueOf(response), UserData.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {

                            Applog.E("UserDetails" + userDetails);
//                            SessionManager.saveUserData(context, userDetails);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//

                            Intent in = new Intent(context, HomeActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);

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
            });
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
            isChecked = true;
            cbChecked.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.no_checked_agreement));
            return false;
        }
        return true;
    }
}
