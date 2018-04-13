package com.greegoapp.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.greegoapp.Fragment.AddPaymentMethodFragment;
import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.R;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import com.greegoapp.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPaymentBinding binding;
    private View snackBarView;
    Context context;
    TextView tvAddPaymentMethod, tvAddPromoCode;
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);

        snackBarView = findViewById(android.R.id.content);
        context = PaymentActivity.this;

        bindViews();
        setListner();

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

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddPaymentMethod:
                Fragment fragment = new AddPaymentMethodFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerBody, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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


}
