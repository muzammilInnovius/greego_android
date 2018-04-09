package com.greegoapp.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Sony Raj on 21/11/17.
 */

public class NewLocationAvailableReceiver extends BroadcastReceiver {

    public static final String LOCATION_AVAILABLE_ACTION = "com.greegoapp.BroadcastReceiver.LOACTION_AVAILABLE";
    public static final String LAT = "lat";
    public static final String LNG = "lng";


    private OnLocationChangedListener mOnLocationChangedListener;

    public NewLocationAvailableReceiver(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) return;
        if (!intent.getAction().equals(LOCATION_AVAILABLE_ACTION)) return;

        double lat = intent.getDoubleExtra(LAT, 0);
        double lng = intent.getDoubleExtra(LNG, 0);


        if (lat == 0 || lng == 0) return;
        mOnLocationChangedListener.onChange(lat, lng);
    }


    public interface OnLocationChangedListener {
        void onChange(Double lat, double lng);
    }
}
