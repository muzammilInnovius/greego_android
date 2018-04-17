package com.greegoapp.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greegoapp.R;


public class HeaderBar extends RelativeLayout {

    public TextView tvTitle;
    public ImageView ivLeft, ivRight,ivRightOfLeft,ivMessage,ivFriendReq,ivFrindDetails,ivNotification,ivProPicHome;
    public RelativeLayout rrHomeBtn;
    private Context context;

    public HeaderBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public HeaderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void init() {
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.headerbar, this, true);
            tvTitle = view.findViewById(R.id.tvTitle);

            ivLeft = findViewById(R.id.ivLeft);
            ivProPicHome= findViewById(R.id.ivProPicHome);
            ivRight = findViewById(R.id.ivRight);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
