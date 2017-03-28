package com.netkoin.app.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.netkoin.app.controller.AppController;


/**
 * GPS broadcast receiver to get the callback if location selected...
 * Adding Recursion because it GPS take little time to start providing lat long once started
 * <p>
 * Created by ashishkumarpatel
 */
public class GpsLocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                startCheckingForLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startCheckingForLocation() {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LocationModel locationModel = AppController.getInstance().getModelFacade().getLocalModel().getLocationModel();
                    //fetching location
                    locationModel.initialize();
                    if (locationModel.getLongitude() == 0.0 && locationModel.getLatitude() == 0.0) {
                        startCheckingForLocation();
                    }
                }
            }, 1200);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
