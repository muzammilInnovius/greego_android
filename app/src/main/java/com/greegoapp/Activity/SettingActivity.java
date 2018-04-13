package com.greegoapp.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.greegoapp.Fragment.UserProfileFragment;
import com.greegoapp.R;
import com.greegoapp.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySettingBinding binding;
    Context context;
    private String mParam1;
    private String mParam2;
    ImageButton ibback;
    RelativeLayout rl_name, rl_phone, rl_email, rl_btnlogout;
    View snackBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        snackBarView = findViewById(android.R.id.content);
        context = SettingActivity.this;


        bindViews();

        setListner();
    }


    private void setListner() {
        ibback.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_email.setOnClickListener(this);
        rl_btnlogout.setOnClickListener(this);
    }

    private void bindViews() {
        ibback = binding.ibBack;
        rl_name = binding.rlName;
        rl_phone = binding.rlPhone;
        rl_email = binding.rlEmail;
        rl_btnlogout = binding.rlLogout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                finish();
                break;
            case R.id.rlName:
                Fragment fragment = new UserProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerBody, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.rlEmail:
                Toast.makeText(context, "email layout click", Toast.LENGTH_LONG).show();
                break;
            case R.id.rlLogout:
                Toast.makeText(context, "logout layout click", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
