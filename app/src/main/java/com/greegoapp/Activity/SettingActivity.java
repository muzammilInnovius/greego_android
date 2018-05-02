package com.greegoapp.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    TextView tvUserName, tvUserEmail, tvUserPhone,tvJoinDate;
    GetUserData userDetails;
    ImageView ivProPic;
    public static final int SETTING_PROFILE_UPDATE = 1001;
    String userName,profilePic;

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
        tvJoinDate=binding.tvJoinDate;
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
//                Fragment fragment = new UserProfileFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.containerBody, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                break;
            case R.id.rlEmail:
                in = new Intent(SettingActivity.this, UserEmailActivity.class);
               startActivity(in);
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
                userName = data.getStringExtra("name");
                profilePic = data.getStringExtra("profilePic");

                Glide.clear(ivProPic);
                Glide.with(getApplicationContext())
                        .load(profilePic)
                        .centerCrop()
                        .signature(new StringSignature(UUID.randomUUID().toString()))
                        .crossFade().skipMemoryCache(true)
                        .into(ivProPic);

//                ivProPic.setImageURI(Uri.parse(profilePic));
                tvUserName.setText(userName);
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
//
                        if (userDetails.getError_code() == 0) {

                            Applog.E("UserUpdate==>Dg==>" + userDetails);
                            String date = userDetails.getData().getCreated_at();
                            date=date.substring(0,10);
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                            String formatedstring = (String) android.text.format.DateFormat.format("dd-MMM-yyyy",date1);
                            formatedstring=formatedstring.substring(3);
                            formatedstring=formatedstring.replace("-"," ");
                            String userName = userDetails.getData().getName();
                            String emailId = userDetails.getData().getEmail();
                            String mobileNO = userDetails.getData().getContact_number();

                            profilePic =userDetails.getData().getProfile_pic();
//                            ivProPic.setImageURI(Uri.parse(profilePic));

                            profilePic =  userDetails.getData().getProfile_pic();
                            if (profilePic != null) {
                                Glide.clear(ivProPic);
                                Glide.with(getApplicationContext())
                                        .load(profilePic)
                                        .centerCrop()
                                        .signature(new StringSignature(UUID.randomUUID().toString()))
                                        .crossFade().skipMemoryCache(true)
                                        .into(ivProPic);

                            } else {
                                ivProPic.setImageResource(R.mipmap.ic_user_profile);
                            }

                            tvUserName.setText(userName);
                            tvUserEmail.setText(emailId);
                            tvUserPhone.setText(mobileNO);
                            tvJoinDate.setText(formatedstring);
//                            SessionManager.saveUserData(context, userDetails);
//                            SnackBar.showSuccess(context, snackBarView, response.getString("message"));
//
                            //getIs_agreed = 0 new user

//
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
}
