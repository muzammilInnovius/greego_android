package com.greegoapp.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPaymentBinding binding;
    private View snackBarView;
    Context context;
    TextView tvAddPaymentMethod, tvAddPromoCode, tvCardDetail;
    ImageButton ibBack;
    String cardNo;

    public static final int ADD_CARD_PAYMENT_METHOD = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);

        snackBarView = findViewById(android.R.id.content);
        context = PaymentActivity.this;

        bindViews();
        setListner();
        setCardNumber();
    }

    private void setCardNumber() {
        cardNo = SessionManager.getCardNo(context);

        int length = cardNo.length();
        if(length>0)
        {
            String s = cardNo;
          /*  String s1 = s.substring(0, 4);
            String s2 = s.substring(4, 8);
            String s3 = s.substring(8, 12);*/
            String s4 = s.substring(12,16);

//            String dashedString = s1 + " " + s2 + " " + s3 + " "+ s4;
            String strcardnumber ="**** **** **** "+s4;
            tvCardDetail.setText(strcardnumber);
        }
        else
        {
            tvCardDetail.setText("**** **** **** ****");
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
        tvCardDetail = binding.tvCardDetail;

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
              /*  alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();
                    }
                });*/
                AlertDialog dialog = alert.create();
                dialog.show();
                break;
            case R.id.ibBack:
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_CARD_PAYMENT_METHOD:
              /*  cardNo = SessionManager.getCardNo(context);
//                String strNumber = data.getStringExtra("cardNumber");
                tvCardDetail.setText(cardNo);*/
                    setCardNumber();
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
