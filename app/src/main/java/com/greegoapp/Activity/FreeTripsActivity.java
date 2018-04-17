package com.greegoapp.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.greegoapp.R;
import com.greegoapp.databinding.ActivityFreeTripsBinding;

public class FreeTripsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityFreeTripsBinding binding;
    Context context;
    ImageButton ibBack;
    private View snackBarView;

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
    }

    private void bindViews() {
        ibBack = binding.ibBack;

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
