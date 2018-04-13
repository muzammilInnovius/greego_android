package com.greegoapp.Fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.Model.CardData;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.Model.UserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.FragmentAddPaymentMethodBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPaymentMethodFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    FragmentAddPaymentMethodBinding binding;
    private View snackBarView;
    Context context;
    DatePicker datePicker;


    EditText edtTvExpDate, edtTvCvv, edtTvZipCode, edtTvCardNumber;
    Button save;
    int mYear, mMonth, mDay;
    ImageButton ibback;
    Button btnSavePaymentMethod;
    // Required empty public constructor
    ArrayList<GetUserData.DataBean.CardsBean> cardData;
    TextView tvEditPaymentTitle;
    ImageButton ibBack;
    private BackPressedFragment backPressed;
    private CallFragmentInterface callMyFragment;

    public static AddPaymentMethodFragment newInstance(String param1, String param2) {
        AddPaymentMethodFragment fragment = new AddPaymentMethodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_payment_method, container, false);

        View view = binding.getRoot();
        snackBarView = getActivity().findViewById(android.R.id.content);
        context = getActivity();
        cardData = new ArrayList<>();

        bindViews();
        setListner();
        callUserMeApi();

        return view;
    }


    private void setListner() {
        ibBack.setOnClickListener(this);
        btnSavePaymentMethod.setOnClickListener(this);
        edtTvExpDate.setOnClickListener(this);
    }

    private void bindViews() {
        edtTvCardNumber = binding.edtTvCardNumber;
        edtTvExpDate = binding.edtTvExpDate;
        edtTvCvv = binding.edtTvCvv;
        edtTvZipCode = binding.edtTvZipCode;
        btnSavePaymentMethod = binding.btnSavePaymentMethod;
        tvEditPaymentTitle = binding.tvEditPaymentTitle;
        ibBack =  binding.ibBack;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                backPressed.onBackPressed(getActivity());
//                Fragment fragment = new PaymentFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.containerBody, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                break;
            case R.id.btnSavePaymentMethod:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callSavePaymentMethodAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;
            case R.id.edtTvExpDate:
                getDate();
                break;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callMyFragment = null;
        backPressed = null;
    }


    private void getDate() {

        try {
            // Get Current Date
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;

                            edtTvExpDate.setText(new StringBuilder().append(mMonth + 1).append("/").append(year).append(" "));
                            //strFrom = txtVwWrkgSncFrom.getText().toString();
                        }
                    }, mDay, mMonth, mYear);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callSavePaymentMethodAPI() {

        try {
            JSONObject jsonObject = new JSONObject();

            String strCardNo = edtTvCardNumber.getText().toString();
            String strExpDate = edtTvExpDate.getText().toString();
            String strzipCode = edtTvZipCode.getText().toString();


            jsonObject.put(WebFields.UPDATE_CARD.PARAM_CARD_NUMBER, strCardNo);
            jsonObject.put(WebFields.UPDATE_CARD.PARAM_EXP_MONTH_YEAR, strExpDate);
            jsonObject.put(WebFields.UPDATE_CARD.PARAM_ZIPCODE, strzipCode);

            Applog.E("request UpdateCard=> " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.UPDATE_CARD.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    try {
                        CardData cardDetails = new Gson().fromJson(String.valueOf(response), CardData.class);
                        MyProgressDialog.hideProgressDialog();
                        if (cardDetails.getError_code() == 0) {
                            SessionManager.setIsUserLoggedin(context, true);
                            Applog.E("UserDetails" + cardDetails);

//                            callUserMeApi();
                            SnackBar.showSuccess(context, snackBarView, "Add card successfully.");
                        } else {
                            MyProgressDialog.hideProgressDialog();
                            SnackBar.showError(context, snackBarView, response.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    CardData cardDetails = new Gson().fromJson(String.valueOf(response), CardData.class);
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
                public Map<String, String> getHeaders() throws AuthFailureError {
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

//        Fragment fragment = new PaymentFragment();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.containerBody, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    private boolean isValid() {
        String cardno = edtTvCardNumber.getText().toString();
        String date = edtTvExpDate.getText().toString();
        String cvvno = edtTvCvv.getText().toString();
        String zipcode = edtTvZipCode.getText().toString();

        if (cardno.length() < 12) {
            edtTvCardNumber.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_card_no));
            return false;
        } else if (date.isEmpty()) {
            edtTvExpDate.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_exp_date));
            return false;
        } else if (cvvno.isEmpty()) {
            edtTvCvv.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_cvv_no));
            return false;
        } else if (cvvno.length() < 3) {
            edtTvCvv.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_cvv_no));
            return false;
        } else if (zipcode.isEmpty()) {
            edtTvZipCode.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_zipcode));
            return false;
        } else if (zipcode.length() < 6 || zipcode.length() > 6) {
            edtTvZipCode.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_zipcode));
            return false;
        }
        return true;
    }

    String cardNo;

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
                            cardData.addAll(userDetails.getData().getCards());

                            if (userDetails.getData() == null) {
                                tvEditPaymentTitle.setText(getResources().getString(R.string.new_payment));
                            } else {
                                tvEditPaymentTitle.setText(getResources().getString(R.string.text_edit_payment_title));
                                for (GetUserData.DataBean.CardsBean cardsBean : cardData) {

                                    byte[] data = Base64.decode(cardsBean.getCard_number(), Base64.DEFAULT);
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                        cardNo = new String(data, StandardCharsets.UTF_8);
                                    }
//                                    String decordNumber = cardsBean.getCard_number();

//                                    try {
//                                        cardNo = URLDecoder.decode(data, "UTF-8");
//                                    } catch (UnsupportedEncodingException e) {
//                                        e.printStackTrace();
//                                    }
                                    Applog.E("Card No===> " + cardNo);
                                    edtTvCardNumber.setText(cardNo);
                                    edtTvExpDate.setText(cardsBean.getExp_month_year());
                                    edtTvCvv.setText(cardsBean.getCvv_number());
                                    edtTvZipCode.setText(cardsBean.getZipcode());
                                }
                            }
//                            edtTvProfileFname.setText(userDetails.getData().getName());
//                            edtTvProfileLname.setText(userDetails.getData().getLastname());
//                            strEmail = userDetails.getData().getEmail();
//                            SessionManager.saveUserData(context, userDetails);
                            SnackBar.showSuccess(context, snackBarView, "Profile Update successfully.");
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
                public Map<String, String> getHeaders() throws AuthFailureError {
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
