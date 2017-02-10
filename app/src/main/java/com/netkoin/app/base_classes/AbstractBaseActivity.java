package com.netkoin.app.base_classes;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.netkoin.app.R;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.location.NKForeverLocationService;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.utils.Utils;
import com.netkoin.app.volly.APIHandlerCallback;

import java.util.ArrayList;

/**
 * Created by siddharth on 1/4/2017.
 */
public abstract class AbstractBaseActivity extends AppCompatActivity implements APIHandlerCallback, View.OnClickListener {
    protected ImageView leftBtnImageView;
    protected TextView titleTextView;
    protected ProgressBar progressBar;
    protected SharedPref sharedPref;

    protected boolean haveAllPermissions = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        sharedPref = new SharedPref(this);

        setContentView(initView());
//        initActionBarView();
        registerEvents();
        updateUI();
    }

    protected void initActionBarView(View view) {

        leftBtnImageView = (ImageView) view.findViewById(R.id.leftBtnImageView);
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        leftBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionBarLeftBtnClick();
            }
        });

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionBarTitleClick();
            }
        });
    }

    public void setActionBarTitle(String title) {
        titleTextView.setText(title);
    }

    public abstract View initView();

    public abstract void registerEvents();

    public abstract void updateUI();

    public abstract void onActionBarLeftBtnClick();

    public abstract void onActionBarTitleClick();

    protected void showAleartPosBtnOnly(final MyApplication.DailogCallback dailogCallback,
                                        String title, String bodyText) {
        Utils.getInstance().hideKeyboard(this);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(bodyText);
        builder1.setTitle(title);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (dailogCallback != null)
                            dailogCallback.onDailogYesClick();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    protected void permissionDailog(Context context,
                                    final MyApplication.DailogCallback dailogCallback,
                                    String title, String bodyText, String yesBtnText, String noBtnText) {
        Utils.getInstance().hideKeyboard(this);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(bodyText);
        builder1.setTitle(title);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                yesBtnText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (dailogCallback != null)
                            dailogCallback.onDailogYesClick();
                    }
                });

        builder1.setNegativeButton(
                noBtnText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (dailogCallback != null)
                            dailogCallback.onDailogNoClick();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    protected void showAleartPosAndNegBtn(Context context,
                                          final MyApplication.DailogCallback dailogCallback,
                                          String title, String bodyText, String yesBtnText, String noBtnText) {
        Utils.getInstance().hideKeyboard(this);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(bodyText);
        builder1.setTitle(title);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                yesBtnText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (dailogCallback != null)
                            dailogCallback.onDailogYesClick();
                    }
                });

        builder1.setNegativeButton(
                noBtnText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (dailogCallback != null)
                            dailogCallback.onDailogNoClick();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    protected final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /**
     * -------------------------------------- Permission handling --------------------------------------
     */
    private int permissionRequestCode;
    private Object extras;
    public final int REQUEST_MARSHMELLO_PERMISSIONS = 10001;

    public String[] mustPermissions =
            {
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED

            };

    private boolean havePermission(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public boolean checkPermission(int requestCode, String permission, Object extras) {
        this.permissionRequestCode = requestCode;
        this.extras = extras;
        //if we have permission then will procceed
        if (havePermission(permission)) {
            return true;
        }
        //else we will take the permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(this, "Please provide this permission.", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

        return false;
    }

    public boolean checkPermissions(int requestCode, String[] permission, Object extras) {
        this.permissionRequestCode = requestCode;
        this.extras = extras;
        ArrayList<String> permissionsStr = new ArrayList<>();

        for (int i = 0; i < permission.length; i++) {
            if (!havePermission(permission[i])) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission[i])) {
                    Toast.makeText(this, "Please provide this permission.", Toast.LENGTH_LONG).show();
                }
                permissionsStr.add(permission[i]);
            }
        }


        if (permissionsStr.size() > 0) {
            String[] needTopermissionArray = permissionsStr.toArray(new String[permissionsStr.size()]);
            ActivityCompat.requestPermissions(this, needTopermissionArray, requestCode);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //System.out.println(">>>>>>>>>>>>>>>>> onRequestPermissionsResult " + requestCode + " grantResults " + grantResults.length);
        if (requestCode == permissionRequestCode) {

            if (grantResults.length >= 1) {
                boolean anyDenied = false;
                for (int i = 0; i < grantResults.length; i++) {
                    //System.out.println(">>>>>>>>>>>>>>>> grantResults " + grantResults[i]);
                    // Check if the only required permission has been granted
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "PERMISSION OF " + permissions[i] + " IS GRANTED.", Toast.LENGTH_SHORT).show();

                        // need to refresh location on permission
                        if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) ||
                                permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            NKForeverLocationService.getInstance().getLocationModel().initialize();
                        }

                    } else {
                        anyDenied = true;
//                        Toast.makeText(this, "PERMISSION OF " + permissions[i] + " IS DENIED.", Toast.LENGTH_SHORT).show();
                    }
                }
                if (anyDenied) {
                    haveAllPermissions = false;
                    onPermissionResult(requestCode, false, extras);
                } else {
                    haveAllPermissions = true;
                    onPermissionResult(requestCode, true, extras);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public abstract void onPermissionResult(int requestCode, boolean isGranted, Object extras);

}
