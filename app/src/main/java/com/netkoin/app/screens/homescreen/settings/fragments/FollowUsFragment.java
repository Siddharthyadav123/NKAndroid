package com.netkoin.app.screens.homescreen.settings.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.screens.homescreen.settings.adapters.FollowUsListViewAdapter;
import com.netkoin.app.utils.Utils;

/**
 * Created by siddharthyadav on 28/01/17.
 */

public class FollowUsFragment extends AbstractBaseFragment {

    private ListView followUsListview;
    private FollowUsListViewAdapter followUsListViewAdapter;

    String[] items = {"FACEBOOK", "TWITTER", "INSTAGRAM"};

    public FollowUsFragment() {
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
        followUsListViewAdapter = new FollowUsListViewAdapter(getActivity(), items);
        followUsListview.setAdapter(followUsListViewAdapter);
    }

    private void registerEvents() {

    }


    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_notification_layout, null, false);
        followUsListview = (ListView) view.findViewById(R.id.notificationSetttingListView);
        initActionBarView(R.drawable.back, "Follow us");

        followUsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Utils.getInstance().openBrowser(getActivity(), URLConstants.URL_FACEBOOK_NETKOIN);
                        break;
                    case 1:
                        Utils.getInstance().openBrowser(getActivity(), URLConstants.URL_TWITTER_NETKOIN);
                        break;
                    case 2:
                        Utils.getInstance().openBrowser(getActivity(), URLConstants.URL_INSTAGRAM_NETKOIN);
                        break;
                    default:
                        break;
                }
            }
        });
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
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {

    }
}