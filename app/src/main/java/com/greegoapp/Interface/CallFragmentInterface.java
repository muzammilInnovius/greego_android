package com.greegoapp.Interface;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.greegoapp.Model.GetUserData;

import java.util.ArrayList;

public interface CallFragmentInterface {
    void onFragmentCall(Context mContext, Fragment myFragment, String tag);


    public void callUpdateVehicalMethod(ArrayList<GetUserData.DataBean.VehiclesBean> alVehicleDetail);
}
