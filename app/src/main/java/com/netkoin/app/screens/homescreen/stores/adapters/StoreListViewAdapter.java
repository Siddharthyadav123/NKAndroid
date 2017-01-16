package com.netkoin.app.screens.homescreen.stores.adapters;

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
import com.netkoin.app.controller.ActivityController;
import com.netkoin.app.entities.Store;
import com.netkoin.app.utils.Utils;

import java.util.ArrayList;

/**
 * Created by siddharth on 1/5/2017.
 */
public class StoreListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Store> storeArrayList = new ArrayList<>();

    public StoreListViewAdapter(Context context, ArrayList<Store> storeArrayList) {
        this.context = context;
        this.storeArrayList = storeArrayList;
    }

    @Override
    public int getCount() {
        if (storeArrayList != null) {
            return storeArrayList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    static class ViewHolder {
        TextView currentLocationTextView;
        TextView kmTextView;
        TextView storeNameTextView;
        TextView storeDesTextView;
        TextView walkInCountTextView;
        TextView productCountTextView;
        TextView purchaseCountTextView;
        ImageView storeLogoImageView;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.store_list_item_layout, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.currentLocationTextView = (TextView) view.findViewById(R.id.currentLocationTextView);
            viewHolder.kmTextView = (TextView) view.findViewById(R.id.kmTextView);
            viewHolder.storeNameTextView = (TextView) view.findViewById(R.id.storeNameTextView);
            viewHolder.storeDesTextView = (TextView) view.findViewById(R.id.storeDesTextView);
            viewHolder.walkInCountTextView = (TextView) view.findViewById(R.id.walkInCountTextView);
            viewHolder.productCountTextView = (TextView) view.findViewById(R.id.productCountTextView);
            viewHolder.purchaseCountTextView = (TextView) view.findViewById(R.id.purchaseCountTextView);
            viewHolder.storeLogoImageView = (ImageView) view.findViewById(R.id.storeLogoImageView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        Store store = storeArrayList.get(pos);
        String imageURL = URLConstants.URL_IMAGE + store.getLogo().getName();
        Glide.with(context).load(imageURL).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.storeLogoImageView);

        //setting store details
        viewHolder.storeNameTextView.setText(store.getName());
        viewHolder.storeDesTextView.setText(store.getShort_desc());
        viewHolder.kmTextView.setText(Utils.getInstance().refineDistanceText(store.getDistance()));

        //setting counts
        if (store.getStore_koins() != null && store.getStore_koins().size() > 0) {
            viewHolder.walkInCountTextView.setText(store.getStore_koins().get(0).getWalkins() + "");
            viewHolder.productCountTextView.setText(store.getStore_koins().get(0).getProducts() + "");
            viewHolder.purchaseCountTextView.setText(store.getStore_koins().get(0).getPurchases() + "");
        }

        if (pos == 0) {
            viewHolder.currentLocationTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.currentLocationTextView.setVisibility(View.GONE);
        }

        viewHolder.currentLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.getInstance().handleEvent(context, ActivityController.ACTIVITY_SEARCH_LOCATION);
            }
        });
        return view;
    }


    public void refreshAdapter(ArrayList<Store> storeArrayList) {
        this.storeArrayList = storeArrayList;
        notifyDataSetChanged();
    }
}
