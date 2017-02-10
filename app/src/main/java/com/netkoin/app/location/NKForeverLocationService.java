package com.netkoin.app.location;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.netkoin.app.controller.AppController;

public class NKForeverLocationService extends Service {
    private static final String TAG = "LocationService";
    public static boolean isServiceRunning = false;
    private LocationModel locationModel = null;
    private Handler timerHandler = null;
    public static NKForeverLocationService nkForeverLocationService;

    public static NKForeverLocationService getInstance() {
        return nkForeverLocationService;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        nkForeverLocationService = this;
        isServiceRunning = true;
        Toast.makeText(NKForeverLocationService.this, "Service Started.", Toast.LENGTH_SHORT).show();
        initLocation();
        repeatLocationRequest();
    }

    private void initLocation() {
        locationModel = new LocationModel(this, null);
        locationModel.initialize();
        AppController.getInstance().getModelFacade().getLocalModel().setLocationModel(locationModel);
    }

    private void repeatLocationRequest() {
        timerHandler = new Handler();
        timerHandler.postDelayed(locationRunnable, 5000);
    }

    private Runnable locationRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Service>> Service Running");
            locationModel.initialize();
            repeatLocationRequest();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        nkForeverLocationService = null;
        isServiceRunning = false;
        timerHandler.removeCallbacks(locationRunnable);
        Toast.makeText(NKForeverLocationService.this, "Service Destroyed.", Toast.LENGTH_SHORT).show();
    }

    public LocationModel getLocationModel() {
        return locationModel;
    }
}

