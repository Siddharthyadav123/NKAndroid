package com.netkoin.app.screens.search_location.activity;

import android.view.View;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseActivity;

public class SearchLocationActivity extends AbstractBaseActivity {

    @Override
    public View initView() {
        View view = getLayoutInflater().inflate(R.layout.activity_search_location, null);
        initActionBarView(view);
        return view;
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void updateUI() {
        setActionBarTitle("Select Location");
    }

    @Override
    public void onActionBarLeftBtnClick() {
        finish();
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
}
