package com.netkoin.app.servicemodels;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.constants.Constants;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.controller.AppController;
import com.netkoin.app.entities.Store;
import com.netkoin.app.entities.StoreFeaturedBanner;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.utils.Utils;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;
import com.netkoin.app.volly.ErrorResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by siddharth on 1/9/2017.
 */
public class StoreServiceModel extends BaseServiceModel {

    private ArrayList<Store> stores = new ArrayList<>();
    private ArrayList<StoreFeaturedBanner> storesFeaturedBanner = new ArrayList<>();

    //store profile featured banners for individual store
    private ArrayList<StoreFeaturedBanner> storesProfileFeaturedBanner = new ArrayList<>();

    boolean nearByRequestInProgress = false;

    public StoreServiceModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }

    public void loadStoreFeaturedBanner() {
        String url = URLConstants.URL_HOME_FEATURED_BANNER;
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_HOME_BANNER,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }

    public void loadStores() {
        //take selected user location if any selected
        double latitude = sharedPref.getFloat(SharedPref.KEY_SELECTED_LOC_LAT);
        double longitude = sharedPref.getFloat(SharedPref.KEY_SELECTED_LOC_LONG);
        int distance = sharedPref.getSettingDistanceByKey(SharedPref.KEY_SETTING_DIS_NEAR_BY_STORES);

        if (latitude == 0.0f) {
            //take from local class
            latitude = AppController.getInstance().getModelFacade().getLocalModel().getLocationModel().getLatitude();
            longitude = AppController.getInstance().getModelFacade().getLocalModel().getLocationModel().getLongitude();

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


        String url = URLConstants.URL_FIND_STORES + "?limit=" + this.limit
                + "&page=" + this.page + "&latitude=" + latitude + "&longitude="
                + longitude + "&distance=" + distance;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_STORES,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }


    public void checkNearByStore(double latitude, double longitude) {
        if (AppController.getInstance().getModelFacade().getLocalModel().getToken() == null) {
            return;
        }
        //looking for nearby store
        if (!sharedPref.getBoolean(SharedPref.KEY_SETTING_NOTI_NEAR_BY_STORES)) {
            return;
        }
        if (nearByRequestInProgress) {
            return;
        }

        //Utils.getInstance().showLocalNotification("Test", "Going to check Nearby store>> " + latitude + " >>Long>> " + longitude);

        nearByRequestInProgress = true;
        //setting distance to find out for 1KM stores
        int distance = 3;

        if (latitude == 0.0f) {
            //take from local class
            latitude = AppController.getInstance().getModelFacade().getLocalModel().getLocationModel().getLatitude();
            longitude = AppController.getInstance().getModelFacade().getLocalModel().getLocationModel().getLongitude();

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

        String url = URLConstants.URL_FIND_STORES + "?limit=" + 1 + "&page=" + 1 + "&latitude=" + latitude
                + "&longitude=" + longitude + "&distance=" + distance;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_NEARBY_STORE,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();

    }

    public void loadStoreProfileFeaturedBanner(int selectedStoreId) {
        String url = URLConstants.URL_STORE_FEATURED_BANNER + "?store_id=" + selectedStoreId;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorResponse);
        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_HOME_BANNER:
                    onStoreHomeFeaturedBannerResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQUEST_ID_GET_STORES:
                    onloadStoreResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS:
                    onStoreFeaturedBannerResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQUEST_ID_GET_NEARBY_STORE:
                    onNearByStoreResponse(isSuccess, result, errorResponse);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onStoreHomeFeaturedBannerResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            if (result != null && jsonObject.length() > 0) {

                JSONArray response = jsonObject.getJSONArray("data");

                storesFeaturedBanner = gson.fromJson(response.toString(), new TypeToken<ArrayList<StoreFeaturedBanner>>() {
                }.getType());

                if (storesFeaturedBanner.size() == 0) {
                    if (apiCallback != null) {
                        errorResponse.setErrorString("Featured banner not found");
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_HOME_BANNER,
                                false, result, errorResponse);
                    }
                } else {
                    if (apiCallback != null) {
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_HOME_BANNER,
                                true, result, errorResponse);
                    }
                }
            } else {
                if (apiCallback != null) {
                    errorResponse.setErrorString("Featured banner not found");
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_HOME_BANNER,
                            false, result, errorResponse);
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_HOME_BANNER,
                        false, result, errorResponse);
            }
        }

    }

    private void onloadStoreResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            if (result != null && jsonObject.length() > 0) {

                JSONArray response = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                stores = gson.fromJson(response.toString(), new TypeToken<ArrayList<Store>>() {
                }.getType());


                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORES,
                            true, result, errorResponse);
                }

            } else {
                if (apiCallback != null) {
                    errorResponse.setErrorString("Stores not found");
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORES,
                            false, result, errorResponse);
                }
            }

        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORES,
                        false, result, errorResponse);
            }

        }

    }

    private void onStoreFeaturedBannerResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            if (result != null && jsonObject.length() > 0) {

                JSONArray response = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                storesProfileFeaturedBanner = gson.fromJson(response.toString(), new TypeToken<ArrayList<StoreFeaturedBanner>>() {
                }.getType());


                if (storesProfileFeaturedBanner.size() == 0) {
                    if (apiCallback != null) {
                        errorResponse.setErrorString("Featured banner not found");
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS,
                                false, result, errorResponse);
                    }
                } else {
                    if (apiCallback != null) {
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS,
                                true, result, errorResponse);
                    }
                }
            } else {

                if (apiCallback != null) {
                    errorResponse.setErrorString("Featured banner not found");
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS,
                            false, result, errorResponse);
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS,
                        false, result, errorResponse);
            }
        }
    }

    private void onNearByStoreResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;

            if (result != null && jsonObject.length() > 0) {
                JSONArray response = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                ArrayList<Store> stores = gson.fromJson(response.toString(), new TypeToken<ArrayList<Store>>() {
                }.getType());

                if (stores == null || stores.size() == 0) {
                    return;
                }

                Store nearByStore = stores.get(0);
                float distanceKM = Float.parseFloat(nearByStore.getDistance());
                int distanceInMeter = (int) (distanceKM * 1000);

                //check distance and check the same entry for the same shop id exist
                if (distanceInMeter <= Constants.NEAR_BY_STORE_DISTANCE_IN_METER) {
                    if (!checkIfNotificationAlreadyGeneratedForNearbyStore(nearByStore)) {
                        if (MyApplication.getInstance().getHomeActivity() != null) {
                            Utils.getInstance().showSnackBar(true, MyApplication.getInstance().getHomeActivity(), "Hey! You are near " + nearByStore.getName() + ". Step-in to collect koins!");
                        } else {
                            Utils.getInstance().showLocalNotification("NetKoin", "Hey! You are near " + nearByStore.getName() + ". Step-in to collect koins!");

                        }
                    }
                }

            }
        }
        nearByRequestInProgress = false;
    }

    private boolean checkIfNotificationAlreadyGeneratedForNearbyStore(Store nearByStore) {
        //finding today's date
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        String todaysDate = day + "-" + month + "-" + year;

        HashMap<String, String> storeIdDictionary = (HashMap<String, String>) sharedPref.getMap(SharedPref.KEY_DICTIONARY_NEAR_BY_STORE);
        if (storeIdDictionary != null && storeIdDictionary.size() > 0) {
            String dateTime = storeIdDictionary.get(nearByStore.getId() + "");

            if (dateTime != null) {
                if (dateTime.equals(todaysDate)) {
                    //do nothing if store id found with the same date
                    //System.out.println("This store already notfied today");
                    return true;
                }
            }
        }
        storeIdDictionary.put(nearByStore.getId() + "", todaysDate);
        sharedPref.putMap(storeIdDictionary, SharedPref.KEY_DICTIONARY_NEAR_BY_STORE);
        return false;

    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public ArrayList<StoreFeaturedBanner> getStoresFeaturedBanner() {
        return storesFeaturedBanner;
    }

    public ArrayList<StoreFeaturedBanner> getStoresProfileFeaturedBanner() {
        return storesProfileFeaturedBanner;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public boolean isNearByRequestInProgress() {
        return nearByRequestInProgress;
    }
}
