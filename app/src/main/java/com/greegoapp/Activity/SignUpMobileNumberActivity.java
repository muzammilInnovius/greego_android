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
import com.greegoapp.databinding.ActivitySignUpMobileNumberBinding;

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
        switch (view.getId()){
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
        Intent in = new Intent(context, DigitCodeActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }

    private boolean isValid() {
        String strMobileNo = edtTvMobileNo.getText().toString();

        if (strMobileNo.isEmpty()) {
            edtTvMobileNo.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_mobile_no_empty));
            return false;
        }
        else if(strMobileNo.length()< 8 || strMobileNo.length()>10)
        {
            edtTvMobileNo.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.mobile_no_length));
            return false;
        }
        return true;
    }
}
