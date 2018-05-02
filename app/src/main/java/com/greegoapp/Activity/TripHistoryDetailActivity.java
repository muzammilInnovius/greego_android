package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greegoapp.Model.TripHistoryModel;
import com.greegoapp.R;
import com.greegoapp.Utils.Applog;
import com.greegoapp.databinding.ActivityTripHistoryDetailBinding;

import java.util.ArrayList;

public class TripHistoryDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityTripHistoryDetailBinding binding;
    private View snackBarView;
    TextView tvdate;
    Context context;
    ImageButton ibback;
    ArrayList<TripHistoryModel.DataBean> alTripHistoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_history_detail);

        snackBarView = findViewById(android.R.id.content);
        context = TripHistoryDetailActivity.this;

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        alTripHistoryList = bundle.getParcelable("tripHistoryDetails");

        bindViews();

        setListner();


    }


    private void setListner() {
        ibback.setOnClickListener(this);
    }

    private void bindViews() {
        ibback = binding.ibBack;
        tvdate = binding.tvTripHistoryTitle;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                finish();
                break;
        }
    }

}
