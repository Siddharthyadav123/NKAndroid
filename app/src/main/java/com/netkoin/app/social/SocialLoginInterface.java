package com.netkoin.app.social;


/**
 * Created by siddharth on 8/2/2016.
 */
public interface SocialLoginInterface {
    public void onSocialLoginSuccess(FbUserDo fbUserDo, String socialType);

    public void onSocialLoginFailure(String error, String socialType);

    public void onSocialLoginCancel(String socialType);
}
