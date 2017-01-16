package com.netkoin.app.screens.barcodescan;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCodeScanActivity extends Activity implements ZXingScannerView.ResultHandler {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        if (state != null) {
//            mFlash = state.getBoolean(FLASH_STATE, false);
//            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
//            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
//            mFlash = false;
//            mAutoFocus = true;
            mSelectedIndices = null;
//            mCameraId = -1;
        }


        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setupFormats();
        setContentView(mScannerView);                // Set the scanner view as the content view

    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }


    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Toast.makeText(BarCodeScanActivity.this, "Format >>" + rawResult.getBarcodeFormat().toString() + "\n Result: " + rawResult.getText(), Toast.LENGTH_LONG).show();

        finish();
        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
//        mScannerView.setFlash(mFlash);
//        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean(FLASH_STATE, mFlash);
//        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
//        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
//        closeMessageDialog();
//        closeFormatsDialog();
    }
}
