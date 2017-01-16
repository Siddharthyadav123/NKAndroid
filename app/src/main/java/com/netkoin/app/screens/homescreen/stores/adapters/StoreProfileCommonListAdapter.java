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
import com.netkoin.app.entities.Catalogue;
import com.netkoin.app.entities.ProductBarcode;
import com.netkoin.app.entities.PurchaseBarcode;
import com.netkoin.app.utils.DateTimeUtils;

import java.util.ArrayList;

/**
 * Created by siddharthyadav on 07/01/17.
 */

public class StoreProfileCommonListAdapter extends BaseAdapter {
    public static final int TYPE_PRODUCT = 0;
    public static final int TYPE_PURCHASE = 1;
    public static final int TYPE_CATALOUGE = 2;

    private Context context;
    private int type;

    private ArrayList<Catalogue> catalouges = null;
    private ArrayList<ProductBarcode> productBarcodes = null;
    private ArrayList<PurchaseBarcode> purchaseBarcodes = null;


    public StoreProfileCommonListAdapter(Context context, Object item, int type) {
        this.context = context;
        this.type = type;

        switch (type) {
            case TYPE_PRODUCT:
                productBarcodes = (ArrayList<ProductBarcode>) item;
                break;
            case TYPE_PURCHASE:
                purchaseBarcodes = (ArrayList<PurchaseBarcode>) item;
                break;
            case TYPE_CATALOUGE:
                catalouges = (ArrayList<Catalogue>) item;
                break;
        }
    }


    @Override
    public int getCount() {
        if (type == TYPE_CATALOUGE && catalouges != null) {
            return catalouges.size();
        } else if (type == TYPE_PURCHASE && purchaseBarcodes != null) {
            return purchaseBarcodes.size();
        } else if (type == TYPE_PRODUCT && productBarcodes != null) {
            return productBarcodes.size();
        }
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
            convertView = LayoutInflater.from(context).inflate(R.layout.product_list_item_layout, null);
        }
        if (type == TYPE_CATALOUGE) {
            feedCatalougeInfo(convertView, position);
        } else if (type == TYPE_PURCHASE) {
            feedPurchaseInfo(convertView, position);
        } else {
            feedProductInfo(convertView, position);
        }

        return convertView;
    }

    private void feedCatalougeInfo(View convertView, int position) {
        TextView productNameTextView = (TextView) convertView.findViewById(R.id.productNameTextView);
        TextView productAddrsTextView = (TextView) convertView.findViewById(R.id.productAddrsTextView);
        TextView dateTimeTextView = (TextView) convertView.findViewById(R.id.dateTimeTextView);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
        ImageView productImageView = (ImageView) convertView.findViewById(R.id.productImageView);

        Catalogue catalouge = catalouges.get(position);
        productNameTextView.setText(catalouge.getName());
        productAddrsTextView.setText(catalouge.getShort_desc());
        priceTextView.setText(catalouge.getKoins() + "");

        //load logo image
        if (catalouge.getBanner() != null) {
            String bannerURLString = URLConstants.URL_IMAGE + catalouge.getBanner().getName();
            Glide.with(context).load(bannerURLString).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).into(productImageView);

        }
        dateTimeTextView.setText(DateTimeUtils.remainingTime(catalouge.getValid_to()));
    }

    private void feedProductInfo(View convertView, int position) {
        TextView productNameTextView = (TextView) convertView.findViewById(R.id.productNameTextView);
        TextView productAddrsTextView = (TextView) convertView.findViewById(R.id.productAddrsTextView);
        TextView dateTimeTextView = (TextView) convertView.findViewById(R.id.dateTimeTextView);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
        ImageView productImageView = (ImageView) convertView.findViewById(R.id.productImageView);

        ProductBarcode productBarcode = productBarcodes.get(position);
        productNameTextView.setText(productBarcode.getName());
        productAddrsTextView.setText(productBarcode.getShort_desc());
        priceTextView.setText(productBarcode.getKoins() + "");

        productImageView.setImageResource(R.drawable.barcode);
        dateTimeTextView.setText(DateTimeUtils.remainingTime(productBarcode.getValid_to()));
    }

    private void feedPurchaseInfo(View convertView, int position) {
        TextView productNameTextView = (TextView) convertView.findViewById(R.id.productNameTextView);
        TextView productAddrsTextView = (TextView) convertView.findViewById(R.id.productAddrsTextView);
        TextView dateTimeTextView = (TextView) convertView.findViewById(R.id.dateTimeTextView);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
        ImageView productImageView = (ImageView) convertView.findViewById(R.id.productImageView);

        PurchaseBarcode purchaseBarcode = purchaseBarcodes.get(position);
        productNameTextView.setText(purchaseBarcode.getName());
        productAddrsTextView.setText(purchaseBarcode.getShort_desc());
        priceTextView.setText(purchaseBarcode.getKoins() + "");

        productImageView.setImageResource(R.drawable.cash);
        dateTimeTextView.setText(DateTimeUtils.remainingTime(purchaseBarcode.getValid_to()));
    }


    public void refreshCatalougeAdapter(ArrayList<Catalogue> catalouges) {
        this.catalouges = catalouges;
        notifyDataSetChanged();
    }

    public void refreshProductAdapter(ArrayList<ProductBarcode> productBarcodes) {
        this.productBarcodes = productBarcodes;
        notifyDataSetChanged();
    }

    public void refreshPurchaseAdapter(ArrayList<PurchaseBarcode> purchaseBarcodes) {
        this.purchaseBarcodes = purchaseBarcodes;
        notifyDataSetChanged();
    }


}
