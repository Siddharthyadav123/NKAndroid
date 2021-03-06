package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.Ads;
import com.netkoin.app.entities.MainCategory;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;
import com.netkoin.app.volly.ErrorResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashishkumarpatel on 12/01/17.
 */

public class AdsServiceModel extends BaseServiceModel {

    private ArrayList<Ads> adsList = new ArrayList<>();

    public AdsServiceModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }

    public void loadAdsByStore(int storeId) {
        String url = URLConstants.URL_ADS + "?store_id=" + storeId + "&limit=" + limit + "&page=" + page;
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_ADS_BY_STORE_ID,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }

    public void loadAdsByCateogry(int catoegryId) {
        //take selected user location if any selected
        double latitude = sharedPref.getFloat(SharedPref.KEY_SELECTED_LOC_LAT);
        double longitude = sharedPref.getFloat(SharedPref.KEY_SELECTED_LOC_LONG);
        int distance = sharedPref.getSettingDistanceByKey(SharedPref.KEY_SETTING_DIS_CAT_ADS);

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
                latitude = 21.1458;
                longitude = 79.0882;
            }

        }

        String url = URLConstants.URL_TREANDING_ADS + "?category_id=" + catoegryId + "&limit=" + limit + "&page=" + page + "&latitude="
                + latitude + "&longitude=" + longitude + "&distance=" + distance;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_INDIVIDAUL_CATEGORY_DETAILS_BY_CAT_ID,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorResponse);

        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_ADS_BY_STORE_ID:
                    onAdsByStoreIdResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQUEST_ID_GET_INDIVIDAUL_CATEGORY_DETAILS_BY_CAT_ID:
                    onAdsByCategoryIdResponse(isSuccess, result, errorResponse);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onAdsByStoreIdResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            if (result != null && jsonObject.length() > 0) {

                JSONArray response = jsonObject.getJSONArray("data");
                adsList = gson.fromJson(response.toString(), new TypeToken<ArrayList<Ads>>() {
                }.getType());


                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ADS_BY_STORE_ID,
                            true, result, errorResponse);
                }

            } else {
                if (apiCallback != null) {
                    errorResponse.setErrorString("No featured item found.");
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ADS_BY_STORE_ID,
                            false, result, errorResponse);
                }
            }


        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ADS_BY_STORE_ID,
                        false, result, errorResponse);
            }

        }
    }


    private void onAdsByCategoryIdResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            if (result != null && jsonObject.length() > 0) {
                JSONArray response = jsonObject.getJSONArray("data");

                adsList = gson.fromJson(response.toString(), new TypeToken<ArrayList<Ads>>() {
                }.getType());

                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_INDIVIDAUL_CATEGORY_DETAILS_BY_CAT_ID,
                            true, result, errorResponse);
                }

            } else {
                if (apiCallback != null) {
                    errorResponse.setErrorString("No items found for this category.");
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_INDIVIDAUL_CATEGORY_DETAILS_BY_CAT_ID,
                            false, result, errorResponse);
                }
            }


        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_INDIVIDAUL_CATEGORY_DETAILS_BY_CAT_ID,
                        false, result, errorResponse);
            }
        }
    }

    public ArrayList<Ads> getAdsList() {
        return adsList;
    }

    public void setAdsList(ArrayList<Ads> adsList) {
        this.adsList = adsList;
    }
}
