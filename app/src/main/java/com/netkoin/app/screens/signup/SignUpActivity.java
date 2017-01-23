package com.netkoin.app.screens.signup;

import android.view.View;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseActivity;
import com.netkoin.app.custom_views.material_edittext.MaterialEditText;
import com.netkoin.app.custom_views.switchbtn.SwitchButton;
import com.netkoin.app.servicemodels.LoginFlowServiceModel;
import com.netkoin.app.utils.Utils;

public class SignUpActivity extends AbstractBaseActivity {
    private MaterialEditText nameMaterialEditText;
    private MaterialEditText emailMaterialEditText;
    private MaterialEditText pwdMaterialEditText;
    private MaterialEditText confPwdMaterialEditText;

    private SwitchButton emailPromotionSwitchBtn;
    private TextView signUpTextView;
    private TextView termToUseTextView;
    private TextView privacyPolicyTextView;

    @Override
    public View initView() {
        View view = getLayoutInflater().inflate(R.layout.activity_sign_up, null);

        initActionBarView(view);

        nameMaterialEditText = (MaterialEditText) view.findViewById(R.id.nameMaterialEditText);
        emailMaterialEditText = (MaterialEditText) view.findViewById(R.id.emailMaterialEditText);
        pwdMaterialEditText = (MaterialEditText) view.findViewById(R.id.pwdMaterialEditText);
        confPwdMaterialEditText = (MaterialEditText) view.findViewById(R.id.confPwdMaterialEditText);

        emailPromotionSwitchBtn = (SwitchButton) view.findViewById(R.id.emailPromotionSwitchBtn);
        signUpTextView = (TextView) view.findViewById(R.id.signUpTextView);
        termToUseTextView = (TextView) view.findViewById(R.id.termToUseTextView);
        privacyPolicyTextView = (TextView) view.findViewById(R.id.privacyPolicyTextView);

        return view;
    }

    @Override
    public void registerEvents() {
        termToUseTextView.setOnClickListener(this);
        privacyPolicyTextView.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpTextView:
                onSignupCtnClick();
                break;
            case R.id.termToUseTextView:
                Utils.getInstance().openBrowser(this, "http://www.netkoin.com");
                break;
            case R.id.privacyPolicyTextView:
                Utils.getInstance().openBrowser(this, "http://www.netkoin.com");
                break;
        }
    }

    private void onSignupCtnClick() {
        if (isValiedField()) {
            LoginFlowServiceModel loginFlowModel = new LoginFlowServiceModel(this, this);
            loginFlowModel.performSignUp(nameMaterialEditText.getEditText().getText().toString().trim(),
                    emailMaterialEditText.getEditText().getText().toString().trim(),
                    emailMaterialEditText.getEditText().getText().toString().trim(),
                    pwdMaterialEditText.getEditText().getText().toString().trim());
        }
    }

    private boolean isValiedField() {
        if (nameMaterialEditText.getEditText().getText().toString().trim().length() == 0) {
            showAleartPosBtnOnly(null, "Alert", "Please enter your full name");
            return false;

        } else if (emailMaterialEditText.getEditText().getText().toString().trim().length() == 0) {
            showAleartPosBtnOnly(null, "Alert", "Please enter your email address");
            return false;

        } else if (pwdMaterialEditText.getEditText().getText().toString().trim().length() == 0) {
            showAleartPosBtnOnly(null, "Alert", "Please enter your password");
            return false;

        } else if (confPwdMaterialEditText.getEditText().getText().toString().trim().length() == 0) {
            showAleartPosBtnOnly(null, "Alert", "Please enter confirm your password");
            return false;

        } else if (!isValidEmail(emailMaterialEditText.getEditText().getText().toString().trim())) {
            showAleartPosBtnOnly(null, "Alert", "Please enter valid email address");
            return false;
        } else if (!pwdMaterialEditText.getEditText().getText().toString().trim().equals(confPwdMaterialEditText.getEditText().getText().toString())) {
            showAleartPosBtnOnly(null, "Alert", "Please check your confirm password");
            return false;
        }
        return true;

    }

    @Override
    public void updateUI() {
        setActionBarTitle("Sign Up");
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
        showAleartPosBtnOnly(null, "Message", errorString);
    }


}
