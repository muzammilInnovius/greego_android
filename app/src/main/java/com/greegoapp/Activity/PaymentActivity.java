package com.greegoapp.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.Adapter.CardsNumberAdapter;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityPaymentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.greegoapp.Activity.HomeActivity.SLIDER_PAYMENT_METHOD;
import static com.greegoapp.Activity.HomeActivity.CHANGE_CONTACT_NO;


public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    public static String cardselected;
    ActivityPaymentBinding binding;
    private View snackBarView;
    Context context;
    TextView tvAddPaymentMethod, tvAddPromoCode, tvCardDetail;
    ImageButton ibBack;
    String cardNo;
    public String selectCardNo;
    public static final int ADD_CARD_PAYMENT_METHOD = 1001;

    //priyanka(27/4)
    private RecyclerView rvCards;
    public static CardsNumberAdapter adapter;
    private ArrayList<GetUserData.DataBean.CardsBean> alCardData;
    SwipeRefreshLayout swiper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);

        snackBarView = findViewById(android.R.id.content);
        context = PaymentActivity.this;
        alCardData = new ArrayList<>();

        bindViews();
        setListner();

        if (ConnectivityDetector.isConnectingToInternet(context)) {
            callUserMeApi();
        } else {
            SnackBar.showInternetError(context, snackBarView);
        }
//        setCardNumber();

        Applog.E("selectCardNo====> " + selectCardNo);

        ViewParent parent = rvCards.getParent();
        parent.clearChildFocus(rvCards);

//        rvCards.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                super.onScrollStateChanged(recyclerView, scrollState);
//
//                if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {
//                    View currentFocus = getCurrentFocus();
//                    if (currentFocus != null) {
//                        currentFocus.clearFocus();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });

    }

    private void setCardNumber() {
//        cardNo = SessionManager.getCardNo(context);

        int length = cardNo.length();
        if (length > 0) {
            String s = cardNo;
          /*  String s1 = s.substring(0, 4);
            String s2 = s.substring(4, 8);
            String s3 = s.substring(8, 12);*/
            String s4 = s.substring(12, 16);

//            String dashedString = s1 + " " + s2 + " " + s3 + " "+ s4;
            String strcardnumber = "**** **** **** " + s4;
            //        tvCardDetail.setText(strcardnumber);
        } else {
            //        tvCardDetail.setText("**** **** **** ****");
        }
    }

    private void setListner() {
        tvAddPromoCode.setOnClickListener(this);
        tvAddPaymentMethod.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }

    private void bindViews() {
        tvAddPaymentMethod = binding.tvAddPaymentMethod;
        tvAddPromoCode = binding.tvAddPromoCode;
        ibBack = binding.ibBack;
        //    tvCardDetail = binding.tvCardDetail;
        rvCards = binding.rvCards;


    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddPaymentMethod:
                Intent in = new Intent(context, AddPaymentMethodActivity.class);
                startActivityForResult(in, ADD_CARD_PAYMENT_METHOD);
                break;
            case R.id.tvAddPromoCode:
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(R.layout.dialog_add_promocode);
                alert.setCancelable(false);


                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

                KeyboardUtility.hideKeyboard(context, view);
                break;
            case R.id.ibBack:
//                callUserMeApi();
                if (CHANGE_CONTACT_NO == 2001) {
                    Intent data = new Intent();
                    data.putExtra("changeCardNo", cardselected);
                    setResult(CHANGE_CONTACT_NO, data);
                } else if (SLIDER_PAYMENT_METHOD == 20001) {
                    Intent data = new Intent();
                    setResult(SLIDER_PAYMENT_METHOD, data);
                }
                finish();
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_CARD_PAYMENT_METHOD:
              /*  cardNo = SessionManager.getCardNo(context);
//                String strNumber = data.getStringExtra("cardNumber");
                tvCardDetail.setText(cardNo);*/
                if (ConnectivityDetector.isConnectingToInternet(context)) {
                    callUserMeApi();
                } else {
                    SnackBar.showInternetError(context, snackBarView);
                }

                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void callUserMeApi() {
        try {

            JSONObject jsonObject = new org.json.JSONObject();

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.USER_ME.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    GetUserData userDetails = new Gson().fromJson(String.valueOf(response), GetUserData.class);
                    GetUserData.DataBean.CardsBean userCardDtls = new Gson().fromJson(String.valueOf(response), GetUserData.DataBean.CardsBean.class);

                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {
                            alCardData.clear();
                            alCardData.addAll(userDetails.getData().getCards());
//                            adapter.notifyDataSetChanged();

                            if (userDetails.getData() == null) {

                                SnackBar.showError(context, snackBarView, "No cards found");
                            }

                            setRecyclerView();

//
//                            adapter = new CardsNumberAdapter(context, alCardData, itemClickListener);
//                            rvCards.setAdapter(adapter);
//                            rvCards.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
//                                    LinearLayoutManager.VERTICAL, true));


//                            if (userCardDtls != null) {
//
//                                if (userCardDtls.getSelected() == 1) {
//
//                                    selectCardNo = userCardDtls.getCard_number();
//                                }
//                            }

                        } else {
                            MyProgressDialog.hideProgressDialog();
                            SnackBar.showError(context, snackBarView, response.getString("message"));
                        }
                    } catch (
                            JSONException e)

                    {
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
                    java.util.Map<String, String> params = new java.util.HashMap<String, String>();

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
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }

    private void setRecyclerView() {
        RecyclerViewItemClickListener mListener = new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Fragment fragment = new TripHistoryDetailFragment();
//                FragmentManager fragmentManager = UserProfileActivity.this.getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.containerBody, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

            }
        };


        adapter = new CardsNumberAdapter(context, alCardData, mListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rvCards.setLayoutManager(mLayoutManager);
        rvCards.setItemAnimator(new DefaultItemAnimator());
        rvCards.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}
