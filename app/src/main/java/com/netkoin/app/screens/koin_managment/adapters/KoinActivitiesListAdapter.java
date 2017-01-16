package com.netkoin.app.screens.koin_managment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netkoin.app.R;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.ActivityLog;
import com.netkoin.app.utils.DateTimeUtils;

import java.util.ArrayList;

/**
 * Created by siddharthyadav on 08/01/17.
 */

public class KoinActivitiesListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ActivityLog> activityLogs = null;

    public KoinActivitiesListAdapter(Context context, ArrayList<ActivityLog> activityLogs) {
        this.context = context;
        this.activityLogs = activityLogs;
    }

    @Override
    public int getCount() {
        if (activityLogs != null)
            return activityLogs.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_koin_activity_layout, null);
        }

        ImageView activityImageView = (ImageView) convertView.findViewById(R.id.activityImageView);
        TextView activityTextView = (TextView) convertView.findViewById(R.id.activityTextView);
        TextView activityTimeTextView = (TextView) convertView.findViewById(R.id.activityTimeTextView);

        ActivityLog activityLog = activityLogs.get(position);
        activityTextView.setText(activityLog.getMessage());
        activityTimeTextView.setText(DateTimeUtils.getComparedDateText(activityLog.getCreated()));
        setActivityLogImage(activityImageView, activityLog.getActivity_log_type_id(), activityLog);

        return convertView;
    }


    private void setActivityLogImage(ImageView imageView, int logTypeId, ActivityLog activityLog) {
        switch (logTypeId) {
            case 1:
                imageView.setImageResource(R.drawable.walk_ins_icon);
                break;
            case 2:
                imageView.setImageResource(R.drawable.products_icon);
                break;
            case 3:
                imageView.setImageResource(R.drawable.purchases_icon);
                break;
            case 4:
                //TODO: redeem
                if (activityLog.getLogo() != null) {
                    //load logo image
                    String logoURLString = URLConstants.URL_IMAGE + (activityLog.getLogo().getName());
                    Glide.with(context).load(logoURLString).dontAnimate()
                            .dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.logo);
                }
                break;
            case 5:
                //TODO: sending
                imageView.setImageResource(R.drawable.logo);
                break;
            case 6:
                //TODO: receiving
                imageView.setImageResource(R.drawable.logo);
                break;
            default:
                imageView.setImageResource(R.drawable.logo);
                break;
        }
    }

    public void refreshAdapter(ArrayList<ActivityLog> activityLogs) {
        this.activityLogs = activityLogs;
        notifyDataSetChanged();
    }
}
