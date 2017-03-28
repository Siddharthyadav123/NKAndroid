package com.netkoin.app.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.netkoin.app.application.MyApplication;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.servicemodels.StoreServiceModel;
import com.netkoin.app.servicemodels.UserServiceModel;


/**
 * Class to fetch lat long
 */
public class LocationModel implements LocationListener {

    private Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    private float latitude; // latitude
    private float longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 2 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 20 sec

    // Declaring a Location Manager
    private LocationManager locationManager;
    private LocationCallback locationCallback;

    private long lastServerHit = System.currentTimeMillis();
    private final int SERVER_HIT_DELAY_IN_MILLIS = 1000 * 20;

    private SharedPref sharedPref;

    private StoreServiceModel storeServiceModel;
    private UserServiceModel userServiceModel;

    public LocationModel(Context context, LocationCallback locationCallback) {
        this.mContext = context;
        this.locationCallback = locationCallback;
        sharedPref = new SharedPref(context);
        storeServiceModel = new StoreServiceModel(mContext, null);
        userServiceModel = new UserServiceModel(mContext, null);
    }

    public void initialize() {
        getLocation();
    }

    /**
     * In this function we�ll get the location from network provider first. If
     * network provider is disabled, then we get the location from GPS provider.
     *
     * @return
     */
    private Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.


                        return null;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = (float) location.getLatitude();
                            longitude = (float) location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return null;
                        }
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {

                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = (float) location.getLatitude();
                                longitude = (float) location.getLongitude();
                            }
                        }

                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(">>gps lat>>" + latitude + "  >>long >> " + longitude);

        //callback for lat long
        if (locationCallback != null && latitude != 0.0f) {
            locationCallback.onLocationFound(latitude, longitude);
            saveLatLong();
            if (hitToServer()) {
                userServiceModel.updateUserLocation(latitude, longitude);
                storeServiceModel.checkNearByStore(latitude, longitude);
            }

        }
//        Toast.makeText(mContext, "Location found Lat>> " + latitude + " >>Long>> " + longitude, Toast.LENGTH_SHORT).show();
//        Utils.getInstance().showLocalNotification("Netkoin Location Found", "Lat:" + latitude + "\n Long:" + longitude);
        return location;
    }

    private boolean hitToServer() {
        //System.out.println("hitServer");
        if (System.currentTimeMillis() - lastServerHit > SERVER_HIT_DELAY_IN_MILLIS) {
            lastServerHit = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    private void saveLatLong() {

        //System.out.println(">>lat");

        sharedPref.put(SharedPref.KEY_SELF_LOC_LAT, new Double(latitude));
        sharedPref.put(SharedPref.KEY_SELF_LOC_LONG, new Double(longitude));
    }

    /**
     * Function to get latitude
     */
    public float getLatitude() {
//        if (location != null) {
//            latitude = (float) location.getLatitude();
//        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public float getLongitude() {
//        if (location != null) {
//            longitude = (float) location.getLongitude();
//        }

        // return longitude
        return longitude;
    }


    public long getLastServerHit() {
        return lastServerHit;
    }

    public void setLastServerHit(long lastServerHit) {
        this.lastServerHit = lastServerHit;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();



            //callback for lat long
            if (locationCallback != null && latitude != 0.0f) {
                //System.out.println("location>>onLocationChanged >> " + MyApplication.getInstance().getLocationModel());
                //System.out.println("location>>onLocationChanged this >> " + this);
                //System.out.println("location>>loadStores >> getLatitude" + latitude);
                //System.out.println("location>>loadStores >> getLongitude" + longitude);
                locationCallback.onLocationChanged(latitude, longitude);
                saveLatLong();
                if (hitToServer()) {
                   // Toast.makeText(mContext, "Location change Lat>> " + latitude + " >>Long>> " + longitude, Toast.LENGTH_SHORT).show();
                    userServiceModel.updateUserLocation(latitude, longitude);
                    storeServiceModel.checkNearByStore(latitude, longitude);
                }
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
