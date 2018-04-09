package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.greegoapp.R;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivitySignUpMobileNumberBinding;

import org.json.JSONObject;

public class SignUpMobileNumberActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySignUpMobileNumberBinding binding;
    EditText edtTvMobileNo;
    ImageButton ibBack;
    ImageButton ibFinish;
    Context context;
    private View snackBarView;

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
            jsonObject.put(WebFields.USERID, SessionManager.getUserID(context));
            jsonObject.put(WebFields.EDIT_PROFILE.REQ_FIRSTNAME, strFirstName);
            jsonObject.put(WebFields.EDIT_PROFILE.REQ_LASTNAME, strLastName);
            jsonObject.put(WebFields.EDIT_PROFILE.REQ_PROFILEPIC, strProPicBase64);
            jsonObject.put(WebFields.EDIT_PROFILE.REQ_CONTACTNUMBER, strContactNumber);
            jsonObject.put(WebFields.EDIT_PROFILE.REQ_NOTES, strNotesPersnlMsg);
            //Please check bank id(Int values) as per yr requirment
            jsonObject.put(WebFields.EDIT_PROFILE.REQ_BANKID, 0);


            JSONObject physicalAddress = new JSONObject();
            physicalAddress.put(WebFields.EDIT_PROFILE.REQ_ADDRESS, address);
            physicalAddress.put(WebFields.EDIT_PROFILE.REQ_CITY, city);
            physicalAddress.put(WebFields.EDIT_PROFILE.REQ_STATE, state);
            physicalAddress.put(WebFields.EDIT_PROFILE.REQ_COUNTRY, GlobalValues.COUNTRY);
            physicalAddress.put(WebFields.EDIT_PROFILE.RESP_PINCODE, zipCode);

            jsonObject.put(WebFields.EDIT_PROFILE.REQ_PHYSICALADDRESS, physicalAddress);

            Applog.e("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.EDIT_PROFILE.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.e("success: " + response.toString());

                    try {
                        if (response.getInt("status") == 1) {
                            MyProgressDialog.hideProgressDialog();
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
                            callMyFragment.callProfileFragmentMethod();
                            backPressed.onBackPressed(getContext());

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
                    Applog.e("Error: " + error.getMessage());

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
//        Intent in = new Intent(context, DigitCodeActivity.class);
//        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(in);
//        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }

    private boolean isValid() {
        String strMobileNo = edtTvMobileNo.getText().toString();

        if (strMobileNo.isEmpty()) {
            edtTvMobileNo.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_mobile_no_empty));
            return false;
        } else if (strMobileNo.length() < 8 || strMobileNo.length() > 10) {
            edtTvMobileNo.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.mobile_no_length));
            return false;
        }
        return true;
    }
}
