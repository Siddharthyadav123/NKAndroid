package com.netkoin.app.base_classes;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.constants.Constants;
import com.netkoin.app.controller.FragmentNavigationViewController;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.utils.Utils;
import com.netkoin.app.volly.APIHandlerCallback;



public abstract class AbstractBaseFragment extends Fragment implements View.OnClickListener, APIHandlerCallback {

    protected View view;
    protected ImageView leftBtnImageView;
    protected TextView titleTextView;
    protected ProgressBar progressBar;

    protected boolean canLoadMoreListItems = false;
    protected ProgressBar progressBarListLoading;

    protected Object parcelExtras;

    protected SwipeRefreshLayout refreshLayout;
    protected View retryView;
    protected SharedPref sharedPref;

    protected FragmentNavigationViewController navigationController;

    protected String previousLocation = null;


    public FragmentNavigationViewController getNavigationController() {
        return navigationController;
    }

    public void setNavigationController(FragmentNavigationViewController navigationController) {
        this.navigationController = navigationController;
    }

    protected void showRefreshingIfRequestPending() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setRefreshing(true);
                }
            });
        }
    }


    protected void initActionBarView(int leftImage, @NonNull String title) {
        leftBtnImageView = (ImageView) view.findViewById(R.id.leftBtnImageView);
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        leftBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionBarLeftBtnClick();
            }
        });

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionBarTitleClick();
            }
        });
        leftBtnImageView.setImageResource(leftImage);
        titleTextView.setText(title);
    }

    public void setActionBarTitle(String title) {
        titleTextView.setText(title);
    }

    public abstract void onActionBarLeftBtnClick();

    public abstract void onActionBarTitleClick();

    public Object getParcelExtras() {
        return parcelExtras;
    }

    public void setParcelExtras(Object parcelExtras) {
        this.parcelExtras = parcelExtras;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(getActivity());
        previousLocation = sharedPref.getString(SharedPref.KEY_SELECTED_LOC);


        if (previousLocation == null) {
            previousLocation = Constants.CURRENT_LOCATION_TEXT;
            sharedPref.put(SharedPref.KEY_SELECTED_LOC, Constants.CURRENT_LOCATION_TEXT);
        }
    }

    @Override
    public void onDestroyView() {
        if (view != null && view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        super.onDestroyView();
    }

    protected void showAleartPosBtnOnly(final MyApplication.DailogCallback dailogCallback,
                                        String title, String bodyText) {
        Utils.getInstance().hideKeyboard(getActivity());
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(bodyText);
        builder1.setTitle(title);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (dailogCallback != null)
                            dailogCallback.onDailogYesClick();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    protected void showLogoutDialog(final MyApplication.DailogCallback dailogCallback) {
        Utils.getInstance().hideKeyboard(getActivity());
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Do you want to logout ?");
        builder1.setTitle("Logout");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Logout",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (dailogCallback != null)
                            dailogCallback.onDailogYesClick();
                    }
                });

        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    protected void showRetryView(String infoText, boolean needRetryButton) {
        if (retryView == null) {
            retryView = getActivity().getLayoutInflater().inflate(R.layout.retry_layout, null);
            TextView infoTextView = (TextView) retryView.findViewById(R.id.infoTextView);
            TextView retryBtnTextView = (TextView) retryView.findViewById(R.id.retryBtnTextView);

            infoTextView.setText(infoText);
            retryBtnTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRetryBtnClick();
                }
            });

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            retryView.setLayoutParams(layoutParams);

            ((RelativeLayout) view).addView(retryView);
            if (needRetryButton) {
                retryBtnTextView.setVisibility(View.VISIBLE);
            } else {
                retryBtnTextView.setVisibility(View.GONE);
            }

        } else {
            retryView.setVisibility(View.VISIBLE);
            TextView retryBtnTextView = (TextView) retryView.findViewById(R.id.retryBtnTextView);
            TextView infoTextView = (TextView) retryView.findViewById(R.id.infoTextView);
            infoTextView.setText(infoText);

            retryBtnTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRetryBtnClick();
                }
            });

            if (needRetryButton) {
                retryBtnTextView.setVisibility(View.VISIBLE);
            } else {
                retryBtnTextView.setVisibility(View.GONE);
            }
        }

    }

    public abstract void onRetryBtnClick();


}
