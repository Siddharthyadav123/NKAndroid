package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.constants.Constants;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.Store;
import com.netkoin.app.entities.StoreFeaturedBanner;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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


        String url = URLConstants.URL_FIND_STORES + "?limit=" + this.limit
                + "&page=" + this.page + "&latitude=" + latitude + "&longitude="
                + longitude + "&distance=" + distance;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_STORES,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }


    public void checkNearByStore(double latitude, double longitude) {
        if (nearByRequestInProgress) {
            return;
        }
        nearByRequestInProgress = true;
        //setting distance to find out for 1KM stores
        int distance = 2;

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
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorString);
        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_HOME_BANNER:
                    onStoreHomeFeaturedBannerResponse(isSuccess, result, errorString);
                    break;
                case RequestConstants.REQUEST_ID_GET_STORES:
                    onloadStoreResponse(isSuccess, result, errorString);
                    break;
                case RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS:
                    onStoreFeaturedBannerResponse(isSuccess, result, errorString);
                    break;
                case RequestConstants.REQUEST_ID_GET_NEARBY_STORE:
                    onNearByStoreResponse(isSuccess, result, errorString);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onStoreHomeFeaturedBannerResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            if (result != null && jsonObject.length() > 0) {

                JSONArray response = jsonObject.getJSONArray("data");

                storesFeaturedBanner = gson.fromJson(response.toString(), new TypeToken<ArrayList<StoreFeaturedBanner>>() {
                }.getType());

                if (storesFeaturedBanner.size() == 0) {
                    if (apiCallback != null) {
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_HOME_BANNER,
                                false, result, "Featured banner not found");
                    }
                } else {
                    if (apiCallback != null) {
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_HOME_BANNER,
                                true, result, "");
                    }
                }
            } else {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_HOME_BANNER,
                            false, result, "Featured banner not found");
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_HOME_BANNER,
                        false, result, errorString);
            }
        }

    }

    private void onloadStoreResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            if (result != null && jsonObject.length() > 0) {

                JSONArray response = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                stores = gson.fromJson(response.toString(), new TypeToken<ArrayList<Store>>() {
                }.getType());


                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORES,
                            true, result, "");
                }

            } else {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORES,
                            false, result, "Stores not found");
                }
            }

        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORES,
                        false, result, errorString);
            }

        }

    }

    private void onStoreFeaturedBannerResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            if (result != null && jsonObject.length() > 0) {

                JSONArray response = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                storesProfileFeaturedBanner = gson.fromJson(response.toString(), new TypeToken<ArrayList<StoreFeaturedBanner>>() {
                }.getType());


                if (storesProfileFeaturedBanner.size() == 0) {
                    if (apiCallback != null) {
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS,
                                false, result, "Featured banner not found");
                    }
                } else {
                    if (apiCallback != null) {
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS,
                                true, result, "");
                    }
                }
            } else {

                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS,
                            false, result, "Featured banner not found");
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS,
                        false, result, errorString);
            }
        }
    }

    private void onNearByStoreResponse(boolean isSuccess, Object result, String
            errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;

            if (result != null && jsonObject.length() > 0) {
                JSONArray response = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                stores = gson.fromJson(response.toString(), new TypeToken<ArrayList<Store>>() {
                }.getType());


                Store nearByStore = stores.get(0);
                float distanceKM = Float.parseFloat(nearByStore.getDistance());
                int distanceInMeter = (int) (distanceKM * 1000);


                //check distance and check the same entry for the same shop id exist
                if (distanceInMeter <= Constants.NEAR_BY_STORE_DISTANCE_IN_METER) {
//                    if (!checkIfNotificationAlreadyGeneratedForNearbyStore(nearByStore)) {
//                        let notificationSettings = UIUserNotificationSettings(forTypes:[.Alert,.
//                        Badge,.Sound],categories:
//                        nil)
//                        UIApplication.sharedApplication().registerUserNotificationSettings(notificationSettings)
//
//                        let notification = UILocalNotification()
////                        notification.fireDate = NSDate(timeIntervalSinceNow: 1)
//                        notification.alertBody = "Hey you are near to " + nearByStore.name
//                        !+" store. Lets visit and earn !!";
//                        notification.alertAction = "NetKoin"
//                        notification.soundName = UILocalNotificationDefaultSoundName
//                        notification.userInfo =["CustomField1":"w00t"]
//                        UIApplication.sharedApplication().scheduleLocalNotification(notification)
//
//                        //showing heads up notification
//                        let view = MessageView.viewFromNib(layout:.CardView)
//                        view.configureDropShadow()
//                        view.button ?.hidden = true;
//                        // Theme message elements with the warning style.
//                        view.configureTheme(.Success)
//                        view.configureContent(title:"Notification", body:
//                        "Hey you are near to " + nearByStore.name !+" store. Lets visit and earn !!"
//                        as String, iconText:"")
//                        SwiftMessages.show(view:view)
//
//                    }
                }


            }
        }
        nearByRequestInProgress = false;
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
