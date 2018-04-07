
package com.greegoapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greegoapp.R;
import com.greegoapp.databinding.ActivityMainBinding;

public class MainActivity extends Activity implements View.OnClickListener {


    ActivityMainBinding binding;
    TextView tvMobileNo;
    LinearLayout llNumber;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = MainActivity.this;

        bindView();
        setListner();
    }

    private void setListner() {
        tvMobileNo.setOnClickListener(this);
        llNumber.setOnClickListener(this);
    }

    private void bindView() {
        tvMobileNo = binding.tvMobileNo;
        llNumber=binding.llNumber;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llNumber:
                Intent in = new Intent(context, SignUpMobileNumberActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                break;

        }
    }
}
