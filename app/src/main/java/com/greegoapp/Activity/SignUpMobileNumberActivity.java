package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.Login;
import com.greegoapp.R;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivitySignUpMobileNumberBinding;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class SignUpMobileNumberActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySignUpMobileNumberBinding binding;
    EditText edtTvMobileNo;
    ImageButton ibBack;
    ImageButton ibFinish;
    Context context;
    private View snackBarView;
    String strMobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_mobile_number);
        context = SignUpMobileNumberActivity.this;
        snackBarView = findViewById(android.R.id.content);

        bindView();
        setListners();
    }

    private void setListners() {
        ibBack.setOnClickListener(this);
        ibFinish.setOnClickListener(this);
        edtTvMobileNo.setOnClickListener(this);
    }

    private void bindView() {
        edtTvMobileNo = binding.edtTvMobileNo;
        ibBack = binding.ibBack;
        ibFinish = binding.ibFinish;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                Intent in = new Intent(context, MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);

                break;
            case R.id.ibFinish:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callMobileNumberAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;
        }
    }

    private void callMobileNumberAPI() {
        try {
            JSONObject jsonObject = new JSONObject();
            String token = SessionManager.getToken(context);

            Applog.E("Token" + token);

            jsonObject.put(WebFields.SIGN_IN.PARAM_CONTACT_NO, strMobileNo);
            jsonObject.put(WebFields.SIGN_IN.PARAM_IS_PHONE_NO, 0);

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SIGN_IN.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    Login loginData = new Gson().fromJson(String.valueOf(response), Login.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
                        if (loginData.getError_code() == 0) {

                            Applog.E("Contact No" + loginData.getData().getContact_number());
                            SessionManager.saveMobileNo(context, loginData);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//                            backPressed.onBackPressed(getContext());


                            Intent in = new Intent(context, DigitCodeActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);

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


    private boolean isValid() {
        strMobileNo = "+91" + edtTvMobileNo.getText().toString();

        if (strMobileNo.isEmpty()) {
            edtTvMobileNo.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_mobile_no_empty));
            return false;
        } else if (strMobileNo.length() < 10) {
            edtTvMobileNo.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.mobile_no_length));
            return false;
        }
        return true;
    }
}
