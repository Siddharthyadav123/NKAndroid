package com.netkoin.app.screens.barcodescan;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseActivity;
import com.netkoin.app.controller.ActivityController;
import com.netkoin.app.servicemodels.UserServiceModel;
import com.netkoin.app.utils.Utils;
import com.netkoin.app.volly.APIHandlerCallback;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


public class BarCodeScanActivity extends AbstractBaseActivity implements APIHandlerCallback {

    public static final int SCAN_MODE_PURCHASE = 0;
    public static final int SCAN_MODE_PRODUCT = 1;

    private View mainView;

    private BarCodeScanParcelDo barCodeScanParcelDo;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private TextView rescanBtnTextView;
    private TextView cancelBtnTextView;

    private ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }


    @Override
    public View initView() {
        barCodeScanParcelDo = getIntent().getParcelableExtra(ActivityController.KEY_PARCEL_EXTRAS);
        mainView = getLayoutInflater().inflate(R.layout.activity_bar_code_scan, null);

        boolean hasCameraPermission = checkPermission(REQUEST_MARSHMELLO_PERMISSIONS, mustPermissions[1], null);
        if (hasCameraPermission) {
            initControls();
        }
        return mainView;
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void updateUI() {

    }

    @Override
    public void onActionBarLeftBtnClick() {

    }

    @Override
    public void onActionBarTitleClick() {

    }

    @Override
    public void onPermissionResult(int requestCode, boolean isGranted, Object extras) {
        initControls();
    }

    private void initControls() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(BarCodeScanActivity.this, mCamera, previewCb,
                autoFocusCB);
        FrameLayout preview = (FrameLayout) mainView.findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        rescanBtnTextView = (TextView) mainView.findViewById(R.id.rescanBtnTextView);
        cancelBtnTextView = (TextView) mainView.findViewById(R.id.cancelBtnTextView);

        rescanBtnTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });

        cancelBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {

                    Log.i("<<<<<<Asset Code>>>>> ",
                            "<<<<Bar Code>>> " + sym.getData());
                    String scanResult = sym.getData().trim();

                    Toast.makeText(BarCodeScanActivity.this, scanResult, Toast.LENGTH_SHORT).show();
                    onBarcodeScanResultFound(scanResult);
                    barcodeScanned = true;


                    break;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private void onBarcodeScanResultFound(String scanResult) {
        try {
            barCodeScanParcelDo.setBarCodeValue(Long.parseLong(scanResult));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(BarCodeScanActivity.this, "Barcode id not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserServiceModel userServiceModel = new UserServiceModel(this, this);
        switch (barCodeScanParcelDo.getScanMode()) {
            case SCAN_MODE_PURCHASE:
                userServiceModel.checkInPurchases(barCodeScanParcelDo);
                break;
            case SCAN_MODE_PRODUCT:
                userServiceModel.checkInProducts(barCodeScanParcelDo);
                break;
        }
    }


    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        Utils.getInstance().showSnackBar(this, errorString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    @Override
    public void onClick(View view) {

    }
}
