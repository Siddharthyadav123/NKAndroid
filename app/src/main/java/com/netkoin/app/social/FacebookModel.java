package com.netkoin.app.social;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.netkoin.app.constants.Constants;

import org.json.JSONObject;

/**
 * Created by siddharth on 1/19/2017.
 */
public class FacebookModel implements FacebookCallback<LoginResult> {
    private Context context;
    private SocialLoginInterface socialLoginInterface;
    private FbUserDo fbUserDo = null;

    public FacebookModel(Context context, SocialLoginInterface socialLoginInterface) {
        this.context = context;
        this.socialLoginInterface = socialLoginInterface;
        fbUserDo = new FbUserDo();
    }


    @Override
    public void onSuccess(LoginResult loginResult) {
        onLoginSuccess(loginResult);
    }

    @Override
    public void onCancel() {
        if (socialLoginInterface != null)
            socialLoginInterface.onSocialLoginCancel(Constants.LOGIN_TYPE_FB);
    }

    @Override
    public void onError(FacebookException error) {
        if (socialLoginInterface != null)
            socialLoginInterface.onSocialLoginFailure(error.getMessage(), Constants.LOGIN_TYPE_FB);
    }

    /**
     * Get called when Twitter login get success
     *
     * @param loginResult
     */
    private void onLoginSuccess(LoginResult loginResult) {
        fbUserDo.setFbid(loginResult.getAccessToken().getUserId());
        fbUserDo.setToken(loginResult.getAccessToken().getToken());
        Toast.makeText(context, "Login sid " + loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();


        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            fbUserDo.parseJsonDataForFacebook(object);
                            if (socialLoginInterface != null)
                                socialLoginInterface.onSocialLoginSuccess(fbUserDo, Constants.LOGIN_TYPE_FB);


                        } catch (Exception e) {
                            Toast.makeText(context, "Login failed 11 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            if (socialLoginInterface != null)
                                socialLoginInterface.onSocialLoginFailure(e.getMessage(), Constants.LOGIN_TYPE_FB);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,picture,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
