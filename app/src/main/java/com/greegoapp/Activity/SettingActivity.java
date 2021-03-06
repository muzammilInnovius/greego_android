package com.greegoapp.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivitySettingBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySettingBinding binding;
    Context context;
    private String mParam1;
    private String mParam2;
    ImageButton ibback;
    RelativeLayout rl_name, rl_phone, rl_email, rl_btnlogout;
    View snackBarView;
    TextView tvUserName, tvUserEmail, tvUserPhone, tvJoinDate, tvEmailVerify;
    GetUserData userDetails;
    ImageView ivProPic;
    public static final int SETTING_PROFILE_UPDATE = 1001;
    public static final int SETTING_EMAIL_UPDATE = 1011;
    String userName, profilePic, emailId, mobileNo, lastName;
    int emailVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        snackBarView = findViewById(android.R.id.content);
        context = SettingActivity.this;


        bindViews();

        setListner();


        if (ConnectivityDetector.isConnectingToInternet(context)) {
            callUserMeApi();
//            CheckGpsStatus();
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void setListner() {
        ibback.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_email.setOnClickListener(this);
        rl_btnlogout.setOnClickListener(this);
    }

    private void bindViews() {
        ibback = binding.ibBack;
        rl_name = binding.rlName;
        rl_phone = binding.rlPhone;
        rl_email = binding.rlEmail;
        rl_btnlogout = binding.rlLogout;
        tvUserName = binding.tvUserName;
        tvUserEmail = binding.tvUserEmail;
        tvUserPhone = binding.tvUserPhone;
        ivProPic = binding.ivProPic;
        tvJoinDate = binding.tvJoinDate;
        tvEmailVerify = binding.tvEmailVerify;


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collaps);
//        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
//        collapsingToolbar.setTitle("Setting");
    }

    Intent in;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                KeyboardUtility.hideKeyboard(context, view);
                finish();
                break;
            case R.id.rlName:
                in = new Intent(SettingActivity.this, UserProfileActivity.class);
                startActivityForResult(in, SETTING_PROFILE_UPDATE);
                break;
            case R.id.rlEmail:
                in = new Intent(SettingActivity.this, UserEmailActivity.class);
                in.putExtra("email", emailId);
                in.putExtra("username", userName);
                in.putExtra("mobileNo", mobileNo);
                in.putExtra("lastName", lastName);
                startActivityForResult(in, SETTING_EMAIL_UPDATE);
                break;
            case R.id.rlLogout:
                showCallbacksLogout("Are you sure you want to Logout?");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case SETTING_PROFILE_UPDATE:
                if (data != null) {
//                callUserMeApi();
                    userName = data.getStringExtra("name");
                    profilePic = data.getStringExtra("profilePic");

                    if (profilePic != null) {
                        Glide.clear(ivProPic);
                        Glide.with(getApplicationContext())
                                .load(profilePic)
                                .centerCrop()
                                .signature(new StringSignature(UUID.randomUUID().toString()))
                                .crossFade().skipMemoryCache(true)
                                .into(ivProPic);
                    } else {
                        ivProPic.setImageResource(R.mipmap.ic_place_holder);
                    }

                    tvUserName.setText(userName);
                }
                break;

            case SETTING_EMAIL_UPDATE:
                if (data != null) {
                    emailId = data.getStringExtra("email");
                    tvUserEmail.setText(emailId);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void showCallbacksLogout(String strLogOut) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("Greego").setMessage(strLogOut);


            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        SessionManager.clearAppSession(context);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
            });

            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
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

                    userDetails = new Gson().fromJson(String.valueOf(response), GetUserData.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
                        if (userDetails.getError_code() == 0) {

                            Applog.E("UserUpdate==>Dg==>" + userDetails);
                            String date = userDetails.getData().getCreated_at();
                            date = date.substring(0, 10);
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                            String formatedstring = (String) android.text.format.DateFormat.format("dd-MMM-yyyy", date1);
                            formatedstring = formatedstring.substring(3);
                            formatedstring = formatedstring.replace("-", " ");

                            userName = userDetails.getData().getName();
                            emailId = userDetails.getData().getEmail();
                            mobileNo = userDetails.getData().getContact_number();

                            profilePic = userDetails.getData().getProfile_pic();
                            emailVerify = userDetails.getData().getEmail_verified();

                            if (emailVerify == 1){
                                tvEmailVerify.setText("Verified");
                            }else {
                                tvEmailVerify.setText("Unverified");
                            }

                            profilePic = userDetails.getData().getProfile_pic();
                            if (profilePic != null) {
                                Glide.clear(ivProPic);
                                Glide.with(getApplicationContext())
                                        .load(profilePic)
                                        .centerCrop()
                                        .signature(new StringSignature(UUID.randomUUID().toString()))
                                        .crossFade().skipMemoryCache(true)
                                        .into(ivProPic);

                            } else {
                                ivProPic.setImageResource(R.mipmap.ic_place_holder);
                            }

                            tvUserName.setText(userName);
                            tvUserEmail.setText(emailId);
                            tvUserPhone.setText(mobileNo);
                            tvJoinDate.setText(formatedstring);

                        } else {
                            MyProgressDialog.hideProgressDialog();
                            SnackBar.showError(context, snackBarView, response.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
