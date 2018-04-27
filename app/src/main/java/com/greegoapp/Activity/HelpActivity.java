package com.greegoapp.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.greegoapp.R;
import com.greegoapp.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityHelpBinding binding;
    Context context;
    private View snackBarView;
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help);

        snackBarView = findViewById(android.R.id.content);
        context = HelpActivity.this;

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
