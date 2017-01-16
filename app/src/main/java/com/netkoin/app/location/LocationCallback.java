package com.netkoin.app.location;

/**
 * Created by siddharth on 12/23/2016.
 */
public interface LocationCallback {

    public void onLocationFound(float latitude, float longitude);

    public void onLocationChanged(float latitude, float longitude);
}
