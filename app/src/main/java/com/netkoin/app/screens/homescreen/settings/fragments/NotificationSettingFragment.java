package com.netkoin.app.screens.homescreen.settings.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.screens.homescreen.settings.adapters.NotificationSettingListAdapter;
import com.netkoin.app.volly.ErrorResponse;

/**
 * Created by ashishkumarpatel on 07/01/17.
 */

public class NotificationSettingFragment extends AbstractBaseFragment {

    private ListView notificationSetttingListView;
    private NotificationSettingListAdapter notificationSettingListAdapter;

    String[] items = {"KOINS RECIEVED VIA STEP-IN'S", "NEARBY STORES", "RECIEVED KOINS VIA TRANSFER"};

    public NotificationSettingFragment() {
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
        setNoficationSettingAdapter();
        registerEvents();
    }

    private void setNoficationSettingAdapter() {
        notificationSettingListAdapter = new NotificationSettingListAdapter(getActivity(), items);
        notificationSetttingListView.setAdapter(notificationSettingListAdapter);
    }

    private void registerEvents() {

    }


    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_notification_layout, null, false);
        notificationSetttingListView = (ListView) view.findViewById(R.id.notificationSetttingListView);
        initActionBarView(R.drawable.back, "Notifications");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        showRefreshingIfRequestPending();
        return view;

    }

    @Override
    public void onActionBarLeftBtnClick() {
        navigationController.pop();
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

    }
}
