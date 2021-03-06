package com.greegoapp.SessionManager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.greegoapp.Model.GetUserData;
import com.greegoapp.Model.Login;
import com.greegoapp.Model.UserData;
import com.greegoapp.Model.UserDriverTripDetails;
import com.greegoapp.Model.UserTripStatus;
import com.greegoapp.Model.VehicleUpdate;
import com.greegoapp.Utils.AppConstants;
import com.greegoapp.Utils.Applog;

public class SessionManager {
    public static ProgressDialog progressDialog;
    private static final String PREF_NAME = "GREEGO_APP_SESSION";
    static GoogleApiClient googleApiClient;
    static AlarmManager alarmManager;
    static PendingIntent pendingIntent;


    public static void saveVehical(Context context, VehicleUpdate vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppPrefFields.PARAM_VEHICLE_MANUFACTURE, vo.getManufacture());
            editor.putString(AppPrefFields.PARAM_VEHICLE_MODEL, vo.getModel());
            editor.putString(AppPrefFields.PARAM_VEHICLE_COLOR, vo.getColor());
            editor.putInt(AppPrefFields.PARAM_VEHICLE_YEAR, vo.getYear());

            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void saveUserTripDetails(Context context, UserDriverTripDetails.DataBean trpDtl) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(AppPrefFields.PARAM_TRIP_DRIVER_ID, trpDtl.getDriver_id());
            editor.putString(AppPrefFields.PARAM_TRIP_DRIVER_NAME, trpDtl.getDriver().getName());
            editor.putString(AppPrefFields.PARAM_TRIP_DRIVER_PIC, trpDtl.getDriver().getProfile_pic());
            editor.putString(AppPrefFields.PARAM_TRIP_DRIVER_CONTACT_NO, trpDtl.getDriver().getContact_number());

            editor.putString(AppPrefFields.PARAM_DR_PROMOCODE, trpDtl.getDriver().getPromocode());
            editor.putString(AppPrefFields.PARAM_DR_CURRENT_STATUS, trpDtl.getDriver().getCurrent_status());
            editor.putString(AppPrefFields.PARAM_DR_LAT, trpDtl.getDriver_location().getLat());
            editor.putString(AppPrefFields.PARAM_DR_LNG, trpDtl.getDriver_location().getLng());
            editor.putString(AppPrefFields.PARAM_DR_ACTUAL_TRIP_AMOUNT, trpDtl.getActual_trip_amount());
            editor.putString(AppPrefFields.PARAM_DR_ACTUAL_TRIP_TRAVEL_TIME, trpDtl.getActual_trip_travel_time());
            editor.putString(AppPrefFields.PARAM_DR_TOTAL_TRIP_AMOUNT, trpDtl.getTotal_trip_amount());

            editor.putString(AppPrefFields.PARAM_REQ_ID, trpDtl.getRequest_id());
            editor.putString(AppPrefFields.PARAM_REQ_EST_TRIP_COST, trpDtl.getRequest().getTotal_estimated_trip_cost());
            editor.putString(AppPrefFields.PARAM_REQ_EST_TRAVEL_TIME, trpDtl.getRequest().getTotal_estimated_travel_time());
            editor.putString(AppPrefFields.PARAM_REQ_FROM_LAT, trpDtl.getRequest().getFrom_lat());
            editor.putString(AppPrefFields.PARAM_REQ_FROM_LNG, trpDtl.getRequest().getFrom_lng());
            editor.putString(AppPrefFields.PARAM_REQ_FROM_ADD, trpDtl.getRequest().getFrom_address());
            editor.putString(AppPrefFields.PARAM_REQ_TO_ADD, trpDtl.getRequest().getTo_address());

            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static UserDriverTripDetails.DataBean getUserTripDtl(Context context) {
        UserDriverTripDetails.DataBean userTripDtl = new UserDriverTripDetails.DataBean();

        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            userTripDtl.setDriver_id(pref.getString(AppPrefFields.PARAM_TRIP_DRIVER_ID, ""));
            userTripDtl.setActual_trip_amount(pref.getString(AppPrefFields.PARAM_DR_ACTUAL_TRIP_AMOUNT, ""));
            userTripDtl.setActual_trip_travel_time(pref.getString(AppPrefFields.PARAM_DR_ACTUAL_TRIP_TRAVEL_TIME, ""));
            userTripDtl.setTotal_trip_amount(pref.getString(AppPrefFields.PARAM_DR_TOTAL_TRIP_AMOUNT, ""));
            userTripDtl.setRequest_id(pref.getString(AppPrefFields.PARAM_REQ_ID, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userTripDtl;
    }


    public static UserDriverTripDetails.DataBean.DriverBean getDriverTripDtl(Context context) {
        UserDriverTripDetails.DataBean.DriverBean driverDtl = new UserDriverTripDetails.DataBean.DriverBean();

        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            driverDtl.setName(pref.getString(AppPrefFields.PARAM_TRIP_DRIVER_NAME, ""));
            driverDtl.setProfile_pic(pref.getString(AppPrefFields.PARAM_TRIP_DRIVER_PIC, ""));
            driverDtl.setContact_number(pref.getString(AppPrefFields.PARAM_TRIP_DRIVER_CONTACT_NO, ""));
            driverDtl.setPromocode(pref.getString(AppPrefFields.PARAM_DR_PROMOCODE, ""));
            driverDtl.setCurrent_status(pref.getString(AppPrefFields.PARAM_DR_CURRENT_STATUS, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return driverDtl;
    }

    public static UserDriverTripDetails.DataBean.DriverLocationBean getUserDrTripLocDtl(Context context) {
        UserDriverTripDetails.DataBean.DriverLocationBean drLocDtl = new UserDriverTripDetails.DataBean.DriverLocationBean();

        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            drLocDtl.setLat(pref.getString(AppPrefFields.PARAM_DR_LAT, ""));
            drLocDtl.setLng(pref.getString(AppPrefFields.PARAM_DR_LNG, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return drLocDtl;
    }

    public static UserDriverTripDetails.DataBean.RequestBean getUserRequestTripDtl(Context context) {
        UserDriverTripDetails.DataBean.RequestBean reqDtl = new UserDriverTripDetails.DataBean.RequestBean();

        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            reqDtl.setTotal_estimated_trip_cost(pref.getString(AppPrefFields.PARAM_REQ_EST_TRIP_COST, ""));
            reqDtl.setTotal_estimated_travel_time(pref.getString(AppPrefFields.PARAM_REQ_EST_TRAVEL_TIME, ""));

            reqDtl.setFrom_lat(pref.getString(AppPrefFields.PARAM_REQ_FROM_LAT, ""));
            reqDtl.setFrom_lng(pref.getString(AppPrefFields.PARAM_REQ_FROM_LNG, ""));

            reqDtl.setFrom_address(pref.getString(AppPrefFields.PARAM_REQ_FROM_ADD, ""));
            reqDtl.setTo_address(pref.getString(AppPrefFields.PARAM_REQ_TO_ADD, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reqDtl;
    }


    public static VehicleUpdate getUpdatedVehical(Context context) {
        VehicleUpdate vehicleDtl = new VehicleUpdate();
        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            vehicleDtl.setManufacture(pref.getString(AppPrefFields.PARAM_VEHICLE_MANUFACTURE, ""));
            vehicleDtl.setModel(pref.getString(AppPrefFields.PARAM_VEHICLE_MODEL, ""));
            vehicleDtl.setYear(pref.getInt(AppPrefFields.PARAM_VEHICLE_YEAR, 0));
            vehicleDtl.setColor(pref.getString(AppPrefFields.PARAM_VEHICLE_COLOR, ""));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicleDtl;
    }


    public static void saveTripId(Context context, String vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppPrefFields.PARAM_TRIP_ID, vo);

            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    //Save and get status

    public static void saveTripStaus(Context context, UserTripStatus vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppPrefFields.PARAM_TRIP_ID, vo.getTrip_id());
            editor.putString(AppPrefFields.PARAM_TRIP_STATUS, vo.getTrip_status());

            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static String getTripId(Context context) {
        String tripId = "";
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            tripId = preferences.getString(AppPrefFields.PARAM_TRIP_ID, "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return tripId;
    }

    public static String getTripStatus(Context context) {
        String tripStatus = "";
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            tripStatus = preferences.getString(AppPrefFields.PARAM_TRIP_STATUS, "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return tripStatus;
    }

    //.........................

    public static void setIsUserLoggedin(Context context, boolean val) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(AppPrefFields.SHARED_ISLOGGEDIN, val);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getIsUserLoggedin(Context context) {
        SharedPreferences preferences = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean val = preferences.getBoolean(AppPrefFields.SHARED_ISLOGGEDIN, false);
        return val;
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

    public static void saveUserData(Context context, Login vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppPrefFields.PARAM_CONTACT_NO, vo.getData().getContact_number());
            editor.putInt(AppPrefFields.PARAM_OTP, vo.getData().getOtp());
            editor.putString(AppPrefFields.PARAM_TOKEN, vo.getData().getToken());

            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

//    public static void clearRememberMeSession(Context context) {
//        try {
//            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_REMEMBER_ME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.clear();
//            editor.apply();
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }

    public static void clearAppSession(Context context) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
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

    public static void saveCardDetails(Context context, String vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppPrefFields.PARAM_CARD_NUMBER, vo);
//            editor.putInt(AppPrefFields.PARAM_ID, vo.getData().getId());
//            editor.putInt(AppPrefFields.PARAM_USER_ID, vo.getData().getUser_id());
//            editor.putString(AppPrefFields.PARAM_CARD_EXP_MONTH_YR, vo.getData().getExp_month_year());
//            editor.putString(AppPrefFields.PARAM_CARD_ZIPCODE_OTP, vo.getData().getZipcode());
////

            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static String getCardNo(Context context) {
        String cardNo = "";
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            cardNo = preferences.getString(AppPrefFields.PARAM_CARD_NUMBER, "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return cardNo;
    }


    public static int getOTP(Context context) {
        int otp = 0;
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            otp = preferences.getInt(AppPrefFields.PARAM_OTP, 0);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return otp;
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


    public static void saveUserDetails(Context context, GetUserData vo) {
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(AppPrefFields.PARAM_USERID, vo.getData().getId());
            editor.putString(AppPrefFields.PARAM_EMAILID, vo.getData().getEmail());
            editor.apply();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static int getUserID(Context context) {
        int userId = 0;
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            userId = preferences.getInt(AppPrefFields.PARAM_USERID, 0);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return userId;
    }

    public static String getEmailId(Context context) {
        String emailId = "";
        try {
            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            emailId = preferences.getString(AppPrefFields.PARAM_EMAILID, "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return emailId;
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
