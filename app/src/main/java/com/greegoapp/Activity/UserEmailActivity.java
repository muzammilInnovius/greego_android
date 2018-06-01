package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.UserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityUserEmailBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.greegoapp.Activity.SettingActivity.SETTING_EMAIL_UPDATE;

public class UserEmailActivity extends AppCompatActivity implements View.OnClickListener {


    TextView tvSendConfEmail;
    EditText edtTvUserEmail;
    ImageView imgVwCancel;
    ImageButton ibBack;
    ActivityUserEmailBinding binding;
    private View snackBarView;
    Context context;
    String emailId,userName,mobileNo,lastName;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_email);

        snackBarView = findViewById(android.R.id.content);
        context = UserEmailActivity.this;

        emailId = getIntent().getStringExtra("email");
        userName = getIntent().getStringExtra("username");
        mobileNo = getIntent().getStringExtra("mobileNo");
        lastName = getIntent().getStringExtra("lastName");

        bindViews();
        setListners();

    }

    private void setListners() {
        ibBack.setOnClickListener(this);
        imgVwCancel.setOnClickListener(this);
//        btnUpdate.setOnClickListener(this);
        tvSendConfEmail.setOnClickListener(this);
    }

    private void bindViews() {
        tvSendConfEmail = binding.tvSendConfEmail;
        edtTvUserEmail = binding.edtTvUserEmail;
        imgVwCancel = binding.imgVwCancel;
        ibBack = binding.ibBack;
        btnUpdate = binding.btnUpdate;

        edtTvUserEmail.setText(emailId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgVwCancel:
                edtTvUserEmail.setText("");
                break;

            case R.id.ibBack:
                Intent data = new Intent();
                String userName = edtTvUserEmail.getText().toString();
                data.putExtra("email", userName);
                setResult(SETTING_EMAIL_UPDATE, data);
                finish();
                break;

            case R.id.tvSendConfEmail:
                if (ConnectivityDetector.isConnectingToInternet(context)) {
                    callSendEmailVarificationAPI();
                } else {
                    SnackBar.showInternetError(context, snackBarView);
                }

                break;

            case R.id.btnUpdate:
//                if (ConnectivityDetector.isConnectingToInternet(context)) {
//                    callEmailUpdateAPI();
//                } else {
//                    SnackBar.showInternetError(context, snackBarView);
//                }

                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent data = new Intent();
        String userName = edtTvUserEmail.getText().toString();
        data.putExtra("email", userName);
        setResult(SETTING_EMAIL_UPDATE, data);
        finish();
    }

    private void callSendEmailVarificationAPI() {
        try {
//            emailId = edtTvUserEmail.getText().toString();
            JSONObject jsonObject = new JSONObject();

            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.EMAIL_VERIFY.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

//                    please verify it by clicking the activation link that has been send to your email
                    SnackBar.showSuccess(context, snackBarView, "Verify your register email address.");

//                    UserData userDetails = new Gson().fromJson(String.valueOf(response), UserData.class);
//                    try {
                        MyProgressDialog.hideProgressDialog();
////
//                        if (userDetails.getError_code() == 0) {
//
//                            SnackBar.showSuccess(context, snackBarView, "Email update successfully.");
//                        } else {
//                            MyProgressDialog.hideProgressDialog();
//                            SnackBar.showError(context, snackBarView, response.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
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

    private void callEmailUpdateAPI() {
        try {
            emailId = edtTvUserEmail.getText().toString();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(WebFields.SIGN_UP.PARAM_FIRST_NAME, "");
            jsonObject.put(WebFields.SIGN_UP.PARAM_LAST_NAME, "");
            jsonObject.put(WebFields.SIGN_UP.PARAM_EMAIL, emailId);
            jsonObject.put(WebFields.SIGN_UP.PARAM_CITY, "");
            jsonObject.put(WebFields.SIGN_UP.PARAM_PRO_PIC, "");

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

                            SnackBar.showSuccess(context, snackBarView, "Email update successfully.");
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
}
