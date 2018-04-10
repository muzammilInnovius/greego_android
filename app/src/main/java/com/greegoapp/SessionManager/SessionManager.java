package com.greegoapp.SessionManager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.greegoapp.Model.Login;
import com.greegoapp.Model.UserData;
import com.greegoapp.Model.VerifyOtp;
import com.greegoapp.Utils.AppConstants;
import com.greegoapp.Utils.Applog;

import java.util.Calendar;

public class SessionManager {
    public static ProgressDialog progressDialog;
    private static final String PREF_NAME = "GREEGO_APP_SESSION";
    static GoogleApiClient googleApiClient;
    static AlarmManager alarmManager;
    static PendingIntent pendingIntent;


    public static void saveUserDetails(Context context, Login vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
//            editor.putLong(AppPrefFields.PARAM_USERID, vo.().getUserId());

            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void saveMobileNo(Context context, Login vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppPrefFields.PARAM_CONTACT_NO, vo.getData().getContact_number());

            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static String getMobileNo(Context context) {
        String mobileNo = "";
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            mobileNo = preferences.getString(AppPrefFields.PARAM_CONTACT_NO, "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mobileNo;
    }

    public static void saveUserData(Context context, VerifyOtp vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppPrefFields.PARAM_CONTACT_NO, vo.getData().getContact_number());
            editor.putInt(AppPrefFields.PARAM_ID, vo.getData().getId());
            editor.putInt(AppPrefFields.PARAM_NEW, vo.getData().getNewX());
            editor.putString(AppPrefFields.PARAM_TOKEN, vo.getData().getToken());


            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void saveUserDetails(Context context, UserData vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString(AppPrefFields.PARAM_CONTACT_NO, vo.getData().getContact_number());
//            editor.putInt(AppPrefFields.PARAM_ID, vo.getData().getId());
//            editor.putInt(AppPrefFields.PARAM_NEW, vo.getData().getCity());
//            editor.putString(AppPrefFields.PARAM_TOKEN, vo.getData().getIs_iphone());


            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static String getToken(Context context) {
        String token = "";
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            token = preferences.getString(AppPrefFields.PARAM_TOKEN, "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return token;
    }


    public static long getUserID(Context context) {
        long userId = 0;
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            userId = preferences.getLong(
                    AppPrefFields.PARAM_USERID, 0);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return userId;
    }

    public static String getlattitude(Context context) {
        String point = "";
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            point = preferences.getString(AppConstants.LATTITUDE, "0.0");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return point;
    }

    public static String getlongitude(Context context) {
        String point = "";
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            point = preferences.getString(AppConstants.LONGITUDE, "0.0");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return point;
    }

    public static void saveLocation(double lat, double lng, Context context) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppConstants.LATTITUDE, "" + lat);
            editor.putString(AppConstants.LONGITUDE, "" + lng);
            editor.commit();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


    public static void setLocationService(final Activity activity) {
        // TODO Auto-generated method stub

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(activity).addApi(
                    LocationServices.API).build();
            googleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                // final LocationSettingsStates state =
                // result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Applog.v("result", "Success");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed
                        // by showing the user
                        // a dialog.
                        Applog.v("result ",
                                "RESOLUTION_REQUIRED");
                        try {
                            // Show the dialog by calling
                            // startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Applog.v("result ",
                                "SETTINGS_CHANGE_UNAVAILABLE");
                        // Location settings are not satisfied. However, we have no
                        // way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


}
