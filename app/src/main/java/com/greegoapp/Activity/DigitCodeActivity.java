package com.greegoapp.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chaos.view.PinView;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.VerifyOtp;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityDigitCodeBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class DigitCodeActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityDigitCodeBinding binding;
    ImageButton ibBack;
    TextView tvResend;
    ImageButton ibFinish;
    PinView pinVwOtpCode;

    Context context;
    private View snackBarView;
    String strOtpCode;
    TextView txtTimer;
    CountDownTimer mCountDownTimer;
    Dialog dialogotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_digit_code);
        context = DigitCodeActivity.this;
        snackBarView = findViewById(android.R.id.content);

        bindView();
        setListners();
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
       /* edtTvDigit1 = binding.edtTvDigit1;
        edtTvDigit2 = binding.edtTvDigit2;
        edtTvDigit3 = binding.edtTvDigit3;
        edtTvDigit4 = binding.edtTvDigit4;
        edtTvDigit5 = binding.edtTvDigit5;
        edtTvDigit6 = binding.edtTvDigit6;*/

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                Intent iback = new Intent(context, SignUpMobileNumberActivity.class);
                iback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iback);
                overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);
                break;
            case R.id.tvResend:
                timer();
                pinVwOtpCode.setText("");
                break;
            /*    edtTvDigit1.setText("");
                edtTvDigit2.setText("");
                edtTvDigit3.setText("");
                edtTvDigit4.setText("");
                edtTvDigit5.setText("");
                edtTvDigit6.setText("");*/

            case R.id.ibFinish:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callDigitCodeAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }


                //  SnackBar.showValidationError(context,view,"In Progress");
                break;
        }
    }


    public void timer() {
        mCountDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                txtTimer.setText(String.format("%02d", minutes) + " : " + String.format("%02d", seconds));

            }

            @Override
            public void onFinish() {
                txtTimer.setText("00" + " : " + "00");
                tvResend.setEnabled(true);
                tvResend.setTextColor(Color.BLUE);
                pinVwOtpCode.setText("");
//                isCounterRunning = false;
                dialogotp.setCanceledOnTouchOutside(true);
                dialogotp.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return false;
                    }
                });
                dialogotp.dismiss();
                pinVwOtpCode.setText("");
            }
        }.start();
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

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.VERIFY_OTP.PARAM_CONTACT_NO, SessionManager.getMobileNo(context));
            jsonObject.put(WebFields.VERIFY_OTP.PARAM_OTP, strOtpCode);

            Applog.E("request DigitCode=> " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.VERIFY_OTP.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    VerifyOtp otpData = new Gson().fromJson(String.valueOf(response), VerifyOtp.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (otpData.getError_code() == 0) {

                            Applog.E("Contact No" + otpData);
                            SessionManager.saveUserData(context, otpData);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//
                            if (otpData.getData().getNewX() == 0) {
                                Intent in = new Intent(context, HomeActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                            }else {
                                Intent in = new Intent(context, SignUpEmailActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                            }


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
}
