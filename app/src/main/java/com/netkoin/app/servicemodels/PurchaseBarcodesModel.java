package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.PurchaseBarcode;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddharth on 1/13/2017.
 */
public class PurchaseBarcodesModel extends BaseServiceModel {

    private ArrayList<PurchaseBarcode> purchaseBarcodes = new ArrayList<>();

    public PurchaseBarcodesModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }

    public void loadPurchaseBarcodes(int storeId) {
        String url = URLConstants.URL_PURCHASE_BARCODES + "?store_id=" + storeId + "&limit=" + limit + "&page=" + page;
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_PURCHASE_BARCODES,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorString);
        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_PURCHASE_BARCODES:
                    onPurchaseBarcodeResponse(isSuccess, result, errorString);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPurchaseBarcodeResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONArray response = jsonObject.getJSONArray("data");
            purchaseBarcodes = gson.fromJson(response.toString(), new TypeToken<ArrayList<PurchaseBarcode>>() {
            }.getType());


            if (purchaseBarcodes.size() == 0) {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_PURCHASE_BARCODES,
                            false, result, "No purchase barcode item found.");
                }
            } else {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_PURCHASE_BARCODES,
                            true, result, "");
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_PURCHASE_BARCODES,
                        false, result, errorString);
            }
        }

    }

    public ArrayList<PurchaseBarcode> getPurchaseBarcodes() {
        return purchaseBarcodes;
    }
}