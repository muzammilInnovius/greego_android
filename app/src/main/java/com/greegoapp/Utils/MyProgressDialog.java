package com.greegoapp.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

/**
 * Created by Android on 28-Feb-18.
 */

public class MyProgressDialog {
    private static ProgressDialog progressBar;

    public static void showProgressDialog(Context context) {
        try {
        progressBar = new ProgressDialog(context);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void hideProgressDialog() {
        try {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
