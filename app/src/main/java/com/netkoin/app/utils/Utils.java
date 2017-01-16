package com.netkoin.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.netkoin.app.R;
import com.netkoin.app.application.MyApplication;

/**
 * Created by siddharth on 1/5/2017.
 */
public class Utils {
    public static Utils instance;

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    //this will return meters if distance is less than 1 KM
    public String refineDistanceText(String distance) {
        System.out.println(">>dis>>" + distance);
        double distanceVal = Double.parseDouble(distance);

        if (distanceVal < 1) {
            int distanceInMeter = (int) Math.floor((distanceVal * 1000));
            //setting meter with no value after point
            return distanceInMeter + " m";
        }
        //setting km with single digit
        return round(distanceVal, 1) + " Km";
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void openBrowser(Context context, String URL) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(URL));
        context.startActivity(i);
    }

    public void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Method responsible to Enable the GPS location
     */
    public void enableGPS(final Context context) {
        float sourceLat = MyApplication.getInstance().getLocationModel().getLatitude();
        float sourceLong = MyApplication.getInstance().getLocationModel().getLongitude();
        if (sourceLat == 0.0 && sourceLong == 0.0) {
            LocationManager manager = (LocationManager) MyApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getResources().getString(R.string.enableGPSText));  // GPS not found
                builder.setMessage(context.getResources().getString(R.string.enableGPSBody)); // Want to enable?
                builder.setPositiveButton(context.getResources().getString(R.string.settingsGPSText), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            }
        }

    }
}
