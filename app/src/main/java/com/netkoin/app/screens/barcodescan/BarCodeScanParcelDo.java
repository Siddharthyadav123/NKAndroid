package com.netkoin.app.screens.barcodescan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siddharth on 1/19/2017.
 */
public class BarCodeScanParcelDo implements Parcelable {
    private int scanMode;
    private int storeId;
    private long barCodeValue;
    private int productBarId;

    protected BarCodeScanParcelDo(Parcel in) {
        scanMode = in.readInt();
        storeId = in.readInt();
        barCodeValue = in.readLong();
        productBarId = in.readInt();
    }

    public BarCodeScanParcelDo() {

    }

    public static final Creator<BarCodeScanParcelDo> CREATOR = new Creator<BarCodeScanParcelDo>() {
        @Override
        public BarCodeScanParcelDo createFromParcel(Parcel in) {
            return new BarCodeScanParcelDo(in);
        }

        @Override
        public BarCodeScanParcelDo[] newArray(int size) {
            return new BarCodeScanParcelDo[size];
        }
    };

    public int getScanMode() {
        return scanMode;
    }

    public void setScanMode(int scanMode) {
        this.scanMode = scanMode;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public long getBarCodeValue() {
        return barCodeValue;
    }

    public void setBarCodeValue(long barCodeValue) {
        this.barCodeValue = barCodeValue;
    }

    public int getProductBarId() {
        return productBarId;
    }

    public void setProductBarId(int productBarId) {
        this.productBarId = productBarId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(scanMode);
        parcel.writeInt(storeId);
        parcel.writeLong(barCodeValue);
        parcel.writeInt(productBarId);
    }
}
