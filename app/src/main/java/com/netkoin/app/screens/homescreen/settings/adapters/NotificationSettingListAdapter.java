package com.netkoin.app.screens.homescreen.settings.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.custom_views.switchbtn.SwitchButton;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.servicemodels.SettingsServiceModel;

/**
 * Created by ashishkumarpatel on 07/01/17.
 */

public class NotificationSettingListAdapter extends BaseAdapter {

    private Context context;
    private String[] settingItems;
    private SharedPref sharedPref;

    private String itemsKeys[] = {SharedPref.KEY_SETTING_NOTI_KOINS_REC_VIA_STEP_IN, SharedPref.KEY_SETTING_NOTI_NEAR_BY_STORES,
            SharedPref.KEY_SETTING_NOTI_KOIN_REC_VIA_TRANSFER};


    public NotificationSettingListAdapter(Context context, String[] settingItems) {
        this.context = context;
        this.settingItems = settingItems;
        sharedPref = new SharedPref(context);
    }

    @Override
    public int getCount() {
        return settingItems.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.settings_list_item, null);
        }
        ImageView leftImageView = (ImageView) view.findViewById(R.id.leftImageView);
        TextView settingTextView = (TextView) view.findViewById(R.id.settingTextView);
        SwitchButton switchBtn = (SwitchButton) view.findViewById(R.id.switchBtn);
        ImageView rightArrayImageView = (ImageView) view.findViewById(R.id.rightArrayImageView);

        settingTextView.setText(settingItems[i]);

        rightArrayImageView.setVisibility(View.GONE);
        switchBtn.setVisibility(View.VISIBLE);
        leftImageView.setVisibility(View.INVISIBLE);


        //setting sound button state
        boolean swithState = sharedPref.getBoolean(itemsKeys[i]);
        if (swithState) {
            switchBtn.setChecked(true);
        } else {
            switchBtn.setChecked(false);
        }

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPref.put(itemsKeys[i], new Boolean(b));
                SettingsServiceModel settingsServiceModel = new SettingsServiceModel(context, null);
                settingsServiceModel.requestPostSettings();
            }
        });

        return view;
    }
}
