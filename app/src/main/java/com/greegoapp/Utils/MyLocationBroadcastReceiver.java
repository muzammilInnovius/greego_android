package com.greegoapp.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.greegoapp.Activity.SignUpAgreementActivity;


public class MyLocationBroadcastReceiver extends BroadcastReceiver {

	// private String strSuccess = "1";
	private Context context;
	private LocationManager locationManager;
	private LocationListener listener;
//	private callGetClosestListner callgetGetClosestlistner;
//	private int callfrom;

	// private GPSTracker gps;


	@SuppressLint("NewApi")
	public void onReceive(final Context context, Intent intent) {
		this.context = context;
		Applog.v("log", "onReceive");

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

//		if (intent !=null && intent.getExtras()!=null){
//		    callfrom= intent.getExtras().getInt("CALLFROM");
//			Applog.v("log", "callfrom***"+callfrom);
//		}


		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			listener = new LocationListener() {

				@Override
				public void onLocationChanged(Location location) {
					double precision = Math.pow(10, 6);
					double valueLatitude = ((int) (precision * location
							.getLatitude())) / precision;
					double valueLongitude = ((int) (precision * location
							.getLongitude())) / precision;
					Applog.v("log", "onLocationChanged");
					Applog.v("log", "LAT: " + valueLatitude + " & LONG: "
							+ valueLongitude);

					String lat = String.valueOf(valueLatitude);
					String lng = String.valueOf(valueLongitude);

					try {
						if (!SessionManager.getlattitude(context).equals(
								valueLatitude)
								|| !SessionManager.getlongitude(context)
								.equals(valueLongitude)) {

							SessionManager.saveLocation(valueLatitude,
									valueLongitude, context);

							SignUpAgreementActivity.callGetClosetListner.callGetClosest
									(lat, lng);
//							ProfressionalSignInFragment.callGetClosetListner.callGetClosest
//									(lat, lng);
//							UserSignInFragment.callGetClosetListner.callGetClosest
//									(lat, lng);


						}
						if (SessionManager.progressDialog != null && SessionManager.progressDialog.isShowing()) {
							SessionManager.progressDialog.dismiss();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onProviderDisabled(String arg0) {
				}

				@Override
				public void onProviderEnabled(String arg0) {
				}

				@Override
				public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				}

			};
			try {

				locationManager.requestSingleUpdate(
						LocationManager.NETWORK_PROVIDER, listener, null);
//				SessionManager.progressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// GlobalData.showSettingsAlert(context);
		}
	}

	public interface callGetClosestListner {
		public void callGetClosest(String lat, String lng);

	}
}
