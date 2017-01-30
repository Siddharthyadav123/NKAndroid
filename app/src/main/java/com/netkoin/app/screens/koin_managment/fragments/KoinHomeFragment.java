package com.netkoin.app.screens.koin_managment.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.controller.AppController;
import com.netkoin.app.controller.FragmentNavigationViewController;
import com.netkoin.app.entities.TotalKoin;
import com.netkoin.app.screens.koin_managment.adapters.KoinHomeListAdapter;
import com.netkoin.app.servicemodels.KoinManagementServiceModel;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by siddharthyadav on 08/01/17.
 */

public class KoinHomeFragment extends AbstractBaseFragment {

    private KoinHomeListAdapter koinHomeListAdapter;

    private ListView koinOptionListView;

    private SegmentedGroup segmentGroup;
    private TextView koinCountTextView;

    private String[] items = {"ACTIVITY", "MESSAGES", "TRANSFER KOINS", "APP TUTORIAL"};

    private KoinManagementServiceModel koinManagementServiceModel;


    public KoinHomeFragment() {
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
        registerEvents();

        //setting the total koin
        TotalKoin totalKoins = AppController.getInstance().getModelFacade().getLocalModel().getTotalKoins();
        koinCountTextView.setText(totalKoins.getTotal_koins() + "");

        //requesting activity logs and unread counts
        koinManagementServiceModel = new KoinManagementServiceModel(getActivity(), this);
        koinManagementServiceModel.loadActivityLogs();

        updateAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (koinManagementServiceModel != null) {
            koinManagementServiceModel.loadActivityLogsUnreadCount();
            koinManagementServiceModel.loadTotalKoins();
        }

    }

    private void updateAdapter() {
        if (koinHomeListAdapter == null) {
            koinHomeListAdapter = new KoinHomeListAdapter(getActivity(), items, koinManagementServiceModel.getUnreadActivityLogscount());
            koinOptionListView.setAdapter(koinHomeListAdapter);
        } else {
            koinHomeListAdapter.refreshAdapter(koinManagementServiceModel.getUnreadActivityLogscount());
        }
    }


    private void registerEvents() {
        segmentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                refreshKoinCount(checkedId);

            }

        });

        koinOptionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        navigationController.push(FragmentNavigationViewController.FRAGMENT_KOIN_ACTIVITY, null);
                        break;
                    case 1:
                        navigationController.push(FragmentNavigationViewController.FRAGMENT_KOIN_MESSAGES, null);
                        break;
                    case 2:
                        navigationController.push(FragmentNavigationViewController.FRAGMENT_KOIN_TRANSFER, null);
                        break;
                    case 3:
                        navigationController.push(FragmentNavigationViewController.FRAGMENT_KOIN_APP_TUTORIAL, null);
                        break;
                }
            }
        });
    }


    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_koin_home_layout, null, false);
        koinOptionListView = (ListView) view.findViewById(R.id.koinOptionListView);
        segmentGroup = (SegmentedGroup) view.findViewById(R.id.segmentGroup);
        koinCountTextView = (TextView) view.findViewById(R.id.koinCountTextView);
        initActionBarView(R.drawable.back, "Koin Management");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return view;

    }

    @Override
    public void onActionBarLeftBtnClick() {
        getActivity().finish();
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

    public void refreshKoinCount(int selectedSegmentId) {
        TotalKoin totalKoins = AppController.getInstance().getModelFacade().getLocalModel().getTotalKoins();
        switch (selectedSegmentId) {
            case R.id.dayRadioBtn:
                koinCountTextView.setText(totalKoins.getEarned_koins_last_1_day() + "");
                break;
            case R.id.currentRadioBtn:
                koinCountTextView.setText(totalKoins.getTotal_koins() + "");
                break;
            case R.id.monthRadioBtn:
                koinCountTextView.setText(totalKoins.getEarned_koins_last_30_days() + "");
                break;
        }
    }


    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_GET_TOTAL_KOIN:
                refreshKoinCount(segmentGroup.getCheckedRadioButtonId());
                break;
            case RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS_UREAD_COUNT:
                updateAdapter();
                break;
            default:
                break;
        }
    }


}

