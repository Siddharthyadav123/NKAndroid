package com.netkoin.app.screens.homescreen.settings.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.pref.SharedPref;

/**
 * Created by siddharthyadav on 07/01/17.
 */

public class DistanceSettingListAdapter extends BaseAdapter {

    private Context context;
    private String[] items;

    private int openItemIndex = -1;
    private SharedPref sharedPref;
    private String[] itemsKeys = {SharedPref.KEY_SETTING_DIS_NEAR_BY_STORES, SharedPref.KEY_SETTING_DIS_NEAR_BY_TRENDING_ADS,
            SharedPref.KEY_SETTING_DIS_CAT_ADS};


    public DistanceSettingListAdapter(Context context, String[] items) {
        this.context = context;
        this.items = items;
        sharedPref = new SharedPref(context);
    }

    @Override
    public int getCount() {
        return items.length;
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
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.distance_list_item_layout, null);
        }

        //init views
        RelativeLayout headerRilLayout = (RelativeLayout) view.findViewById(R.id.headerRilLayout);
        RelativeLayout footerRilLayout = (RelativeLayout) view.findViewById(R.id.footerRilLayout);
        ImageView rightArrayImageView = (ImageView) view.findViewById(R.id.rightArrayImageView);
        TextView settingTextView = (TextView) view.findViewById(R.id.settingTextView);
        TextView currentKMTextView = (TextView) view.findViewById(R.id.currentKMTextView);
        final TextView selectedDistanceTextView = (TextView) view.findViewById(R.id.selectedDistanceTextView);
        TextView saveBtnTextView = (TextView) view.findViewById(R.id.saveBtnTextView);
        final SeekBar distanceSeekbar = (SeekBar) view.findViewById(R.id.distanceSeekbar);

        //setting seekbar style
        distanceSeekbar.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.theamPurpleDark), PorterDuff.Mode.MULTIPLY);

        //setting data
        currentKMTextView.setText(sharedPref.getInt(itemsKeys[pos]) + " KM");
        distanceSeekbar.setProgress(sharedPref.getInt(itemsKeys[pos]));
        selectedDistanceTextView.setText(sharedPref.getInt(itemsKeys[pos]) + " KM");

        //setting header text
        settingTextView.setText(items[pos]);

        //hader open close logic
        if (openItemIndex != -1) {
            if (openItemIndex == pos) {
                footerRilLayout.setVisibility(View.VISIBLE);
                view.setBackgroundColor(context.getResources().getColor(R.color.LightGray));
                rightArrayImageView.setRotation(90);
            } else {
                footerRilLayout.setVisibility(View.GONE);
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                rightArrayImageView.setRotation(0);
            }
        } else {
            footerRilLayout.setVisibility(View.GONE);
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
            rightArrayImageView.setRotation(0);
        }

        //makeing header open n close
        headerRilLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHeaderClick(pos);
            }
        });


        //setting progress
        distanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedDistanceTextView.setText(progress + " KM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //perform action on save button click
        saveBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.put(itemsKeys[pos], new Integer(distanceSeekbar.getProgress()));
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private void onHeaderClick(int pos) {
        if (pos == openItemIndex) {
            openItemIndex = -1;
        } else {
            openItemIndex = pos;
        }

        notifyDataSetChanged();
    }
}
