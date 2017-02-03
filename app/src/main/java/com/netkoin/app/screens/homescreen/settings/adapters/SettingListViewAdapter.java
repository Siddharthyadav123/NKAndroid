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

public class SettingListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] settingItems;
    private SharedPref sharedPref;

    public SettingListViewAdapter(Context context, String[] settingItems) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.settings_list_item, null);
        }
        ImageView leftImageView = (ImageView) view.findViewById(R.id.leftImageView);
        TextView settingTextView = (TextView) view.findViewById(R.id.settingTextView);
        SwitchButton switchBtn = (SwitchButton) view.findViewById(R.id.switchBtn);
        ImageView rightArrayImageView = (ImageView) view.findViewById(R.id.rightArrayImageView);

        settingTextView.setText(settingItems[i]);

        if (i == 0) {
            rightArrayImageView.setVisibility(View.GONE);

            //setting sound button state
            boolean soundSwitchState = sharedPref.getBoolean(SharedPref.KEY_SETTING_SOUND_ENABLED);
            switchBtn.setVisibility(View.VISIBLE);
            if (soundSwitchState) {
                switchBtn.setChecked(true);
            } else {
                switchBtn.setChecked(false);
            }

            switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    sharedPref.put(SharedPref.KEY_SETTING_SOUND_ENABLED, new Boolean(b));
                }
            });

        } else {
            rightArrayImageView.setVisibility(View.VISIBLE);
            switchBtn.setVisibility(View.GONE);
        }

        if (i == 4) {
            leftImageView.setVisibility(View.INVISIBLE);
        } else {
            leftImageView.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
