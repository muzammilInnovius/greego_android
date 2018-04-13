package com.greegoapp.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.greegoapp.R;
import com.greegoapp.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {
    ActivityHelpBinding binding;
    Context context;
    private View snackBarView;

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
    }

    private void bindViews() {


    }
}
