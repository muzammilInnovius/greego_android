package com.greegoapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.greegoapp.R;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.databinding.ActivityDigitCodeBinding;

public class DigitCodeActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityDigitCodeBinding binding;
    ImageButton ibBack;
    EditText edtTvDigit1, edtTvDigit2, edtTvDigit3, edtTvDigit4, edtTvDigit5, edtTvDigit6;
    TextView tvResend;
    ImageButton ibFinish;
    PinView invitecode;


    Context context;
    private View snackBarView;

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
        invitecode.setOnClickListener(this);
    }

    private void bindView() {
        ibBack = binding.ibBack;
        invitecode = binding.txtSecurityCode;
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
                invitecode.setText("");
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


    private boolean isValid() {
        String code = invitecode.getText().toString();
        if (code.length() < 6)
        {
            invitecode.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_digit_code));
            return false;
        }
        return true;
    }

    private void callDigitCodeAPI() {
        Intent in = new Intent(context, SignUpEmailActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }
}
