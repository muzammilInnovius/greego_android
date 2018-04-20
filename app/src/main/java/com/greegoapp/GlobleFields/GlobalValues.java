package com.greegoapp.GlobleFields;

import com.greegoapp.Utils.Applog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Android on 01-Feb-18.
 */

public class GlobalValues {

    public static final String BEARER_TOKEN = "Bearer ";
    public static final int SIGN_UP_NORMAL = 1;
    public static final int SIGN_UP_WITH_FB = 2;
    public static final String IS_FROM_CHANGE_ACCOUNT = "is_from_change_account";
    public static final String IS_FROM_LOGOUT = "is_from_logout";
    public static final String COUNTRY = "USA";

    public static final String ACCOUNT_TYPE = "AccountType";
    public static final String MANAGE_ACCOUNTANTS_TYPE = "AccountantsType";
    public static final String TRANSACTION_DETAILS = "transactionDetail";
    public static final String IS_TRANSACTION_DELETE = "isTransactionDelete";
    public static final String FAVORITE_CHART_OF_ACCOUNT_TYPE = "ExpenseAccountantsType";

    public static final int TAX_LIMIT = 6;
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";



    public static final int PAYMENT_METHOD_SELECTION_ACC_RECEIVABLE = 1451;

    public static final int TIME_OUT = 15000;



    public static long dataDifferent(String dateStop) {
        Date calendar = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.US);

        Date dStart = null;
        Date dStop = null;
        long diffDays = 0;

        try {
            dStart = dateFormat.parse(calendar.toString());
            dStop = dateFormat.parse(dateStop);

            //in milliseconds
            long diff = dStop.getTime() - dStart.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days====>");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Applog.e("diffDays==>" ,"" + diffDays);
        return diffDays;

    }
}
