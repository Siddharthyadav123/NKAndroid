package com.netkoin.app.screens.splash;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseActivity;
import com.netkoin.app.controller.ActivityController;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.servicemodels.LoginFlowServiceModel;
import com.netkoin.app.volly.ErrorResponse;

public class SplashActivity extends AbstractBaseActivity {

    private RelativeLayout splashRelLayout;

    @Override
    public View initView() {
        View view = getLayoutInflater().inflate(R.layout.activity_splash, null);
        splashRelLayout = (RelativeLayout) view.findViewById(R.id.splashRelLayout);
        return view;
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void updateUI() {
        animateSplashLayout();

        //lauching next screen based on below conditions
        String authToken = sharedPref.getString(SharedPref.KEY_AUTH_TOKEN);
        String previousRequestParamJsonbody = sharedPref.getString(SharedPref.KEY_SILENT_LOGIN_REQUEST_PARAM);

        if (authToken != null && previousRequestParamJsonbody != null) {
            int previousSilentLoginRequestId = sharedPref.getInt(SharedPref.KEY_SILENT_LOGIN_TYPE);
            LoginFlowServiceModel loginFlowModel = new LoginFlowServiceModel(this, this);
            loginFlowModel.performSilentLogin(previousSilentLoginRequestId, previousRequestParamJsonbody);

//            finish();
//            AppController.getInstance().getModelFacade().getLocalModel().setToken(authToken);
//            ActivityController.getInstance().handleEvent(this, ActivityController.ACTIVITY_HOME_SCREEN);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    ActivityController.getInstance().handleEvent(SplashActivity.this, ActivityController.ACTIVITY_LOGIN_OPTIONS);
                }
            }, 2000);
        }
    }

    @Override
    public void onActionBarLeftBtnClick() {

    }

    @Override
    public void onActionBarTitleClick() {

    }

    @Override
    public void onPermissionResult(int requestCode, boolean isGranted, Object extras) {

    }

    private void animateSplashLayout() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation fadeOut = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_out);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animateSplashLayout();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                splashRelLayout.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splashRelLayout.startAnimation(fadeIn);
    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        if (isSuccess) {
            finish();
            ActivityController.getInstance().handleEvent(this, ActivityController.ACTIVITY_HOME_SCREEN);
        } else {
            showAleartPosBtnOnly(null, "Message", errorResponse.getErrorString());
        }
    }

    @Override
    public void onClick(View view) {

    }
}
