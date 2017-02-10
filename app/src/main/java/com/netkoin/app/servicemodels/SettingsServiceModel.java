package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;
import com.netkoin.app.volly.ErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddharth on 1/23/2017.
 */
public class SettingsServiceModel extends BaseServiceModel {
    public SettingsServiceModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }

    public void requestPostSettings() {
        int settingId = sharedPref.getIntWithMinusOneAsDefault(SharedPref.KEY_SETTING_ID);
        int user_id = sharedPref.getInt(SharedPref.KEY_USER_ID);

        //settings
        boolean step_in = sharedPref.getSettingBool(SharedPref.KEY_SETTING_NOTI_KOINS_REC_VIA_STEP_IN);
        boolean nearby_store = sharedPref.getSettingBool(SharedPref.KEY_SETTING_NOTI_NEAR_BY_STORES);
        boolean received_koins_transfer = sharedPref.getSettingBool(SharedPref.KEY_SETTING_NOTI_KOIN_REC_VIA_TRANSFER);

        boolean isSettingExistPeviously = false;
        String URL = URLConstants.URL_POST_SETTINGS + ".json";
        int requestType = Request.Method.POST;

        //if setting already exist
        if (settingId != -1) {
            isSettingExistPeviously = true;
            URL = URLConstants.URL_POST_SETTINGS + settingId + ".json";
            requestType = Request.Method.PUT;
        }

        String requestBody = formRequestBody(isSettingExistPeviously, user_id, step_in, nearby_store, received_koins_transfer);

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQIEST_ID_POST_SETTINGS,
                requestType, URL, false, null, requestBody);
        apiHandler.requestAPI();
    }


    private String formRequestBody(boolean isSettingExistPeviously, int user_id, boolean step_in, boolean nearby_store, boolean received_koins_transfer) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("step_in", step_in ? 1 : 0);
            jsonObject.put("nearby_store", nearby_store ? 1 : 0);
            jsonObject.put("received_koins_transfer", received_koins_transfer ? 1 : 0);

            if (!isSettingExistPeviously)
                jsonObject.put("user_id", user_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorResponse);
        try {
            switch (requestId) {
                case RequestConstants.REQIEST_ID_POST_SETTINGS:
                    onPostSettingResponse(isSuccess, result, errorResponse);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void onPostSettingResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            int settingId = sharedPref.getIntWithMinusOneAsDefault(SharedPref.KEY_SETTING_ID);
            if (settingId == -1) {
                JSONObject jsonObject = (JSONObject) result;
                JSONObject data = jsonObject.getJSONObject("data");
                int id = data.getInt("id");
                sharedPref.put(SharedPref.KEY_SETTING_ID, new Integer(id));
            }
        }
    }
}
