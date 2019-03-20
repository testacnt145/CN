package com.chattynotes.adapters.location;

import com.chattynotes.util.PermissionUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
 
public class GPSTracker extends Service implements LocationListener {
 
    private final Context ctx;
 
    // flag for GPS status
    boolean isGPSEnabled = false;
    boolean canGetLocation = false;
    
    // flag for network status
    boolean isNetworkEnabled = false;
 
    Location location; 
    double latitude, longitude; 
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 5 * 1000 ; // 5 seconds
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
 
//___________________________________________________________________________________________________________
    public GPSTracker(Context context) {
        this.ctx = context;
        notifier = (notifyLocationVariation)context;
        getLocation();
    }
 
    public Location getLocation() {
        try {
            locationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if(PermissionUtil.checkPermissionSilent(ctx, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        //LogUtil.e(getClass().getSimpleName(), "Getting From Network");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if(PermissionUtil.checkPermissionSilent(ctx, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            //LogUtil.e(getClass().getSimpleName(), "Getting From GPS");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ignored) {
        }
 
        return location;
    }
     
    public void stopUsingGPS() {
        if(locationManager != null) {
            if(PermissionUtil.checkPermissionSilent(ctx, Manifest.permission.ACCESS_FINE_LOCATION))
                locationManager.removeUpdates(GPSTracker.this);
        }       
    }
     
    public double getLatitude() {
        if(location != null)
            latitude = location.getLatitude();
        return latitude;
    }
     
    public double getLongitude(){
        if(location != null)
            longitude = location.getLongitude();
        return longitude;
    }
    
    public double getAccuracy() {
        if(location != null)
           return location.getAccuracy();
        return 15;
    }
     
    //Function to check GPS/wifi enabled
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    
    @Override
    public void onLocationChanged(Location _location) {
        try {
            float meters = _location.getAccuracy();
            location.setAccuracy(meters);
            notifier.locationChange();
        } catch (Exception ignored) {
            //LogUtil.exception(getClass().getSimpleName(), "onLocationChanged", ignored);
        }
    }
 
    @Override
    public void onProviderDisabled(String provider) {
    	//LogUtil.e(getClass().getSimpleName(), "onProviderDisabled");
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    	//LogUtil.e(getClass().getSimpleName(), "onProviderEnabled");
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	//LogUtil.e(getClass().getSimpleName(), "onStatusChanged");
    }
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

//_________________________________________________________________________________________________________________________
    notifyLocationVariation notifier;
    public interface notifyLocationVariation {
		 void locationChange();
	 } 
    
//_________________________________________________________________________________________________________________________
    //On pressing Settings button will launch Settings Options
    public void showSettingsAlert(){
       AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
       alertDialog
       .setTitle("GPS is settings")
       .setMessage("GPS is not enabled. Do you want to go to settings menu?")
       .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,int which) {
               Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
               ctx.startActivity(intent);
           }
       })
       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
        	   dialog.cancel();
           }
       })
       .show();
   }
 
}