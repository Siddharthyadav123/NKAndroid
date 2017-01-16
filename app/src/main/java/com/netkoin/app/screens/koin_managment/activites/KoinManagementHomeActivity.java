package com.netkoin.app.screens.koin_managment.activites;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseActivity;
import com.netkoin.app.screens.homescreen.home.fragments.SettingsHomeScreenFragments;
import com.netkoin.app.screens.homescreen.home.fragments.StoresHomeScreenFragment;
import com.netkoin.app.screens.koin_managment.fragments.KoinHomeParentFragment;

public class KoinManagementHomeActivity extends AbstractBaseActivity {

    private KoinHomeParentFragment koinHomeParentFragment;

    @Override
    public View initView() {
        View view = getLayoutInflater().inflate(R.layout.activity_koin_management_home, null);
        return view;
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void updateUI() {
        FragmentTransaction childFragTrans = getSupportFragmentManager().beginTransaction();
        koinHomeParentFragment = new KoinHomeParentFragment();
        childFragTrans.replace(R.id.koinMangFrameLayout, koinHomeParentFragment);
        childFragTrans.addToBackStack(null);
        childFragTrans.commit();
    }

    @Override
    public void onActionBarLeftBtnClick() {

    }

    @Override
    public void onActionBarTitleClick() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {

    }

    @Override
    public void onBackPressed() {
        //poping fragments if there is any else finish the activity on back press
        if (!koinHomeParentFragment.getNavigationController().pop()) {
            finish();
        }
    }
}
