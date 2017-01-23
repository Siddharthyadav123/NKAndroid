package com.netkoin.app.servicemodels;

import android.content.Context;
import android.os.Build;

import com.android.volley.Request;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.iid.FirebaseInstanceId;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.controller.AppController;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.social.FbUserDo;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddharth on 1/9/2017.
 */
public class LoginFlowServiceModel extends BaseServiceModel {

    private String requestBody;

    public LoginFlowServiceModel(Context context, APIHandlerCallback apiHandlerCallback) {
        super(context, apiHandlerCallback);
    }

    public void performSignUp(String firstName, String userName, String email, String pwd) {
        requestBody = formSignUpJsonBody(firstName, userName, email, pwd).toString();
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_POST_SIGNUP, Request.Method.POST, URLConstants.URL_SIGNUP, true, "Signing up..", requestBody);
        apiHandler.setNeedTokenHeader(false);
        apiHandler.requestAPI();
    }


    public void performSignIn(String email, String pwd) {
        requestBody = formLoginJsonBody(email, pwd).toString();
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_POST_SIGNIN, Request.Method.POST, URLConstants.URL_SIGNIN, true, "Signing in..", requestBody);
        apiHandler.setNeedTokenHeader(false);
        apiHandler.requestAPI();

    }

    public void performFacebookSignIn(FbUserDo fbUserDo) {
        requestBody = formFBLoginJsonBody(fbUserDo).toString();
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_POST_FACEBOOK_SIGNIN, Request.Method.POST, URLConstants.URL_FACEBOOK_SIGNIN, true, "Signing in...", requestBody);
        apiHandler.setNeedTokenHeader(false);
        apiHandler.requestAPI();
    }

    public void performGPlusSignIn(GoogleSignInAccount acct) {
        requestBody = formGPlusLoginJsonBody(acct).toString();
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_POST_GPLUS_SIGNIN, Request.Method.POST, URLConstants.URL_GPLUS_SIGNIN, true, "Signing in...", requestBody);
        apiHandler.setNeedTokenHeader(false);
        apiHandler.requestAPI();
    }


    public void performLogout() {
        requestBody = formLogoutJsonBody().toString();
        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_POST_LOGOUT,
                Request.Method.POST, URLConstants.URL_POST_LOGOUT, true, "Logging out..", requestBody);
        apiHandler.setNeedTokenHeader(true);
        apiHandler.requestAPI();

    }


    //api for to hit from splash to refresh the token n all
    public void performSilentLogin(int requestId, String requestBody) {
        this.requestBody = refreshPushToken(requestBody);
        String url = "";
        switch (requestId) {
            case RequestConstants.REQUEST_ID_POST_SIGNIN:
                url = URLConstants.URL_SIGNIN;
                break;
            case RequestConstants.REQUEST_ID_POST_FACEBOOK_SIGNIN:
                url = URLConstants.URL_FACEBOOK_SIGNIN;
                break;
            case RequestConstants.REQUEST_ID_POST_GPLUS_SIGNIN:
                url = URLConstants.URL_GPLUS_SIGNIN;
                break;
            default:
                break;
        }
        APIHandler apiHandler = new APIHandler(context, this, requestId, Request.Method.POST, url, false, null, requestBody);
        apiHandler.setNeedTokenHeader(true);
        apiHandler.setAllowRetryOnFailure(true);
        apiHandler.requestAPI();
    }


    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorString);
        try {
            switch (requestId) {
                case RequestConstants.REQUEST_ID_POST_SIGNIN:
                    onSignInResponse(isSuccess, result, errorString);
                    break;
                case RequestConstants.REQUEST_ID_POST_FACEBOOK_SIGNIN:
                    onFacebookSignInResponse(isSuccess, result, errorString);
                    break;
                case RequestConstants.REQUEST_ID_POST_SIGNUP:
                    onSignUpResponse(isSuccess, result, errorString);
                    break;
                case RequestConstants.REQUEST_ID_POST_GPLUS_SIGNIN:
                    onGPlusSignInResponse(isSuccess, result, errorString);
                    break;
                case RequestConstants.REQUEST_ID_POST_LOGOUT:
                    onLogoutResponse(isSuccess, result, errorString);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onLogoutResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONObject data = jsonObject.getJSONObject("data");
            int message_code = data.getInt("message_code");

            if (message_code == 1011) {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_LOGOUT,
                            true, result, "Logout Successfully !!");
                }
            } else {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_LOGOUT,
                            false, result, "Logout failed.");
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_LOGOUT,
                        false, result, errorString);
            }
        }
    }

    private void onGPlusSignInResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONObject data = jsonObject.getJSONObject("data");

            Integer id = data.getInt("id");
            String token = data.getString("token");

            if (data.has("user_setting")) {
                JSONObject userSettingsJson = data.getJSONObject("user_setting");
                addSettings(userSettingsJson);
            }

            AppController.getInstance().getModelFacade().getLocalModel().setToken(token);

            //storing details for silent login
            sharedPref.put(SharedPref.KEY_SILENT_LOGIN_REQUEST_PARAM, requestBody);
            sharedPref.put(SharedPref.KEY_SILENT_LOGIN_TYPE, new Integer(RequestConstants.REQUEST_ID_POST_GPLUS_SIGNIN));

            //saving user id and token
            sharedPref.put(SharedPref.KEY_AUTH_TOKEN, token);
            sharedPref.put(SharedPref.KEY_USER_ID, id);

            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_GPLUS_SIGNIN,
                        true, result, "Login Successful !");
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_GPLUS_SIGNIN,
                        false, result, errorString);
            }
        }
    }


    private void onFacebookSignInResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONObject data = jsonObject.getJSONObject("data");

            Integer id = data.getInt("id");
            String token = data.getString("token");

            if (data.has("user_setting")) {
                JSONObject userSettingsJson = data.getJSONObject("user_setting");
                addSettings(userSettingsJson);
            }

            AppController.getInstance().getModelFacade().getLocalModel().setToken(token);

            //storing details for silent login
            sharedPref.put(SharedPref.KEY_SILENT_LOGIN_REQUEST_PARAM, requestBody);
            sharedPref.put(SharedPref.KEY_SILENT_LOGIN_TYPE, new Integer(RequestConstants.REQUEST_ID_POST_FACEBOOK_SIGNIN));

            //saving user id and token
            sharedPref.put(SharedPref.KEY_AUTH_TOKEN, token);
            sharedPref.put(SharedPref.KEY_USER_ID, id);

            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_FACEBOOK_SIGNIN,
                        true, result, "Login Successful !");
            }

        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_FACEBOOK_SIGNIN,
                        false, result, errorString);
            }
        }
    }

    private void onSignInResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONObject data = jsonObject.getJSONObject("data");

            Integer id = data.getInt("id");
            String token = data.getString("token");

            if (data.has("user_setting")) {
                JSONObject userSettingsJson = data.getJSONObject("user_setting");
                addSettings(userSettingsJson);
            }

            AppController.getInstance().getModelFacade().getLocalModel().setToken(token);

            //storing details for silent login
            sharedPref.put(SharedPref.KEY_SILENT_LOGIN_REQUEST_PARAM, requestBody);
            sharedPref.put(SharedPref.KEY_SILENT_LOGIN_TYPE, new Integer(RequestConstants.REQUEST_ID_POST_SIGNIN));

            //saving user id and token
            sharedPref.put(SharedPref.KEY_AUTH_TOKEN, token);
            sharedPref.put(SharedPref.KEY_USER_ID, id);

            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_SIGNIN,
                        true, result, "Login Successful !");
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_SIGNIN,
                        false, result, errorString);
            }
        }
    }

    private void addSettings(JSONObject userSettingsJson) throws JSONException {
        Integer id = userSettingsJson.getInt("id");
        int nearby_store = userSettingsJson.getInt("nearby_store");
        int received_koins_transfer = userSettingsJson.getInt("received_koins_transfer");
        int step_in = userSettingsJson.getInt("step_in");

        //storing settings
        sharedPref.put(SharedPref.KEY_SETTING_ID, id);

        if (nearby_store == 1)
            sharedPref.put(SharedPref.KEY_SETTING_NOTI_NEAR_BY_STORES, new Boolean(true));
        else
            sharedPref.put(SharedPref.KEY_SETTING_NOTI_NEAR_BY_STORES, new Boolean(false));

        if (received_koins_transfer == 1)
            sharedPref.put(SharedPref.KEY_SETTING_NOTI_KOIN_REC_VIA_TRANSFER, new Boolean(true));
        else
            sharedPref.put(SharedPref.KEY_SETTING_NOTI_KOIN_REC_VIA_TRANSFER, new Boolean(false));

        if (step_in == 1)
            sharedPref.put(SharedPref.KEY_SETTING_NOTI_KOINS_REC_VIA_STEP_IN, new Boolean(true));
        else
            sharedPref.put(SharedPref.KEY_SETTING_NOTI_KOINS_REC_VIA_STEP_IN, new Boolean(false));

    }

    private void onSignUpResponse(boolean isSuccess, Object result, String errorString) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONObject data = jsonObject.getJSONObject("data");

            Integer id = data.getInt("id");
            String token = data.getString("token");

            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_SIGNUP,
                        true, result, "Sign-up Successful, Please sign in.");
            }
        } else {

            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_SIGNUP,
                        false, result, errorString);
            }
        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String brand = Build.BRAND;
        if (brand.startsWith(manufacturer)) {
            return capitalize(brand);
        } else {
            return capitalize(manufacturer) + " " + brand;
        }
    }

    public String getDeviceModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public String getDeviceAPIVersion() {
        return android.os.Build.VERSION.SDK_INT + "";
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public JSONObject formSignUpJsonBody(String firstName, String userName, String email, String pwd) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", firstName);
            jsonObject.put("username", userName);
            jsonObject.put("email", email);
            jsonObject.put("password", pwd);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject formLoginJsonBody(String email, String pwd) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", email);
            jsonObject.put("password", pwd);
            jsonObject.put("push_token", getPushToken());
            jsonObject.put("device_name", getDeviceName());
            jsonObject.put("device_model", getDeviceModel());
            jsonObject.put("os_name", "android");
            jsonObject.put("os_version", getDeviceAPIVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject formLogoutJsonBody() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("push_token", getPushToken());
            jsonObject.put("device_name", getDeviceName());
            jsonObject.put("device_model", getDeviceModel());
            jsonObject.put("os_name", "android");
            jsonObject.put("os_version", getDeviceAPIVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject formGPlusLoginJsonBody(GoogleSignInAccount acct) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("google_id", acct.getId());
            jsonObject.put("token", acct.getIdToken());
            jsonObject.put("first_name", acct.getDisplayName());
            jsonObject.put("last_name", acct.getFamilyName());
            jsonObject.put("email", acct.getEmail());
            jsonObject.put("push_token", getPushToken());
            jsonObject.put("device_name", getDeviceName());
            jsonObject.put("device_model", getDeviceModel());
            jsonObject.put("os_name", "android");
            jsonObject.put("os_version", getDeviceAPIVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String refreshPushToken(String oldJsonBody) {
        try {
            JSONObject jsonObject = new JSONObject(oldJsonBody);
            jsonObject.put("push_token", getPushToken());
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject formFBLoginJsonBody(FbUserDo fbUserDo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", fbUserDo.getName());
            jsonObject.put("last_name", fbUserDo.getName());
            jsonObject.put("facebook_id", fbUserDo.getFbid());
            jsonObject.put("token", fbUserDo.getToken());
            jsonObject.put("email", fbUserDo.getEmail());
            jsonObject.put("push_token", getPushToken());
            jsonObject.put("device_name", getDeviceName());
            jsonObject.put("device_model", getDeviceModel());
            jsonObject.put("os_name", "android");
            jsonObject.put("os_version", getDeviceAPIVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private String getPushToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println(">>FCM token found >>" + token);
        return token;
    }
}
