package com.greegoapp.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.chaos.view.PinView;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.FCM.Config;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.Model.Login;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityDigitCodeBinding;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigitCodeActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityDigitCodeBinding binding;
    ImageButton ibBack;
    TextView tvResend;
    ImageButton ibFinish;
    PinView pinVwOtpCode;

    Context context;
    private View snackBarView;
    String strOtpCode;
    TextView txtTimer, tvCntWthDriver;
    CountDownTimer mCountDownTimer;
    Dialog dialogotp;
    int sendOtp;
    SmsVerifyCatcher smsVerifyCatcher;
    public static String deviceId = "";
    String registerFCMKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_digit_code);
        context = DigitCodeActivity.this;
        snackBarView = findViewById(android.R.id.content);

        registerFCMKey = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0).getString("regId", "def");

        bindView();
        setListners();
        timer();
        tvCntWthDriver.setText(SessionManager.getMobileNo(context));

        sendOtp = SessionManager.getOTP(context);

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                pinVwOtpCode.setText(code);//set code in edit text

                if (ConnectivityDetector
                        .isConnectingToInternet(context)) {

                    callDigitCodeAPI();

                } else {
                    SnackBar.showInternetError(context, snackBarView);
                }
            }
        });
    }

    private void setListners() {
        ibFinish.setOnClickListener(this);
        tvResend.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        pinVwOtpCode.setOnClickListener(this);
    }

    private void bindView() {
        ibBack = binding.ibBack;
        pinVwOtpCode = binding.pinVwDigitCode;
        tvResend = binding.tvResend;
        ibFinish = binding.ibFinish;
        tvCntWthDriver = binding.tvCntWthDriver;
        txtTimer = binding.txtTimer;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                Intent iback = new Intent(context, SignUpMobileNumberActivity.class);
                iback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iback);
                overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);
                finish();
                break;
            case R.id.tvResend:
                cancelTimer();
                timer();

                if (registerFCMKey != null) {
                    if (ConnectivityDetector.isConnectingToInternet(context)) {
                        callMobileNumberAPI();
                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }

                }else {
                    SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                }

                pinVwOtpCode.setText("");
                KeyboardUtility.hideKeyboard(context, view);
                break;

            case R.id.ibFinish:

                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callDigitCodeAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }


                break;

            case R.id.pinVwDigitCode:
                KeyboardUtility.showKeyboard(context, pinVwOtpCode);
                break;
        }
    }


    private void callMobileNumberAPI() {
        try {
//            deviceId = DeviceId.getDeviceId(this);
//            Applog.E("DeviceId==>" + deviceId);

            JSONObject jsonObject = new JSONObject();
//            String token = SessionManager.getToken(context);
//            Applog.E("Token" + token);

            jsonObject.put(WebFields.SIGN_IN.PARAM_CONTACT_NO, SessionManager.getMobileNo(context));
            jsonObject.put(WebFields.SIGN_IN.PARAM_IS_PHONE_NO, 0);
//            if (registerFCMKey != null) {
                jsonObject.put(WebFields.SIGN_IN.PARAM_DEVICE_ID, registerFCMKey);
//            } else {
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

                            sendOtp = SessionManager.getOTP(context);
                            Applog.E("Resend Otp ==> " + sendOtp);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//                            backPressed.onBackPressed(getContext());


//                            Intent in = new Intent(context, DigitCodeActivity.class);
//                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(in);
//                            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);

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

    public void timer() {
        txtTimer.setText("");

        mCountDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                txtTimer.setText(String.format("%02d", minutes) + " : " + String.format("%02d", seconds));

            }

            @Override
            public void onFinish() {
                SnackBar.showValidationError(context, snackBarView, getString(R.string.otp_resend_empty));
                String mobileNo = SessionManager.getMobileNo(context);

                sendOtp = 0;

                txtTimer.setText("00" + " : " + "00");
                tvResend.setEnabled(true);
                tvResend.setTextColor(Color.BLUE);
                pinVwOtpCode.setText("");
//                isCounterRunning = false;
//                dialogotp.setCanceledOnTouchOutside(true);
//                dialogotp.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                    @Override
//                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        return false;
//                    }
//                });
//                dialogotp.dismiss();
                pinVwOtpCode.setText("");
            }
        }.start();
    }

    //cancel timer
    void cancelTimer() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
    }

    private boolean isValid() {
        strOtpCode = pinVwOtpCode.getText().toString();
        if (strOtpCode.length() < 6) {
            pinVwOtpCode.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_digit_code));
            return false;
        }
        return true;
    }

    private void callDigitCodeAPI() {

        String mobileNo = SessionManager.getMobileNo(context);
        strOtpCode = pinVwOtpCode.getText().toString();

//        if (ConnectivityDetector.isConnectingToInternet(context)) {
//            callUserMeApi();
//        } else {
//            SnackBar.showInternetError(context, snackBarView);
//        }



        if (sendOtp != 0) {
            String newOtp = "" + sendOtp;
            if (strOtpCode.matches(newOtp)) {
                callUserMeApi();

            } else {
                SnackBar.showValidationError(context, snackBarView, getString(R.string.otp_resend_wng));
            }
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

                            Applog.E("UserDetails==>Dg==>" + userDetails);
//                            SessionManager.saveUserData(context, userDetails);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//
                            //getIs_agreed = 0 new user
                            if (userDetails.getData().getIs_agreed() == 0) {
                                Intent in = new Intent(context, SignUpEmailActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                            } else {
                                SessionManager.setIsUserLoggedin(context, true);
                                Intent in = new Intent(context, HomeActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                            }


                            SessionManager.saveUserDetails(context, userDetails);

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


    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }


}
