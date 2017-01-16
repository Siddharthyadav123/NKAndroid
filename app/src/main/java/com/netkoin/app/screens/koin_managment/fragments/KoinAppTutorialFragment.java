package com.netkoin.app.screens.koin_managment.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;

/**
 * Created by siddharthyadav on 08/01/17.
 */

public class KoinAppTutorialFragment extends AbstractBaseFragment {


    private WebView faqWebView;

    public KoinAppTutorialFragment() {
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
        loadWebView();
    }

    private void loadWebView() {
        faqWebView.getSettings().setJavaScriptEnabled(true);
        faqWebView.loadUrl("http://www.netkoin.com");
    }

    private void registerEvents() {
        faqWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                progressBar.setVisibility(View.VISIBLE);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), "Page Loading failed, " + description, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_faq_layout, null, false);
        faqWebView = (WebView) view.findViewById(R.id.faqWebView);
        initActionBarView(R.drawable.back, "App Tutorial");
        progressBar.setVisibility(View.VISIBLE);
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
