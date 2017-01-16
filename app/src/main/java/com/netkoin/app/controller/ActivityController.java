package com.netkoin.app.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.netkoin.app.screens.homescreen.home.activites.HomeScreenActivity;
import com.netkoin.app.screens.koin_managment.activites.KoinManagementHomeActivity;
import com.netkoin.app.screens.search_location.activity.SearchLocationActivity;
import com.netkoin.app.screens.signin.SignInActivity;
import com.netkoin.app.screens.signinoption.SignInOptionActivity;
import com.netkoin.app.screens.signup.SignUpActivity;

/**
 * This class will be used to launch the Activity
 * Created by siddharthyadav on 03/01/17.
 */

public class ActivityController {

    //parcel key to get the data in other activity
    public static final String KEY_PARCEL_EXTRAS = "extras";

    public static final int ACTIVITY_LOGIN_OPTIONS = 0;
    public static final int ACTIVITY_SIGN_IN = 1;
    public static final int ACTIVITY_SIGN_UP = 2;
    public static final int ACTIVITY_HOME_SCREEN = 3;
    public static final int ACTIVITY_KOIN_MANAGEMENT_HOME_SCREEN = 4;
    public static final int ACTIVITY_SEARCH_LOCATION = 5;

    public static ActivityController instance;


    public static ActivityController getInstance() {
        if (instance == null) {
            instance = new ActivityController();
        }
        return instance;
    }


    private Intent formActivityIntent(@NonNull Context context, int activityId) {
        Intent intent = null;
        switch (activityId) {
            case ACTIVITY_LOGIN_OPTIONS:
                intent = new Intent(context, SignInOptionActivity.class);
                break;
            case ACTIVITY_SIGN_IN:
                intent = new Intent(context, SignInActivity.class);
                break;
            case ACTIVITY_SIGN_UP:
                intent = new Intent(context, SignUpActivity.class);
                break;
            case ACTIVITY_HOME_SCREEN:
                intent = new Intent(context, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case ACTIVITY_KOIN_MANAGEMENT_HOME_SCREEN:
                intent = new Intent(context, KoinManagementHomeActivity.class);
                break;
            case ACTIVITY_SEARCH_LOCATION:
                intent = new Intent(context, SearchLocationActivity.class);
                break;

        }
        return intent;
    }


    public void handleEvent(@NonNull Context context, int activityId) {
        Intent intent = formActivityIntent(context, activityId);
        context.startActivity(intent);
    }


    public void handleEvent(@NonNull Context context, int activityId, String extras) {
        Intent intent = formActivityIntent(context, activityId);
        intent.putExtra(KEY_PARCEL_EXTRAS, extras);
        context.startActivity(intent);
    }

    public void handleEvent(@NonNull Context context, int activityId, int extras) {
        Intent intent = formActivityIntent(context, activityId);
        intent.putExtra(KEY_PARCEL_EXTRAS, extras);
        context.startActivity(intent);

    }

    public void handleEvent(@NonNull Context context, int activityId, @Nullable Parcelable extras) {
        Intent intent = formActivityIntent(context, activityId);
        intent.putExtra(KEY_PARCEL_EXTRAS, extras);
        context.startActivity(intent);
    }


}
