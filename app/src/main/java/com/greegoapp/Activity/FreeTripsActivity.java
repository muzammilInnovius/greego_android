package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.greegoapp.R;
import com.greegoapp.databinding.ActivityFreeTripsBinding;

public class FreeTripsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityFreeTripsBinding binding;
    Context context;
    ImageButton ibBack;
    private View snackBarView;
    TextView tvInviteCode;
    ImageView ivInviteCode;
    String strInviteCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_free_trips);

        snackBarView = findViewById(android.R.id.content);
        context = FreeTripsActivity.this;

        bindViews();
        setListner();
    }


    private void setListner() {
        ibBack.setOnClickListener(this);
        ivInviteCode.setOnClickListener(this);
    }

    private void bindViews() {
        ibBack = binding.ibBack;
        tvInviteCode = binding.tvInviteCode;
        ivInviteCode = binding.ivInviteCode;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                finish();
                break;
            case R.id.ivInviteCode:
                shareFreeTripInviteCode();
                break;
        }
    }


    // Method to share either text or URL.
    private void shareFreeTripInviteCode() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        strInviteCode = tvInviteCode.getText().toString();

        share.putExtra(Intent.EXTRA_TEXT, strInviteCode);
        startActivity(Intent.createChooser(share, "Share On Trip details!"));
    }
}
