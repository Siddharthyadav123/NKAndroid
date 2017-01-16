package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siddharthyadav on 02/01/17.
 */

public class PurchaseBarcode implements Parcelable {
    private int id;
    private String name;
    private String short_desc;
    private int koins;
    private String valid_to;

    protected PurchaseBarcode(Parcel in) {
        id = in.readInt();
        name = in.readString();
        short_desc = in.readString();
        koins = in.readInt();
        valid_to = in.readString();
    }

    public static final Creator<PurchaseBarcode> CREATOR = new Creator<PurchaseBarcode>() {
        @Override
        public PurchaseBarcode createFromParcel(Parcel in) {
            return new PurchaseBarcode(in);
        }

        @Override
        public PurchaseBarcode[] newArray(int size) {
            return new PurchaseBarcode[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public int getKoins() {
        return koins;
    }

    public void setKoins(int koins) {
        this.koins = koins;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(short_desc);
        dest.writeInt(koins);
        dest.writeString(valid_to);
    }
}
