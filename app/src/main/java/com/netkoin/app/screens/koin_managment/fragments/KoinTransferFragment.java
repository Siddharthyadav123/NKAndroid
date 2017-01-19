package com.netkoin.app.screens.koin_managment.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.servicemodels.KoinManagementServiceModel;
import com.netkoin.app.utils.Utils;

/**
 * Created by siddharthyadav on 08/01/17.
 */

public class KoinTransferFragment extends AbstractBaseFragment {


    private EditText receiverEmailEditText;
    private EditText amountEditText;
    private EditText msgEditText;
    private TextView confirmBtnTextView;

    private KoinManagementServiceModel koinManagementServiceModel;


    public KoinTransferFragment() {
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

        koinManagementServiceModel = new KoinManagementServiceModel(getActivity(), this);
    }


    private void registerEvents() {
        confirmBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    koinManagementServiceModel.transeferKoin(Integer.parseInt(amountEditText.getText().toString().trim()),
                            receiverEmailEditText.getText().toString().trim(),
                            msgEditText.getText().toString().trim());
                }
            }
        });
    }

    private boolean isValidate() {
        if (receiverEmailEditText.getText().toString().trim().length() == 0) {
            showAleartPosBtnOnly(null, "Alert", "Please enter receiver's email");
            return false;

        } else if (amountEditText.getText().toString().trim().length() == 0) {
            showAleartPosBtnOnly(null, "Alert", "Please enter amount to be transfered");
            return false;

        } else if (msgEditText.getText().toString().trim().length() == 0) {
            showAleartPosBtnOnly(null, "Alert", "Please enter some message");
            return false;
        }
        return true;
    }

    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_koin_transfer_layout, null, false);
        receiverEmailEditText = (EditText) view.findViewById(R.id.receiverEmailEditText);
        amountEditText = (EditText) view.findViewById(R.id.amountEditText);
        msgEditText = (EditText) view.findViewById(R.id.msgEditText);

        confirmBtnTextView = (TextView) view.findViewById(R.id.confirmBtnTextView);

        initActionBarView(R.drawable.back, "Transfer Koins");
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
        switch (requestId) {
            case RequestConstants.REQUEST_ID_POST_TRANSFER_KOINS:
                onTransferKoinResponse(isSuccess, result, errorString);
            default:
                break;
        }
    }

    private void onTransferKoinResponse(boolean isSuccess, Object result, String errorString) {
        if (isSuccess) {
            Utils.getInstance().showSnackBar(getActivity(), (String) result);
        } else {
            Utils.getInstance().showSnackBar(getActivity(), errorString);
        }
    }
}
