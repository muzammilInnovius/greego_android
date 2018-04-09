package com.greegoapp.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyLocation extends Service {

	LocationManager lm;
	Location myLocation;
	LocationListener locationListener;
	private final long MIN_TIME = 400;
	private final float MIN_DISTANCE = 1000;
	
	@Override
	public void onStart(Intent intent, int startid) {

		myLocation = null;
		
 		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
 		
 		Criteria criteria = new Criteria();
 		criteria.setAccuracy(Criteria.ACCURACY_COARSE);  // Faster, no GPS fix.
 		
 		String provider = lm.getBestProvider(criteria, true); // only retrieve enabled providers.
 		if(provider==null){
 			
 			stopSelf();
 			return;
 		}
 		
 	        locationListener = new LocationListener() {
 	            public void onLocationChanged(Location location) {
 	              // Called when a new location is found by the network location provider.
 	            	if(lm!=null){
 	            		 makeUseOfNewLocation(location);
 	            	}
 	            }

 				public void onStatusChanged(String provider, int status, Bundle extras) {
// 					Toast.makeText(c, "Out Of Service", Toast.LENGTH_LONG).show();
 				}

 	            public void onProviderEnabled(String provider) {
// 	            	Toast.makeText(c, "Provider enable", Toast.LENGTH_LONG).show();
 	            }

 	            public void onProviderDisabled(String provider) {
// 	            	Toast.makeText(c, "Provider disable", Toast.LENGTH_LONG).show();
 	            }
 	        };
 	         
 	        // Register the listener with the Location Manager to receive location updates
					lm.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, locationListener);					
     }
	
	
	private void makeUseOfNewLocation(Location location) {
     	myLocation = location;
     	Log.v("location","Found> "+myLocation.getLatitude()+". "+myLocation.getLongitude());
     	
     	SharedPreferences sp = getSharedPreferences("user_id_share", MODE_PRIVATE);
     	Editor edit = sp.edit();
     	final String latitude = ""+myLocation.getLatitude();
     	final String longitude = ""+myLocation.getLongitude();
     	edit.putString(AppConstants.LONGITUDE, longitude);
     	edit.putString(AppConstants.LATTITUDE, latitude);
     	edit.commit();
     	
     	try {
     		
     		lm.removeUpdates(locationListener);	
     		locationListener = null;
     		lm = null;
     		stopSelf();
		} catch (Exception e) {
			locationListener = null;
			lm = null;
     		stopSelf();
//     		Log.e("GPS",">"+e.toString());
			// TODO: handle exception
		}
     	
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
