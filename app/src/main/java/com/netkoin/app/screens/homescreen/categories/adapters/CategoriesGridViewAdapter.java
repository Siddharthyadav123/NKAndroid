package com.netkoin.app.screens.homescreen.categories.adapters;

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
import com.netkoin.app.entities.MainCategory;

import java.util.ArrayList;

public class CategoriesGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MainCategory> categories = null;

    public CategoriesGridViewAdapter(Context context, ArrayList<MainCategory> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        if (categories != null)
            return categories.size();
        else
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.category_grid_item_layout, null);
        }
        ImageView categoryImageView = (ImageView) view.findViewById(R.id.categoryImageView);
        TextView categoryTextView = (TextView) view.findViewById(R.id.categoryTextView);

        MainCategory category = categories.get(i);

        String imageURL = URLConstants.URL_IMAGE + category.getLogo().getName();
        Glide.with(context).load(imageURL).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).into(categoryImageView);

        categoryTextView.setText(category.getName());
        return view;
    }
}
