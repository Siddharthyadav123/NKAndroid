package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.Catalogue;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddharth on 1/13/2017.
 */
public class CataloguesServiceModel extends BaseServiceModel {

    ArrayList<Catalogue> catalouges = new ArrayList<>();

    public CataloguesServiceModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }


    public void loadCatalougesByStore(int storeId) {
        String url = URLConstants.URL_CATALOGUES + "?store_id=" + storeId + "&limit=" + limit + "&page=" + page;
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_CATALOUGES,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorString);
        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_CATALOUGES:
                    onCatalougesResponse(isSuccess, result, errorString);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCatalougesResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONArray response = jsonObject.getJSONArray("data");
            catalouges = gson.fromJson(response.toString(), new TypeToken<ArrayList<Catalogue>>() {
            }.getType());


            if (catalouges.size() == 0) {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_CATALOUGES,
                            false, result, "No items found in catalogue.");
                }
            } else {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_CATALOUGES,
                            true, result, "");
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_CATALOUGES,
                        false, result, errorString);
            }
        }
    }

    public ArrayList<Catalogue> getCatalouges() {
        return catalouges;
    }
}
