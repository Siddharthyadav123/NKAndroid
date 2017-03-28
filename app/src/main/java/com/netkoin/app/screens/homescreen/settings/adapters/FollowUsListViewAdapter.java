package com.netkoin.app.screens.homescreen.settings.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.custom_views.switchbtn.SwitchButton;
import com.netkoin.app.pref.SharedPref;

/**
 * Created by ashishkumarpatel on 28/01/17.
 */

public class FollowUsListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] followUsItems;
    private SharedPref sharedPref;

    private int[] itemImages = {R.drawable.facebook, R.drawable.twitter, R.drawable.instagram};


    public FollowUsListViewAdapter(Context context, String[] followUsItems) {
        this.context = context;
        this.followUsItems = followUsItems;
        sharedPref = new SharedPref(context);
    }

    @Override
    public int getCount() {
        return followUsItems.length;
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

        settingTextView.setText(followUsItems[i]);

        rightArrayImageView.setVisibility(View.VISIBLE);
        switchBtn.setVisibility(View.GONE);

        leftImageView.setImageResource(itemImages[i]);

        return view;
    }
}

