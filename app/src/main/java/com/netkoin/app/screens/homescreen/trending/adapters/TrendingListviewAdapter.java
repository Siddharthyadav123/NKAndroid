package com.netkoin.app.screens.homescreen.trending.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netkoin.app.R;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.Ads;
import com.netkoin.app.utils.DateTimeUtils;
import com.netkoin.app.utils.Utils;

import java.util.ArrayList;

/**
 * Created by ashishkumarpatel on 05/01/17.
 */

public class TrendingListviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Ads> trendingAds = null;
    private boolean isNearBy = true;

    public TrendingListviewAdapter(Context context, ArrayList<Ads> trendingAds) {
        this.context = context;
        this.trendingAds = trendingAds;
    }

    @Override
    public int getCount() {
        if (trendingAds != null)
            return trendingAds.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        ImageView bannerImageView;
        ImageView logoImageView;
        ImageView locImageView;

        TextView dateTimeTextView;
        TextView kmTextView;
        TextView footerTextView;

        RelativeLayout locRL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.trending_list_item_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.bannerImageView = (ImageView) convertView.findViewById(R.id.bannerImageView);
            viewHolder.logoImageView = (ImageView) convertView.findViewById(R.id.logoImageView);
            viewHolder.locImageView = (ImageView) convertView.findViewById(R.id.locImageView);

            viewHolder.dateTimeTextView = (TextView) convertView.findViewById(R.id.dateTimeTextView);
            viewHolder.kmTextView = (TextView) convertView.findViewById(R.id.kmTextView);
            viewHolder.footerTextView = (TextView) convertView.findViewById(R.id.footerTextView);

            viewHolder.locRL = (RelativeLayout) convertView.findViewById(R.id.locRL);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Ads ads = trendingAds.get(position);
        viewHolder.footerTextView.setText(ads.getName());

        //load store logo image
        String logoURLString = URLConstants.URL_IMAGE + ads.getLogo().getName();
        Glide.with(context).load(logoURLString).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.logoImageView);

        //load store banner image
        String bannerURLString = URLConstants.URL_IMAGE + ads.getBanner().getName();
        Glide.with(context).load(bannerURLString).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.bannerImageView);

        viewHolder.dateTimeTextView.setText(DateTimeUtils.remainingTime(ads.getValid_to() + ""));

        if (!isNearBy) {
            viewHolder.locRL.setVisibility(View.GONE);
        } else {
            viewHolder.locRL.setVisibility(View.VISIBLE);
            if (ads.getDistance() != null) {
                viewHolder.kmTextView.setText(Utils.getInstance().refineDistanceText(ads.getDistance()));
            }
        }


        return convertView;
    }

    public void refreshAdapter(ArrayList<Ads> trendingAds) {
        this.trendingAds = trendingAds;
        notifyDataSetChanged();

    }


    public void setNearBy(boolean nearBy) {
        isNearBy = nearBy;
    }
}
