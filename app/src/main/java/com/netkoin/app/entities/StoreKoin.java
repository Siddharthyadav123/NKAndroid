package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashishkumarpatel on 02/01/17.
 */

public class StoreKoin implements Parcelable {

    private int id;
    private int walkins;
    private int products;
    private int purchases;

    protected StoreKoin(Parcel in) {
        id = in.readInt();
        walkins = in.readInt();
        products = in.readInt();
        purchases = in.readInt();
    }

    public static final Creator<StoreKoin> CREATOR = new Creator<StoreKoin>() {
        @Override
        public StoreKoin createFromParcel(Parcel in) {
            return new StoreKoin(in);
        }

        @Override
        public StoreKoin[] newArray(int size) {
            return new StoreKoin[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWalkins() {
        return walkins;
    }

    public void setWalkins(int walkins) {
        this.walkins = walkins;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    public int getPurchases() {
        return purchases;
    }

    public void setPurchases(int purchases) {
        this.purchases = purchases;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(walkins);
        dest.writeInt(products);
        dest.writeInt(purchases);
    }
}
