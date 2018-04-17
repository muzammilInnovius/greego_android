package com.greegoapp.Utils;

import android.content.Context;
import android.provider.Settings.Secure;

import com.google.android.gms.auth.GoogleAuthUtil;

public class DeviceId {
//    String accountName = getAccount();

    public static String getDeviceId(Context context) {
        String deviceId = "";
        try {
            deviceId = Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }


//    public static String getFCMId(Context context) {
//        String idToken = null;
//        try {
//            idToken = GoogleAuthUtil.getToken(context, accountName, scope);
//        } catch (Exception e) {
//            Applog.E("exception while getting idToken: " + e);
//        }
//        return idToken;
//    }

}
