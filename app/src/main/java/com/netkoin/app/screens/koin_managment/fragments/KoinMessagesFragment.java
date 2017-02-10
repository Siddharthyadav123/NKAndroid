package com.netkoin.app.screens.koin_managment.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.entities.Message;
import com.netkoin.app.screens.koin_managment.adapters.KoinMessagesListAdapter;
import com.netkoin.app.servicemodels.KoinManagementServiceModel;
import com.netkoin.app.volly.ErrorResponse;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

public class KoinMessagesFragment extends AbstractBaseFragment {

    public static final int TYPE_SENT = 0;
    public static final int TYPE_RECEIVED = 1;
    public int currentSegmentFilter = TYPE_SENT;


    private ListView messagesListView;
    private SegmentedGroup segmentGroup;
    private ProgressBar progressBarCenter;


    private ArrayList<Message> messages = null;
    private KoinManagementServiceModel koinManagementServiceModel;
    private KoinMessagesListAdapter koinMessagesListAdapter;

    public KoinMessagesFragment() {
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

        //loading messages
        koinManagementServiceModel = new KoinManagementServiceModel(getActivity(), this);
        koinManagementServiceModel.loadMessages(currentSegmentFilter);

    }

    private void updateMessagesAdapter() {
        if (koinMessagesListAdapter == null) {
            koinMessagesListAdapter = new KoinMessagesListAdapter(getActivity(), messages, currentSegmentFilter);
            messagesListView.setAdapter(koinMessagesListAdapter);
        } else {
            koinMessagesListAdapter.refreshAdapter(messages, currentSegmentFilter);
        }
    }

    private void loadSentMsgs() {
        progressBarCenter.setVisibility(View.VISIBLE);
        this.currentSegmentFilter = TYPE_SENT;
        koinManagementServiceModel.resetPage();
        progressBarListLoading.setVisibility(View.GONE);
        messagesListView.smoothScrollToPosition(0);
        koinManagementServiceModel.loadMessages(currentSegmentFilter);
    }

    private void loadRecievedMsg() {
        progressBarCenter.setVisibility(View.VISIBLE);
        this.currentSegmentFilter = TYPE_RECEIVED;
        koinManagementServiceModel.resetPage();
        progressBarListLoading.setVisibility(View.GONE);
        messagesListView.smoothScrollToPosition(0);
        koinManagementServiceModel.loadMessages(currentSegmentFilter);
    }


    private void registerEvents() {
        segmentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.nearByRadioBtn:
                            loadSentMsgs();
                            break;
                        case R.id.nationWideRadioBtn:
                            loadRecievedMsg();
                            break;
                    }
                }
            }

        });

        messagesListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //for pagination
                if (totalItemCount > 0 && canLoadMoreListItems) {
                    if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                        progressBarListLoading.setVisibility(View.VISIBLE);
                        //requesting stores
                        canLoadMoreListItems = false;
                        koinManagementServiceModel.loadMessages(currentSegmentFilter);

                    } else {
                        progressBarListLoading.setVisibility(View.GONE);
                    }
                }

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //setting page to 1
                koinManagementServiceModel.resetPage();
                koinManagementServiceModel.loadMessages(currentSegmentFilter);
            }
        });
    }


    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_message_layout, null, false);
        messagesListView = (ListView) view.findViewById(R.id.messagesListView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        segmentGroup = (SegmentedGroup) view.findViewById(R.id.segmentGroup);
        progressBarListLoading = (ProgressBar) view.findViewById(R.id.progressBarListLoading);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);

        initActionBarView(R.drawable.back, "Messages");
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
        if (retryView != null) {
            retryView.setVisibility(View.GONE);
        }
        progressBarCenter.setVisibility(View.VISIBLE);
        koinManagementServiceModel.loadMessages(currentSegmentFilter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_GET_MESSAGES:
                onMessagesResponse(isSuccess, errorResponse);
                break;
            default:
                break;
        }
    }

    private void onMessagesResponse(boolean isSuccess, ErrorResponse errorResponse) {
        progressBarListLoading.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);

        if (retryView != null) {
            retryView.setVisibility(View.GONE);
        }

        if (isSuccess) {
            if (koinManagementServiceModel.getPage() == 1) {
                messages = null;
            }

            ArrayList<Message> newMessages = koinManagementServiceModel.getMessages();
            if (messages == null) {
                messages = newMessages;
            } else {
                messages.addAll(newMessages);
            }

            updateMessagesAdapter();

            if (newMessages.size() >= koinManagementServiceModel.getLimit() && newMessages.size() > 0) {
                canLoadMoreListItems = true;
                koinManagementServiceModel.incrementPage();
            } else {
                canLoadMoreListItems = false;
            }

            //showing info window
            if (messages == null || messages.size() == 0) {
                if (currentSegmentFilter == TYPE_SENT) {
                    showRetryView("No message sent.", true);
                } else {
                    showRetryView("No message received.", true);
                }
            }

        } else {

            if (errorResponse.getErrorCode() == ErrorResponse.EROOR_CODE_INTERENT_NOT_FOUND) {

            } else if (messages == null || messages.size() == 0) {
                showRetryView(errorResponse.getErrorString(), true);
            }

        }
        progressBarCenter.setVisibility(View.GONE);
    }
}
