package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greegoapp.R;
import com.greegoapp.databinding.ActivityHelpCenterDetailBinding;

public class HelpCenterDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityHelpCenterDetailBinding binding;
    Context context;
    private View snackBarView;
    ImageButton ibBack;
    TextView tvTitle, tvDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help_center_detail);

        snackBarView = findViewById(android.R.id.content);
        context = HelpCenterDetailActivity.this;

        bindViews();
        setListner();
        Intent i = getIntent();
        if (i.getExtras() != null) {
            tvTitle.setText(i.getStringExtra("title"));
            tvDetail.setText(Html.fromHtml(i.getStringExtra("detail")));
        }
    }

    private void setListner() {
        ibBack.setOnClickListener(this);
    }

    private void bindViews() {
        tvTitle = binding.tvTitle;
        tvDetail = binding.tvHelpCenterDetail;
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
