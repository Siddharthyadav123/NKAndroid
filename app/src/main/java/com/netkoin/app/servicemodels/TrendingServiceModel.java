package com.netkoin.app.servicemodels;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.Ads;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;
import com.netkoin.app.volly.ErrorResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashishkumarpatel on 1/11/2017.
 */
public class TrendingServiceModel extends BaseServiceModel {

    private ArrayList<Ads> trendingAds = new ArrayList<>();

    public TrendingServiceModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }

    public void loadTreandingAds() {
        //take selected user location if any selected
        double latitude = sharedPref.getFloat(SharedPref.KEY_SELECTED_LOC_LAT);
        double longitude = sharedPref.getFloat(SharedPref.KEY_SELECTED_LOC_LONG);
        int distance = sharedPref.getSettingDistanceByKey(SharedPref.KEY_SETTING_DIS_NEAR_BY_TRENDING_ADS);

        if (latitude == 0.0f) {
            //take from local class
            latitude = MyApplication.getInstance().getLocationModel().getLatitude();
            longitude = MyApplication.getInstance().getLocationModel().getLongitude();

            //take previously saved location
            if (latitude == 0.0f) {
                latitude = sharedPref.getFloat(SharedPref.KEY_SELF_LOC_LAT);
                longitude = sharedPref.getFloat(SharedPref.KEY_SELF_LOC_LONG);
            }

            //if still not found then hardcoding to nagpur
            if (latitude == 0.0f) {
//                latitude = 21.1458;
//                longitude = 79.0882;
                Toast.makeText(context, "Lat long not found.", Toast.LENGTH_SHORT).show();
            }

        }


        String url = URLConstants.URL_TREANDING_ADS + "?limit=" + this.limit
                + "&page=" + this.page + "&latitude=" + latitude + "&longitude="
                + longitude + "&distance=" + distance;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_TRENDING_ADS,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }

    public void loadNationWideTrending() {
        //take selected user location if any selected
//        double latitude = sharedPref.getFloat(SharedPref.KEY_SELECTED_LOC_LAT);
//        double longitude = sharedPref.getFloat(SharedPref.KEY_SELECTED_LOC_LONG);
//        int distance = sharedPref.getSettingDistanceByKey(SharedPref.KEY_SETTING_DIS_NEAR_BY_TRENDING_ADS);
//
//        if (latitude == 0.0f) {
//            //take from local class
//            latitude = MyApplication.getInstance().getLocationModel().getLatitude();
//            longitude = MyApplication.getInstance().getLocationModel().getLongitude();
//
//            //take previously saved location
//            if (latitude == 0.0f) {
//                latitude = sharedPref.getFloat(SharedPref.KEY_SELF_LOC_LAT);
//                longitude = sharedPref.getFloat(SharedPref.KEY_SELF_LOC_LONG);
//            }
//
//
//            //if still not found then hardcoding to nagpur
//            if (latitude == 0.0f) {
////                latitude = 21.1458;
////                longitude = 79.0882;
//                Toast.makeText(context, "Lat long not found.", Toast.LENGTH_SHORT).show();
//            }
//
//        }

        String url = URLConstants.URL_ADS + "?limit=" + this.limit
                + "&page=" + this.page + "&sort=trending_koins&direction=desc";

//        String url = URLConstants.URL_TREANDING_ADS + "?limit=" + this.limit
//                + "&page=" + this.page + "&latitude=" + latitude + "&longitude="
//                + longitude + "&distance=" + distance;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_TRENDING_ADS,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorResponse);
        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_TRENDING_ADS:
                    onGetTreandingResponse(isSuccess, result, errorResponse);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onGetTreandingResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            if (result != null && jsonObject.length() > 0) {
                JSONArray response = jsonObject.getJSONArray("data");

                trendingAds = gson.fromJson(response.toString(), new TypeToken<ArrayList<Ads>>() {
                }.getType());

                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_TRENDING_ADS,
                            true, result, errorResponse);
                }
            } else {
                if (apiCallback != null) {
                    errorResponse.setErrorString("No items are in trending.");
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_TRENDING_ADS,
                            false, result, errorResponse);
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_TRENDING_ADS,
                        false, result, errorResponse);
            }
        }
    }

    public ArrayList<Ads> getTrendingAds() {
        return trendingAds;
    }

    public void setTrendingAds(ArrayList<Ads> trendingAds) {
        this.trendingAds = trendingAds;
    }
}
