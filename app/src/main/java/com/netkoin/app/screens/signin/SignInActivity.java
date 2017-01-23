package com.netkoin.app.screens.signin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseActivity;
import com.netkoin.app.constants.Constants;
import com.netkoin.app.controller.ActivityController;
import com.netkoin.app.custom_views.material_edittext.MaterialEditText;
import com.netkoin.app.servicemodels.LoginFlowServiceModel;
import com.netkoin.app.social.FacebookModel;
import com.netkoin.app.social.FbUserDo;
import com.netkoin.app.social.SocialLoginInterface;
import com.netkoin.app.utils.Utils;

import java.util.Arrays;

public class SignInActivity extends AbstractBaseActivity implements SocialLoginInterface, GoogleApiClient.OnConnectionFailedListener {
    private MaterialEditText emailMaterialEditText;
    private MaterialEditText pwdMaterialEditText;
    private TextView gPlusTextView;
    private TextView fbTextView;
    private TextView termToUseTextView;
    private TextView privacyPolicyTextView;
    private LinearLayout loginButtonLinLayout;

    @Override
    public View initView() {
        View view = getLayoutInflater().inflate(R.layout.activity_sign_in, null);
        initActionBarView(view);

        initGoogle();

        emailMaterialEditText = (MaterialEditText) view.findViewById(R.id.emailMaterialEditText);
        pwdMaterialEditText = (MaterialEditText) view.findViewById(R.id.pwdMaterialEditText);

        gPlusTextView = (TextView) view.findViewById(R.id.gPlusTextView);
        fbTextView = (TextView) view.findViewById(R.id.fbTextView);

        termToUseTextView = (TextView) view.findViewById(R.id.termToUseTextView);
        privacyPolicyTextView = (TextView) view.findViewById(R.id.privacyPolicyTextView);
        loginButtonLinLayout = (LinearLayout) view.findViewById(R.id.loginButtonLinLayout);

        checkKeyHash();
        checkPermissions(REQUEST_MARSHMELLO_PERMISSIONS, mustPermissions, null);
        return view;
    }


    private void checkKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getApplication().getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String myHashCode = HttpRequest.Base64.encodeBytes(md.digest());
//                Toast.makeText(SignInActivity.this, ">> hash>> " + myHashCode, Toast.LENGTH_LONG).show();
//                System.out.println(">>Key hash >> " + myHashCode);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void registerEvents() {
        gPlusTextView.setOnClickListener(this);
        fbTextView.setOnClickListener(this);

        termToUseTextView.setOnClickListener(this);
        privacyPolicyTextView.setOnClickListener(this);
        loginButtonLinLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gPlusTextView:
                onGPlusClick();
                break;
            case R.id.fbTextView:
                onFBBtnClick();
                break;
            case R.id.termToUseTextView:
                Utils.getInstance().openBrowser(this, "http://www.netkoin.com");
                break;
            case R.id.privacyPolicyTextView:
                Utils.getInstance().openBrowser(this, "http://www.netkoin.com");
                break;
            case R.id.loginButtonLinLayout:
                onLoginBtnClick();
                break;
        }
    }

    private void onFBBtnClick() {
        requestFBLogin();
    }

    private void onGPlusClick() {
        requestGPlusLogin();
    }


    private void onLoginBtnClick() {
        if (isValiedField()) {
            LoginFlowServiceModel loginFlowModel = new LoginFlowServiceModel(this, this);
            loginFlowModel.performSignIn(emailMaterialEditText.getEditText().getText().toString().trim(),
                    pwdMaterialEditText.getEditText().getText().toString().trim());
        }
    }

    private boolean isValiedField() {
        if (emailMaterialEditText.getEditText().getText().toString().trim().length() == 0) {
            showAleartPosBtnOnly(null, "Alert", "Please enter your email address");
            return false;
        } else if (pwdMaterialEditText.getEditText().getText().toString().trim().length() == 0) {
            showAleartPosBtnOnly(null, "Alert", "Please enter your password");
            return false;
        } else if (!isValidEmail(emailMaterialEditText.getEditText().getText().toString().trim())) {
            showAleartPosBtnOnly(null, "Alert", "Please enter valid email address");
            return false;
        }
        return true;
    }


    @Override
    public void updateUI() {
        setActionBarTitle("Sign In");
    }

    @Override
    public void onActionBarLeftBtnClick() {
        finish();
    }

    @Override
    public void onActionBarTitleClick() {

    }

    @Override
    public void onPermissionResult(int requestCode, boolean isGranted, Object extras) {

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        if (isSuccess) {
            ActivityController.getInstance().handleEvent(this, ActivityController.ACTIVITY_HOME_SCREEN);
        } else {
            showAleartPosBtnOnly(null, "Message", errorString);
        }
    }

    /**
     * ----------- Google Login
     */
    public final int RC_SIGN_IN = 1001;
    private GoogleApiClient mGoogleApiClient;

    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void requestGPlusLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    /**
     * --------------------FB code------------
     */
    private CallbackManager fbCallbackManager = null;
    private FacebookModel faceBookModel = null;

    public void requestFBLogin() {
        fbCallbackManager = CallbackManager.Factory.create();
        faceBookModel = new FacebookModel(this, this);
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(fbCallbackManager, faceBookModel);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_status", "user_birthday"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            if (fbCallbackManager != null) {
                fbCallbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
//            Toast.makeText(this, acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            LoginFlowServiceModel loginFlowModel = new LoginFlowServiceModel(this, this);
            loginFlowModel.performGPlusSignIn(acct);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    @Override
    public void onSocialLoginSuccess(FbUserDo fbUserDo, String socialType) {
        if (socialType.equals(Constants.LOGIN_TYPE_FB)) {
            LoginFlowServiceModel loginFlowModel = new LoginFlowServiceModel(this, this);
            loginFlowModel.performFacebookSignIn(fbUserDo);
        }
    }

    @Override
    public void onSocialLoginFailure(String error, String socialType) {
        System.out.println(">>" + error);
    }

    @Override
    public void onSocialLoginCancel(String socialType) {
        System.out.println(">>" + socialType);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
