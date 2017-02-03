package com.netkoin.app.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootServiceRestartBroadcast extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, LocationService.class);
        context.startService(startServiceIntent);
    }
}
