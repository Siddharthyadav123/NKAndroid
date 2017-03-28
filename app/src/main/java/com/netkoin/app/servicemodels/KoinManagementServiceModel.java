package com.netkoin.app.servicemodels;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.ActivityLog;
import com.netkoin.app.entities.Message;
import com.netkoin.app.screens.koin_managment.fragments.KoinMessagesFragment;
import com.netkoin.app.volly.APIHandler;
import com.netkoin.app.volly.APIHandlerCallback;
import com.netkoin.app.volly.ErrorResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashishkumarpatel on 13/01/17.
 */

public class KoinManagementServiceModel extends BaseServiceModel {

    private ArrayList<ActivityLog> activityLogs = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    int unreadActivityLogscount;

    public KoinManagementServiceModel(Context context, APIHandlerCallback apiCallback) {
        super(context, apiCallback);
    }


    public void loadActivityLogs() {
        String url = URLConstants.URL_GET_ACTIVITY_LOGS + "?sort=created&direction=desc&limit=" + limit + "&page=" + page;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }

    public void loadMessages(int currentSegmentFilter) {
        String url = null;
        if (currentSegmentFilter == KoinMessagesFragment.TYPE_SENT) {
            url = URLConstants.URL_GET_SENT_MESSAGES + "?limit=" + limit + "&page=" + page;
        } else {
            url = URLConstants.URL_GET_RECIEVED_MESSAGES + "?limit=" + limit + "&page=" + page;
        }

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_MESSAGES,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }

    //{"koins":1 , "receiver_email":"rrr@netkoin.com","message":"ABCDE"}
    public void transeferKoin(int koins, String receiver_email, String message) {
        String requestBody = formTransferKoinJson(koins, receiver_email, message).toString();
        String url = URLConstants.URL_POST_TRANSER_KOINS;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_POST_TRANSFER_KOINS,
                Request.Method.POST, url, true, "Transferring Koins..", requestBody);
        apiHandler.requestAPI();
    }

    public JSONObject formTransferKoinJson(int koins, String receiver_email, String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("koins", koins);
            jsonObject.put("receiver_email", receiver_email);
            jsonObject.put("message", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public void loadActivityLogsUnreadCount() {
        String url = URLConstants.URL_GET_ACTIVITY_LOGS_UNREAD_COUNT;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS_UREAD_COUNT,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();

    }

    public void updateReadActivityStatus() {
        String url = URLConstants.URL_GET_ACTIVITY_LOGS_SET_READ_ALL;

        APIHandler apiHandler = new APIHandler(context, this, RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS_SET_READ_ALL,
                Request.Method.GET, url, false, null, null);
        apiHandler.requestAPI();
    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        super.onAPIHandlerResponse(requestId, isSuccess, result, errorResponse);
        try {

            switch (requestId) {
                case RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS:
                    onActivityLogResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQUEST_ID_POST_TRANSFER_KOINS:
                    onTransferKoinsResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS_UREAD_COUNT:
                    onActivityLogsUnreadCountResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS_SET_READ_ALL:
                    onActivitLogsSetReadAllResponse(isSuccess, result, errorResponse);
                    break;
                case RequestConstants.REQUEST_ID_GET_MESSAGES:
                    onMessagesResponse(isSuccess, result, errorResponse);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onMessagesResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONArray response = jsonObject.getJSONArray("data");

            messages = gson.fromJson(response.toString(), new TypeToken<ArrayList<Message>>() {
            }.getType());

            if (messages.size() == 0) {
                if (apiCallback != null) {
                    errorResponse.setErrorString("No Messages found");
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_MESSAGES,
                            false, result, errorResponse);
                }
            } else {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_MESSAGES,
                            true, result, errorResponse);
                }
            }

        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_MESSAGES,
                        false, result, errorResponse);
            }

        }
    }

    private void onActivitLogsSetReadAllResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONObject response = jsonObject.getJSONObject("data");
            // message
            String message = response.getString("message");

            if (apiCallback != null) {
                errorResponse.setErrorString(message);
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS_SET_READ_ALL,
                        true, result, errorResponse);
            }
        } else {

            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS_SET_READ_ALL,
                        false, result, errorResponse);
            }
        }

    }

    private void onActivityLogsUnreadCountResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONObject response = jsonObject.getJSONObject("data");
            // message
            unreadActivityLogscount = response.getInt("count");
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS_UREAD_COUNT,
                        true, result, errorResponse);
            }
        } else {
            unreadActivityLogscount = 0;
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS_UREAD_COUNT,
                        false, result, errorResponse);
            }
        }
    }

    private void onTransferKoinsResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (result != null) {
            JSONObject jsonObject = (JSONObject) result;
            JSONObject response = jsonObject.getJSONObject("data");

            if (isSuccess) {
                // message
                String message = response.getString("message");
                if (apiCallback != null) {
                    errorResponse.setErrorString(message);
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_TRANSFER_KOINS,
                            true, message, errorResponse);
                }

            } else {
                // error message
                String error_message = response.getString("error_message");
                errorResponse.setErrorString(error_message);
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_TRANSFER_KOINS,
                            false, result, errorResponse);
                }
            }
        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_POST_TRANSFER_KOINS,
                        false, result, errorResponse);
            }

        }
    }

    private void onActivityLogResponse(boolean isSuccess, Object result, ErrorResponse errorResponse) throws JSONException {
        if (isSuccess) {
            JSONObject jsonObject = (JSONObject) result;
            JSONArray response = jsonObject.getJSONArray("data");

            activityLogs = gson.fromJson(response.toString(), new TypeToken<ArrayList<ActivityLog>>() {
            }.getType());


            if (activityLogs.size() == 0) {
                if (apiCallback != null) {
                    errorResponse.setErrorString("No Activity logs found");
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS,
                            false, result, errorResponse);
                }
            } else {
                if (apiCallback != null) {
                    this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS,
                            true, result, errorResponse);
                }
            }

        } else {
            if (apiCallback != null) {
                this.apiCallback.onAPIHandlerResponse(RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS,
                        false, result, errorResponse);
            }

        }
    }

    public ArrayList<ActivityLog> getActivityLogs() {
        return activityLogs;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public int getUnreadActivityLogscount() {
        return unreadActivityLogscount;
    }
}
