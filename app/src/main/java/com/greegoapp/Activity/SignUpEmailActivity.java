package com.greegoapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.greegoapp.R;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.EmailValidation;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.databinding.ActivitySignUpEmailBinding;

public class SignUpEmailActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySignUpEmailBinding binding;
    EditText edtTvEmail;
    ImageButton ibBack;
    ImageButton ibFinish;
    Context context;
    private View snackBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_sign_up_email);*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_email);
        context = SignUpEmailActivity.this;
        snackBarView = findViewById(android.R.id.content);
        bindView();
        setListners();
    }

    private void setListners() {
        ibFinish.setOnClickListener(this);
        edtTvEmail.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }

    private void bindView() {
        edtTvEmail = binding.edtTvEmail;
        ibBack = binding.ibBack;
        ibFinish = binding.ibFinish;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                KeyboardUtility.hideKeyboard(context, view);
                finish();
//                Intent in = new Intent(context, DigitCodeActivity.class);
//                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(in);
//                overridePendingTransition(R.anim.trans_right_out, R.anim.trans_left_in);

                break;
            case R.id.ibFinish:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callEmailAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;
        }
    }

    String email;

    private boolean isValid() {
        email = edtTvEmail.getText().toString();
        if (email.isEmpty()) {
            edtTvEmail.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.entered_email_no_empty));
            return false;
        } else if (!EmailValidation.checkEmailIsCorrect(email)) {
            edtTvEmail.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_email));
            return false;
        }

        return true;
    }

    private void callEmailAPI() {
        Intent in = new Intent(context, SignUpUserNameActivity.class);
        in.putExtra("email", email);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
        finish();
    }
}
