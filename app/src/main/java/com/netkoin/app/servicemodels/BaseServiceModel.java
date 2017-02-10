package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.controller.AppController;
import com.netkoin.app.entities.TotalKoin;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;
import com.netkoin.app.volly.ErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddharthyadav on 02/01/17.
 */

public class BaseServiceModel implements APIHandlerCallback {
    protected Context context;
    protected int limit = 7;
    protected int page = 1;
    protected APIHandlerCallback apiCallback;
    protected SharedPref sharedPref;
    protected Gson gson = null;

    public void incrementPage() {
        this.page = this.page + 1;
    }

    public void resetPage() {
        this.page = 1;
    }

    public BaseServiceModel(Context context, APIHandlerCallback apiCallback) {
        this.context = context;
        this.apiCallback = apiCallback;
        sharedPref = new SharedPref(context);
        gson = new Gson();
    }


    public void loadTotalKoins() {
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_TOTAL_KOIN, Request.Method.GET,
                URLConstants.URL_GET_TOTAL_KOINS, false, null, null);
        apiHandler.requestAPI();

    }


    private void onTotalKoinResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONObject response = jsonObject.getJSONObject("data");

            TotalKoin totalKoins = gson.fromJson(response.toString(), TotalKoin.class);
            AppController.getInstance().getModelFacade().getLocalModel().setTotalKoins(totalKoins);

            if (apiCallback != null) {
                apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_TOTAL_KOIN,
                        true, result, errorResponse);
            }
        } else {
            if (apiCallback != null) {
                apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_TOTAL_KOIN,
                        false, result, errorResponse);
            }
        }
    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_TOTAL_KOIN:
                    onTotalKoinResponse(isSuccess, result, errorResponse);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
