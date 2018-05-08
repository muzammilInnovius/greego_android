package com.greegoapp.FCM;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.UserDriverTripDetails;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.WebFields;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.greegoapp.Fragment.MapHomeFragment.REQUEST_USER_TRIP;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    String trip_id, status = null, payment_status, totalAmount;
    private NotificationUtils notificationUtils;
    Context context = this;
    ArrayList<UserDriverTripDetails.DataBean> alUserDrTripDetails;
    JSONObject json;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            Applog.e("==>1=>", "PushNotification" + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            String payload = remoteMessage.getData().toString();
            try {
                if (payload.contains("status")) {
                    json = new JSONObject(remoteMessage.getData().toString());
                    payload = json.optString("status");
                    if (payload.contains("2")) {
                        status = "2";
                        //json = new JSONObject(remoteMessage.getData().toString());
                    } else if (payload.contains("3")) {
                        status = "3";
                        //json = new JSONObject(remoteMessage.getData().toString());
                    } else if (payload.contains("4")) {
                        status = "4";
                        //  json = new JSONObject(remoteMessage.getData().toString());
                    }
                    Log.e("Payload check", " happy :): ");
                } else if (payload.contains("payment_status")) {
                    payment_status = json.optString("payment_status");
                    totalAmount = json.optString("total_amount");
                } else {
                    status = "1";
                    //json = new JSONObject(remoteMessage.getData().toString());
                }

                json = new JSONObject(remoteMessage.getData().toString());
                trip_id = json.optString("trip_id");

                if (status != null) {
                    handleDataMessage(trip_id, status);
                } else if (payment_status != null) {
                    handlePaymentDataMessage(trip_id, payment_status,totalAmount);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }
    }

    private void handlePaymentDataMessage(String trip_id, String payment_status, String totalAmount) {
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());

        Applog.e("==>3=>", "PushNotification" + json.toString());

        if (payment_status.equals("1")) {
            String data = json.toString();
            data = data.substring(data.indexOf(":") + 1, data.length() - 1);

            Intent intent = new Intent();
            intent.setAction(REQUEST_USER_TRIP);
            intent.putExtra("trip_id", trip_id);
            intent.putExtra("payment_status", payment_status);
            intent.putExtra("total_amount", totalAmount);
            sendBroadcast(intent);

        } else {

        }

        try {

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                notificationUtils.playNotificationSound();
            } else {
                notificationUtils.playNotificationSound();
            }
        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }
    }


    private void handleNotification(String tripId) {


        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Applog.e("==>2=>", "PushNotification" + tripId);

            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", tripId);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);

//            MyProgressDialog.hideProgressDialog();
            Applog.e("==>", "PushNotification");
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();

            SessionManager.saveTripId(this, tripId);


        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }


    private void handleDataMessage(String trip_id, String status) {
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());

        Applog.e("==>3=>", "PushNotification" + json.toString());

        if (status.equals("1")) {
            String data = json.toString();
            data = data.substring(data.indexOf(":") + 1, data.length() - 1);

            callUserTripDetailsAPI(trip_id);
        } else {
            Intent intent = new Intent();
            intent.setAction(REQUEST_USER_TRIP);
            intent.putExtra("tripStatus", status);
            sendBroadcast(intent);
        }

        try {

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                notificationUtils.playNotificationSound();
            } else {
                notificationUtils.playNotificationSound();
            }
        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }
    }


    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


    private void callUserTripDetailsAPI(String tripId) {
        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(WebFields.GET_USER_TRIP_DETAILS.PARAM_TRIP_ID, Integer.parseInt(tripId));

            Applog.E("request: trip " + jsonObject.toString());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.GET_USER_TRIP_DETAILS.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());


                    UserDriverTripDetails driverTripDetails = new Gson().fromJson(String.valueOf(response), UserDriverTripDetails.class);

                    alUserDrTripDetails = new ArrayList<>();

                    if (driverTripDetails.getError_code() == 0) {

                        alUserDrTripDetails.add(driverTripDetails.getData());
//                        alUserDrDetails.add(driverDetails);

//                    alUserNearDriverDtls = new ArrayList<UserNearDriverList.DataBean>();
                        Applog.E("size==>" + alUserDrTripDetails);
                        MyProgressDialog.hideProgressDialog();

                        Intent intent = new Intent();
                        intent.setAction(REQUEST_USER_TRIP);
                        intent.putExtra("tripStatus", status);
                        intent.putParcelableArrayListExtra("driverAllData", alUserDrTripDetails);
                        sendBroadcast(intent);

                    } else {
                        MyProgressDialog.hideProgressDialog();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressDialog.hideProgressDialog();
                    Applog.E("Error: " + error.getMessage());
//                    SnackBar.showError(this, snackBarView, getResources().getString(R.string.something_went_wrong));
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put(WebFields.PARAM_ACCEPT, "application/json");
                    Applog.E("Token==>" + SessionManager.getToken(context));
                    params.put(WebFields.PARAM_AUTHOTIZATION, GlobalValues.BEARER_TOKEN + SessionManager.getToken(context));

                    return params;
                }
            };

            MyProgressDialog.hideProgressDialog();
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    GlobalValues.TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
