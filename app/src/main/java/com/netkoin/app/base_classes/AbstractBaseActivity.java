package com.netkoin.app.base_classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.utils.Utils;
import com.netkoin.app.volly.APIHandlerCallback;

/**
 * Created by siddharth on 1/4/2017.
 */
public abstract class AbstractBaseActivity extends AppCompatActivity implements APIHandlerCallback, View.OnClickListener {
    protected ImageView leftBtnImageView;
    protected TextView titleTextView;
    protected ProgressBar progressBar;
    protected SharedPref sharedPref;

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
}
