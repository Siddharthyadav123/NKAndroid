package com.netkoin.app.screens.barcodescan;

/**
 * Created by siddharth on 1/24/2017.
 */
public interface BarCodeCallback {
    public void OnBarCodeScan(String rawValue, String barcodeValue);
}
