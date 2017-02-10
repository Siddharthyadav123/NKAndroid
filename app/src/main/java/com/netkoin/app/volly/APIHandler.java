package com.netkoin.app.volly;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.controller.AppController;
import com.netkoin.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class APIHandler implements Response.Listener<Object>, Response.ErrorListener {

    private final int CONNECTION_TIMEOUT_IN_MILLIS = 10000;
    private final int MAX_REQUEST_ATTEMPT = 2;

    private Context context;
    private int requestId;
    private int methodType;
    private boolean showLoading = false;
    private String loadingText;
    private String url;
    private ProgressDialog pDialog = null;
    private APIHandlerCallback apiHandlerCallback = null;
    private String requestBody = null;
    private Map<String, String> headers = null;
    private boolean showToastOnRespone = true;

    private boolean needTokenHeader = true;
    private boolean allowRetryOnFailure = false;
    private boolean responseFromOurServer = true;
    private boolean needToExitAppOnRetryPopupCancel = false;

    public APIHandler(Context context, APIHandlerCallback apiHandlerCallback, int requestId, int methodType, String url,
                      boolean showLoading, String loadingText, String requestBody) {
        this.context = context;
        this.apiHandlerCallback = apiHandlerCallback;
        this.requestId = requestId;
        this.methodType = methodType;
        this.url = url;
        this.showLoading = showLoading;
        this.loadingText = loadingText;
        this.requestBody = requestBody;
    }

    private void showLoading() {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pDialog = new ProgressDialog(context);
                    pDialog.setMessage(loadingText);
                    pDialog.show();
                }
            });
        }
    }

    private void hideLoading() {
        try {
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pDialog != null)
                            pDialog.dismiss();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void requestAPI() {
        if (context instanceof Activity) {
            Utils.getInstance().hideKeyboard((Activity) context);

        }
        //check if internet connect found or not
        if (!MyApplication.getInstance().checkConnection(context)) {
            if (showToastOnRespone) {
                String noInternetConnection = "No internet connection found";
                if (allowRetryOnFailure) {
                    showRetryErrorAlert("Alert", "No Internet Connection Found.");
                } else {
                    showToast(noInternetConnection);
                    ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.EROOR_CODE_INTERENT_NOT_FOUND,
                            noInternetConnection);
                    onAPIFailure(null, errorResponse);
                }
            }
            return;
        }

        //System.out.println("[API] request url = " + url);
        //System.out.println("[API] request body = " + requestBody);
        if (showLoading) {
            showLoading();
        }

        if (needTokenHeader) {
            headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + AppController.getInstance().getModelFacade().getLocalModel().getToken());
        }
        GenericRequest genericRequest = new GenericRequest(methodType, url, requestBody, this, this, headers);
        genericRequest.setRetryPolicy(new DefaultRetryPolicy(CONNECTION_TIMEOUT_IN_MILLIS,
                MAX_REQUEST_ATTEMPT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(genericRequest, requestId);
    }

    private void showToast(final String text) {
        if (showToastOnRespone) {
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (text != null && text.length() > 0)
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onResponse(Object response) {
        hideLoading();
        //System.out.println("[API] response body volly = " + response.toString());
        if (responseFromOurServer) {
            if (response != null) {
                JSONObject jsonObject = null;
                boolean isSuccess = false;
                try {
                    jsonObject = new JSONObject(response.toString());
                    isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        onAPISuccess(jsonObject);
                    } else {
                        //if not success from server
                        JSONObject data = jsonObject.getJSONObject("data");
                        String error_message = data.getString("error_message");
                        int error_code = data.getInt("error_code");
                        if (data != null && data.length() > 0 && error_message != null) {
                            ErrorResponse errorResponse = new ErrorResponse(error_code, error_message);
                            onAPIFailure(jsonObject, errorResponse);
                        } else {
                            ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.EROOR_CODE_FAILED_WITH_NO_MSG,
                                    "Failed with no message data");
                            onAPIFailure(jsonObject, errorResponse);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.EROOR_CODE_UNABLE_TO_FETCH_DATA, "Unable to fetch data");
                onAPIFailure(null, errorResponse);
                //System.out.println("[API] response fail volly = " + "Error in response");
            }
        } else {
            onAPISuccess(response);
        }


    }


    @Override
    public void onErrorResponse(VolleyError error) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.EROOR_CODE_ERROR_FROM_SERVER, "Error from Server");
        onAPIFailure(null, errorResponse);
        hideLoading();
    }

    private void onAPISuccess(final Object result) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (apiHandlerCallback != null) {
                        apiHandlerCallback.onAPIHandlerResponse(requestId, true, result, new ErrorResponse());
                    }
                }
            });
        } else {
            if (apiHandlerCallback != null) {
                apiHandlerCallback.onAPIHandlerResponse(requestId, true, result, new ErrorResponse());
            }
        }

    }

    private void onAPIFailure(final Object result, final ErrorResponse errorResponse) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (apiHandlerCallback != null) {
                        apiHandlerCallback.onAPIHandlerResponse(requestId, false, result, errorResponse);
                    }
                }
            });
        } else {
            if (apiHandlerCallback != null) {
                apiHandlerCallback.onAPIHandlerResponse(requestId, false, result, errorResponse);
            }
        }
    }


    public boolean isShowToastOnRespone() {
        return showToastOnRespone;
    }

    public void setShowToastOnRespone(boolean showToastOnRespone) {
        this.showToastOnRespone = showToastOnRespone;
    }

    public void setAllowRetryOnFailure(boolean allowRetryOnFailure) {
        this.allowRetryOnFailure = allowRetryOnFailure;
    }

    public void setNeedTokenHeader(boolean needTokenHeader) {
        this.needTokenHeader = needTokenHeader;
    }

    public void setResponseFromOurServer(boolean responseFromOurServer) {
        this.responseFromOurServer = responseFromOurServer;
    }

    protected void showRetryErrorAlert(String title, String bodyText) {
        Utils.getInstance().hideKeyboard((Activity) context);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(bodyText);
        builder1.setTitle(title);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        requestAPI();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (needToExitAppOnRetryPopupCancel) {
                            if (context instanceof Activity) {
                                ((Activity) context).finishAffinity();
                            }
                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public boolean isNeedToExitAppOnRetryPopupCancel() {
        return needToExitAppOnRetryPopupCancel;
    }

    public void setNeedToExitAppOnRetryPopupCancel(boolean needToExitAppOnRetryPopupCancel) {
        this.needToExitAppOnRetryPopupCancel = needToExitAppOnRetryPopupCancel;
    }
}
