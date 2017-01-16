package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.ProductBarcode;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddharth on 1/13/2017.
 */
public class ProductBarcodesModel extends BaseServiceModel {

    ArrayList<ProductBarcode> productBarcodes = new ArrayList<>();

    public ProductBarcodesModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }

    public void loadProductBarcodes(int storeId) {
        String url = URLConstants.URL_PRODUCT_BARCODES + "?store_id=" + storeId + "&limit=" + limit + "&page=" + page;
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_PRODUCT_BARCODES,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorString);
        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_PRODUCT_BARCODES:
                    onProductBarcodeResponse(isSuccess, result, errorString);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onProductBarcodeResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONArray response = jsonObject.getJSONArray("data");
            productBarcodes = gson.fromJson(response.toString(), new TypeToken<ArrayList<ProductBarcode>>() {
            }.getType());


            if (productBarcodes.size() == 0) {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_PRODUCT_BARCODES,
                            false, result, "No product barcode item found.");
                }
            } else {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_PRODUCT_BARCODES,
                            true, result, "");
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_PRODUCT_BARCODES,
                        false, result, errorString);
            }
        }
    }

    public ArrayList<ProductBarcode> getProductBarcodes() {
        return productBarcodes;
    }
}
