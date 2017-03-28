package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.controller.AppController;
import com.netkoin.app.screens.barcodescan.BarCodeScanParcelDo;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;
import com.netkoin.app.volly.ErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ashishkumarpatel on 15/01/17.
 */

public class UserServiceModel extends BaseServiceModel {
    public UserServiceModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }

    public void redeemKoin(int store_id, int catalogue_id) {
        String requestBody = formJsonToRedeemKoin(store_id, catalogue_id).toString();
        String url = URLConstants.URL_REDEEM_KOIN;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQIEST_ID_REDEEM_KOIN,
                Request.Method.POST, url, true, "Please wait...", requestBody);
        apiHandler.requestAPI();
    }


    public void updateUserLocation(double latitude, double longitude) {
        if (AppController.getInstance().getModelFacade().getLocalModel().getToken() == null) {
            return;
        }
        String requestBody = formJsonForUserLocation(latitude, longitude).toString();
        String url = URLConstants.URL_UPDATE_USER_LOCATION;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQIEST_ID_UPDATE_USER_LOCATION,
                Request.Method.POST, url, false, "Please wait...", requestBody);
        apiHandler.requestAPI();
    }


    public void checkInWalkins(int storeId) {
        String url = URLConstants.URL_CHECK_IN_WALKIN + "?store_id=" + storeId;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQIEST_ID_CHECKIN_WALKIN,
                Request.Method.GET, url, true, "Please wait...", null);
        apiHandler.requestAPI();
    }

    public void checkInProducts(BarCodeScanParcelDo barCodeScanParcelDo) {
        String url = URLConstants.URL_CHECK_IN_PRODUCTS + "?store_id=" + barCodeScanParcelDo.getStoreId() +
                "&product_barcode_id=" + barCodeScanParcelDo.getProductBarId() + "&barcode_value=" + barCodeScanParcelDo.getBarCodeValue();

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQIEST_ID_CHECKIN_PRODUCTS,
                Request.Method.GET, url, true, "Verifying barcode...", null);
        apiHandler.requestAPI();

    }

    public void checkInPurchases(BarCodeScanParcelDo barCodeScanParcelDo) {
        String url = URLConstants.URL_CHECK_IN_PURCHASES + "?store_id=" + barCodeScanParcelDo.getStoreId() +
                "&purchase_barcode_id=" + barCodeScanParcelDo.getProductBarId() + "&barcode_value=" + barCodeScanParcelDo.getBarCodeValue();

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQIEST_ID_CHECKIN_PURCHASES,
                Request.Method.GET, url, true, "Verifying barcode...", null);
        apiHandler.requestAPI();

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorResponse);

        try {
            switch (requestId) {
                case RequestConstants.REQIEST_ID_UPDATE_USER_LOCATION:
                    break;
                case RequestConstants.REQIEST_ID_CHECKIN_WALKIN:
                    onCheckinWalkinResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQIEST_ID_CHECKIN_PRODUCTS:
                    onCheckinProductsResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQIEST_ID_CHECKIN_PURCHASES:
                    onCheckinPurchaseResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQIEST_ID_REDEEM_KOIN:
                    onRedeemKoinResponse(isSuccess, result, errorResponse);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onRedeemKoinResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        JSONObject jsonObject = (JSONObject) result;
        JSONObject data = jsonObject.getJSONObject("data");

        if (isSuccess) {
            String message = data.getString("message");
            if (apiCallback != null) {
                errorResponse.setErrorString(message);
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQIEST_ID_REDEEM_KOIN,
                        true, result, errorResponse);
            }

        } else {
            String error_message = data.getString("error_message");
            if (apiCallback != null) {
                errorResponse.setErrorString(error_message);
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQIEST_ID_REDEEM_KOIN,
                        false, result, errorResponse);
            }
        }
    }

    private void onCheckinPurchaseResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        JSONObject jsonObject = (JSONObject) result;
        JSONObject data = jsonObject.getJSONObject("data");
        if (isSuccess) {
            String message = data.getString("message");
            if (apiCallback != null) {
                errorResponse.setErrorString(message);
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQIEST_ID_CHECKIN_PURCHASES,
                        true, result, errorResponse);
            }

        } else {
            String error_message = data.getString("error_message");
            if (apiCallback != null) {
                errorResponse.setErrorString(error_message);
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQIEST_ID_CHECKIN_PURCHASES,
                        false, result, errorResponse);
            }

        }

    }

    private void onCheckinProductsResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        JSONObject jsonObject = (JSONObject) result;
        JSONObject data = jsonObject.getJSONObject("data");

        if (isSuccess) {
            String message = data.getString("message");
            if (apiCallback != null) {
                errorResponse.setErrorString(message);
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQIEST_ID_CHECKIN_PRODUCTS,
                        true, result, errorResponse);
            }

        } else {
            String error_message = data.getString("error_message");
            if (apiCallback != null) {
                errorResponse.setErrorString(error_message);
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQIEST_ID_CHECKIN_PRODUCTS,
                        false, result, errorResponse);
            }

        }
    }

    private void onCheckinWalkinResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {


        JSONObject jsonObject = (JSONObject) result;
        JSONObject data = jsonObject.getJSONObject("data");

        if (isSuccess) {
            String message = data.getString("message");
            if (apiCallback != null) {
                errorResponse.setErrorString(message);
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQIEST_ID_CHECKIN_WALKIN,
                        true, result, errorResponse);
            }

        } else {

            String error_message = data.getString("error_message");
            if (apiCallback != null) {
                errorResponse.setErrorString(error_message);
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQIEST_ID_CHECKIN_WALKIN,
                        false, result, errorResponse);
            }

        }
    }

    public JSONObject formJsonToRedeemKoin(int store_id, int catalogue_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("store_id", store_id);
            jsonObject.put("catalogue_id", catalogue_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject formJsonForUserLocation(double latitude, double longitude) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
