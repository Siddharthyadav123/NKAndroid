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

import com.netkoin.app.pref.SharedPref;


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
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 2; // 2 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 10 sec

    // Declaring a Location Manager
    private LocationManager locationManager;
    private LocationCallback locationCallback;

    private SharedPref sharedPref;

    public LocationModel(Context context, LocationCallback locationCallback) {
        this.mContext = context;
        this.locationCallback = locationCallback;
        sharedPref = new SharedPref(context);
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
        System.out.println(">>gps lat>>" + latitude + "  >>long >> " + longitude);

        //callback for lat long
        if (locationCallback != null && latitude != 0.0f) {
            locationCallback.onLocationFound(latitude, longitude);
            saveLatLong();
        }
        Toast.makeText(mContext, "Lat>> " + latitude + " >>Long>> " + longitude, Toast.LENGTH_SHORT).show();
        return location;
    }

    private void saveLatLong() {
        sharedPref.put(SharedPref.KEY_SELF_LOC_LAT, new Double(latitude));
        sharedPref.put(SharedPref.KEY_SELF_LOC_LONG, new Double(longitude));
    }

    /**
     * Function to get latitude
     */
    public float getLatitude() {
        if (location != null) {
            latitude = (float) location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public float getLongitude() {
        if (location != null) {
            longitude = (float) location.getLongitude();
        }

        // return longitude
        return longitude;
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();

            Toast.makeText(mContext, "Lat>> " + latitude + " >>Long>> " + longitude, Toast.LENGTH_SHORT).show();

            //callback for lat long
            if (locationCallback != null && latitude != 0.0f) {
                locationCallback.onLocationChanged(latitude, longitude);
                saveLatLong();
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

//    public void findMyAddress(Context context, AddressCallback addressCallback) {
//        this.addressCallback = addressCallback;
//        String strAdd = "";
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
//                }
//                strAdd = strReturnedAddress.toString();
//            }
//
//            if (addressCallback != null) {
//                addressCallback.onAddressResult(true, strAdd);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            addressFromAPI(context);
//        }
//
//    }

//    public void addressFromAPI(Context context) {
//        try {
//            String address = String.format(Locale.ENGLISH, "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
//                    + Locale.getDefault().getCountry(), latitude, longitude);
//
//            //requesting
//            APIHandler apiHandler = new APIHandler(context, this, RequestConstant.REQUEST_GET_ADDRESS,
//                    Request.Method.GET, address, true, context.getResources().getString(R.string.fetchingAddressText), null, null,
//                    null);
//
//            apiHandler.requestAPI();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onAPISuccessResponse(int requestId, String responseString) {
//        try {
//            String address = "";
//            JSONObject jsonObject = new JSONObject(responseString.toString());
//
//            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
//                JSONArray results = jsonObject.getJSONArray("results");
//                for (int i = 0; i < results.length(); i++) {
//                    JSONObject result = results.getJSONObject(i);
//                    address = result.getString("formatted_address");
//                    break;
//                }
//            }
//            if (addressCallback != null) {
//                addressCallback.onAddressResult(true, address);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (addressCallback != null) {
//            addressCallback.onAddressResult(false, mContext.getResources().getString(R.string.addressNotFoundText));
//        }
//    }
//
//    @Override
//    public void onAPIFailureResponse(int requestId, String errorString) {
//
//    }
//
//    public interface AddressCallback {
//        public void onAddressResult(boolean isFound, String addressOrError);
//    }


}
