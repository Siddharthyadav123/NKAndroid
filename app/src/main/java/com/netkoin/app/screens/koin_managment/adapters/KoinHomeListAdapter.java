package com.netkoin.app.screens.koin_managment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.custom_views.switchbtn.SwitchButton;

/**
 * Created by ashishkumarpatel on 08/01/17.
 */

public class KoinHomeListAdapter extends BaseAdapter {

    private Context context;
    private String[] settingItems;


    private int actvityLogCount = 0;

    public KoinHomeListAdapter(Context context, String[] settingItems, int actvityLogCount) {
        this.context = context;
        this.settingItems = settingItems;
        this.actvityLogCount = actvityLogCount;
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
        TextView activityCountTextView = (TextView) view.findViewById(R.id.activityCountTextView);

        settingTextView.setText(settingItems[i]);
        leftImageView.setImageResource(R.drawable.dot);
        switchBtn.setVisibility(View.GONE);

        if (i == 0 && actvityLogCount > 0) {
            activityCountTextView.setText(actvityLogCount + "");
            activityCountTextView.setVisibility(View.VISIBLE);
        } else {
            activityCountTextView.setVisibility(View.GONE);
        }


        leftImageView.setPadding(15, 15, 15, 15);

        return view;
    }

    public void refreshAdapter(int actvityLogCount) {
        this.actvityLogCount = actvityLogCount;
        notifyDataSetChanged();
    }
}
