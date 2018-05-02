package com.greegoapp.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.greegoapp.R;
import com.greegoapp.databinding.ActivityUserEmailBinding;

public class UserEmailActivity extends AppCompatActivity implements View.OnClickListener {


    TextView tvSendConfEmail, edtTvUserEmail;
    ImageView imgVwCancel;
    ImageButton ibBack;
    ActivityUserEmailBinding binding;
    private View snackBarView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_email);

        snackBarView = findViewById(android.R.id.content);
        context = UserEmailActivity.this;

        bindViews();
        setListners();

    }

    private void setListners() {
        ibBack.setOnClickListener(this);
        imgVwCancel.setOnClickListener(this);
    }

    private void bindViews() {
        tvSendConfEmail = binding.tvSendConfEmail;
        edtTvUserEmail = binding.edtTvUserEmail;
        imgVwCancel = binding.imgVwCancel;
        ibBack = binding.ibBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgVwCancel:
                edtTvUserEmail.setText("");
                break;

            case R.id.ibBack:
                finish();
                break;

            case R.id.tvSendConfEmail:
                break;

        }
    }
}
