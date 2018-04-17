package com.greegoapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.Activity.HomeActivity;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.FragmentSettingBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingFragment extends Fragment implements View.OnClickListener {
    FragmentSettingBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View snackBarView;
    Context context;
    private String mParam1;
    private String mParam2;
    ImageButton ibback;
    RelativeLayout rl_name, rl_phone, rl_email, rl_btnlogout;
    private BackPressedFragment backPressed;
    private OnFragmentInteractionListener mListener;
    CallFragmentInterface callMyFragment;
    TextView tvUserName,tvUserEmail;
    GetUserData userDetails;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CallFragmentInterface) {
            callMyFragment = (CallFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CallFragmentInterface");
        }


        if (context instanceof BackPressedFragment) {
            backPressed = (BackPressedFragment) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CallFragmentInterface");
        }
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);

        View view = binding.getRoot();
        snackBarView = getActivity().findViewById(android.R.id.content);
        context = getActivity();

        bindViews();

        setListner();

        if (ConnectivityDetector.isConnectingToInternet(context)) {
            callUserMeApi();
//            CheckGpsStatus();
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }

        return view;
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
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        callMyFragment = null;
        backPressed = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                backPressed.onBackPressed(getActivity());
                break;
            case R.id.rlName:
                Fragment fragment = new UserProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerBody, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.rlEmail:
                Toast.makeText(getContext(), "email layout click", Toast.LENGTH_LONG).show();
                break;
            case R.id.rlLogout:
                Toast.makeText(getContext(), "logout layout click", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
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

                            String userName = userDetails.getData().getName();
                            String emailId = userDetails.getData().getEmail();
                            tvUserName.setText(userName);
                            tvUserEmail.setText(emailId);
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
