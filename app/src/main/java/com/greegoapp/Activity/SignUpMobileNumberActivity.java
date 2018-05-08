package com.greegoapp.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.Adapter.CustomAdapter;
import com.greegoapp.AppController.AppController;
import com.greegoapp.FCM.Config;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.Login;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivitySignUpMobileNumberBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpMobileNumberActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ActivitySignUpMobileNumberBinding binding;
    EditText edtTvMobileNo;
    ImageButton ibBack;
    ImageButton ibFinish;
    Context context;
    private View snackBarView;
    String strMobileNo;
    public static String deviceId = "";
    Spinner spinnerCountry;
    TextView tvCountryCode;
    ImageView imgVwCntyLogo;
    String[] countryName = {"India", "USA", "North Koria", "South Coria"};
    String[] countryCode = {"+91", "+1", "+850", "+82"};
    int[] countryFlag = {R.mipmap.ic_indian_flag, R.mipmap.american_flag_large, R.mipmap.ic_nc_flag, R.mipmap.ic_sc_flag};

    String registerFCMKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_mobile_number);
        context = SignUpMobileNumberActivity.this;
        snackBarView = findViewById(android.R.id.content);

        registerFCMKey = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0).getString("regId", "");

        bindView();
        setListners();
        setSpinnerData();
    }

    private void setSpinnerData() {
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), countryFlag, countryCode, countryName);
        spinnerCountry.setAdapter(customAdapter);
    }


    private void setListners() {
        ibBack.setOnClickListener(this);
        ibFinish.setOnClickListener(this);
        edtTvMobileNo.setOnClickListener(this);
        imgVwCntyLogo.setOnClickListener(this);
        spinnerCountry.setOnItemSelectedListener(this);
    }

    private void bindView() {
        edtTvMobileNo = binding.edtTvMobileNo;
        ibBack = binding.ibBack;
        ibFinish = binding.ibFinish;
        spinnerCountry = binding.spinnerCountry;
        tvCountryCode = binding.tvCountryCode;
        imgVwCntyLogo = binding.imgVwCntyLogo;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {



            case R.id.ibBack:
                KeyboardUtility.hideKeyboard(context, view);
                Intent in = new Intent(context, MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);
                finish();

//                Intent output = new Intent();
//                setResult(RESULT_OK, output);
//                finish();
                break;
            case R.id.ibFinish:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {
                        if (registerFCMKey != null) {
                            callMobileNumberAPI();
                        }else {
                            SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                        }

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;

            case R.id.imgVwCntyLogo:
                spinnerCountry.setVisibility(View.VISIBLE);
                spinnerCountry.performClick();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    String uniqueId;

    private void callMobileNumberAPI() {
        try {

//            deviceId = DeviceId.getDeviceId(this);
//            Applog.E("DeviceId==>" + deviceId + "==UUID==>" + uniqueId);
            JSONObject jsonObject = new JSONObject();
//            String token = SessionManager.getToken(context);
//            Applog.E("Token" + token);

            jsonObject.put(WebFields.SIGN_IN.PARAM_CONTACT_NO, strMobileNo);
            jsonObject.put(WebFields.SIGN_IN.PARAM_IS_PHONE_NO, 0);
//            if (registerFCMKey != null) {
                jsonObject.put(WebFields.SIGN_IN.PARAM_DEVICE_ID, registerFCMKey);
//            }else {
//                SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
//            }

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SIGN_IN.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    try {
                        Login loginData = new Gson().fromJson(String.valueOf(response), Login.class);

                        MyProgressDialog.hideProgressDialog();
                        if (loginData.getError_code() == 0) {

                            Applog.E("Contact No" + loginData.getData().getContact_number());
                            SessionManager.saveUserData(context, loginData);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//                            backPressed.onBackPressed(getContext());

                            Intent in = new Intent(context, DigitCodeActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);

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

        //   Toast.makeText(context,tvCountryCode.getText().toString(),Toast.LENGTH_LONG).show();
        strMobileNo = tvCountryCode.getText().toString() + edtTvMobileNo.getText().toString();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // do something on back.
        return;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        imgVwCntyLogo.setImageResource(countryFlag[position]);
        tvCountryCode.setText(countryCode[position]);
        spinnerCountry.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
