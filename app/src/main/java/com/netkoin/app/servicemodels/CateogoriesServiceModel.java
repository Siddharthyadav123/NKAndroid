package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.MainCategory;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddharth on 1/11/2017.
 */
public class CateogoriesServiceModel extends BaseServiceModel {

    private ArrayList<MainCategory> categories = new ArrayList<>();

    public CateogoriesServiceModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }

    public void loadCateogries() {
        String url = URLConstants.URL_GET_CATEGORIES;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_CATEOGRIES_LIST,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorString);
        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_CATEOGRIES_LIST:
                    onCategoriesResponse(isSuccess, result, errorString);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onCategoriesResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;

            if (result != null && jsonObject.length() > 0) {

                JSONArray response = jsonObject.getJSONArray("data");
                categories = gson.fromJson(response.toString(), new TypeToken<ArrayList<MainCategory>>() {
                }.getType());


                if (categories.size() == 0) {
                    if (apiCallback != null) {
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_CATEOGRIES_LIST,
                                false, result, "Cateogries not found");
                    }
                } else {
                    if (apiCallback != null) {
                        this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_CATEOGRIES_LIST,
                                true, result, "");
                    }
                }
            } else {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_CATEOGRIES_LIST,
                            false, result, "Cateogries not found");
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_CATEOGRIES_LIST,
                        false, result, errorString);
            }
        }
    }

    public ArrayList<MainCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<MainCategory> categories) {
        this.categories = categories;
    }
}
