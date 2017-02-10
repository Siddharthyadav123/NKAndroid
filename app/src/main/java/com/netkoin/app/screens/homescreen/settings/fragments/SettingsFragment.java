package com.netkoin.app.screens.homescreen.settings.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.netkoin.app.R;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.controller.ActivityController;
import com.netkoin.app.controller.AppController;
import com.netkoin.app.controller.FragmentNavigationViewController;
import com.netkoin.app.screens.homescreen.settings.adapters.SettingListViewAdapter;
import com.netkoin.app.servicemodels.LoginFlowServiceModel;
import com.netkoin.app.utils.Utils;
import com.netkoin.app.volly.ErrorResponse;

/**
 * Created by siddharth on 1/6/2017.
 */
public class SettingsFragment extends AbstractBaseFragment {
    private ListView settingsListView;
    private SettingListViewAdapter settingListViewAdapter;

    String[] items = {"SOUNDS", "NOTIFICATIONS", "FAQ", "DISTANCE",
            "FOLLOW US", "LOGOUT"};


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeView();
    }

    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings_layout, null, false);
        initActionBarView(R.drawable.koin_mgmnt, "Settings");
        initViews();
        registerEvents();
        setSettingsAdapter();
    }

    private void setSettingsAdapter() {
        settingListViewAdapter = new SettingListViewAdapter(getActivity(), items);
        settingsListView.setAdapter(settingListViewAdapter);
    }


    private void initViews() {
        settingsListView = (ListView) view.findViewById(R.id.settingsListView);
    }

    private void registerEvents() {
        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onSettingItemClick(position);
            }
        });
    }

    private void onSettingItemClick(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                navigationController.push(FragmentNavigationViewController.FRAGMENT_NOTIFICATION_SETTINGS, null);
                break;
            case 2:
                navigationController.push(FragmentNavigationViewController.FRAGMENT_FAQ, null);
                break;
            case 3:
                navigationController.push(FragmentNavigationViewController.FRAGMENT_DISTANCE_SETTINGS, null);
                break;
            case 4:
                navigationController.push(FragmentNavigationViewController.FRAGMENT_FOLLOW_US, null);
                break;
            case 5:
                showLogoutDialog(dailogCallback);
                break;
        }
    }

    private MyApplication.DailogCallback dailogCallback = new MyApplication.DailogCallback() {
        @Override
        public void onDailogYesClick() {
            LoginFlowServiceModel loginFlowServiceModel = new LoginFlowServiceModel(getActivity(), SettingsFragment.this);
            loginFlowServiceModel.performLogout();
        }

        @Override
        public void onDailogNoClick() {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return view;

    }

    @Override
    public void onActionBarLeftBtnClick() {
        ActivityController.getInstance().handleEvent(getActivity(), ActivityController.ACTIVITY_KOIN_MANAGEMENT_HOME_SCREEN);
    }


    @Override
    public void onActionBarTitleClick() {

    }

    @Override
    public void onRetryBtnClick() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_POST_LOGOUT:
                if (isSuccess) {
                    AppController.getInstance().logoutApp(getActivity());
                }
                break;
            default:
                break;
        }
    }
}
