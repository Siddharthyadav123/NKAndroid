package com.netkoin.app.controller;

import android.app.Activity;

import com.netkoin.app.models.ModelFacade;
import com.netkoin.app.pref.SharedPref;

/**
 * Created by ashishkumarpatel on 02/01/17.
 */

public class AppController {
    public static AppController instance;

    private ModelFacade modelFacade;

    private AppController() {
        modelFacade = new ModelFacade();
    }

    public static AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    public ModelFacade getModelFacade() {
        return modelFacade;
    }

    public void logoutApp(Activity activity) {
        //keeping push token.. before logging out.. because clearing shared prefernce clears the token from lib too
//        if(modelFacade.getLocalModel().getPushToken() ==  null)
//        {
//            modelFacade.localModel.pushToken = FIRInstanceID.instanceID().token()!
//        }

        modelFacade.getLocalModel().setToken(null);
        new SharedPref(activity).clearSharedPref();
        activity.finish();
        ActivityController.getInstance().handleEvent(activity, ActivityController.ACTIVITY_LOGIN_OPTIONS);
    }
}
