
package com.greegoapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greegoapp.R;
import com.greegoapp.databinding.ActivityMainBinding;

public class MainActivity extends Activity implements View.OnClickListener {


    ActivityMainBinding binding;
    TextView tvMobileNo;
    LinearLayout llNumber;
    Context context;
   private static int FIRST_SCREEN = 101;

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
        llNumber = binding.llNumber;
    }

    @Override
    public void onClick(View view) {
        Intent in;

        switch (view.getId()) {
            case R.id.llNumber:
                in = new Intent(context, SignUpMobileNumberActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(in,FIRST_SCREEN);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                finish();
                break;

            case R.id.tvMobileNo:
                in = new Intent(context, SignUpMobileNumberActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                startActivityForResult(in,FIRST_SCREEN);
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FIRST_SCREEN) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(context, "First screen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
