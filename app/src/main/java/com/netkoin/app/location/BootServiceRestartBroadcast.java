package com.netkoin.app.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.netkoin.app.utils.Utils;

public class BootServiceRestartBroadcast extends BroadcastReceiver {
    public static String TAG = "BootServiceRestartBroadcast";

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                startLocationService(context);
            }
        }
    }

    private void startLocationService(Context context) {
        if (!Utils.getInstance().isServiceRunning(NKForeverLocationService.class, context)) {
            Log.d(TAG, "Service>> Service started from braodcast");
            Intent intent = new Intent(context, NKForeverLocationService.class);
            context.startService(intent);
        } else {
            Log.d(TAG, "Service>> Service already running checked from Broadcast");
        }
    }


}
