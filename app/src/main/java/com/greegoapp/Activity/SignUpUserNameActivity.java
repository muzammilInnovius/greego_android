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
import com.greegoapp.databinding.ActivitySignUpUserNameBinding;

public class SignUpUserNameActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySignUpUserNameBinding binding;
    EditText edtTvFname, edtTvLname;
    ImageButton ibBack, ibFinish;
    Context context;
    private View snackBarView;
    String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user_name);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_user_name);
        strEmail = getIntent().getStringExtra("email");

        context = SignUpUserNameActivity.this;
        snackBarView = findViewById(android.R.id.content);
        bindView();
        setListners();
    }

    private void setListners() {
        edtTvFname.setOnClickListener(this);
        edtTvLname.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        ibFinish.setOnClickListener(this);
    }

    private void bindView() {
        edtTvFname = binding.edtTvFname;
        edtTvLname = binding.edtTvLname;
        ibBack = binding.ibBack;
        ibFinish = binding.ibFinish;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                Intent in = new Intent(context, SignUpEmailActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);

                break;
            case R.id.ibFinish:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callUnameApi();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;
        }
    }

    private void callUnameApi() {
        String strFName = edtTvFname.getText().toString();
        String strLName = edtTvLname.getText().toString();

        Intent in = new Intent(context, SignUpAgreementActivity.class);
        in.putExtra("email", strEmail);
        in.putExtra("fName", strFName);
        in.putExtra("lName", strLName);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);

    }

    private boolean isValid() {
        return true;
    }
}
