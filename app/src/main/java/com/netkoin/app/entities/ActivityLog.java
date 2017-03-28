package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashishkumarpatel on 02/01/17.
 */

public class ActivityLog implements Parcelable {
    private int id;
    private String name;
    private String short_desc;
    private int activity_log_type_id;
    private String message;
    private int logo_id;
    private Logo logo;
    private String created;

    protected ActivityLog(Parcel in) {
        id = in.readInt();
        name = in.readString();
        short_desc = in.readString();
        activity_log_type_id = in.readInt();
        message = in.readString();
        logo_id = in.readInt();
        logo = in.readParcelable(Logo.class.getClassLoader());
        created = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(short_desc);
        dest.writeInt(activity_log_type_id);
        dest.writeString(message);
        dest.writeInt(logo_id);
        dest.writeParcelable(logo, flags);
        dest.writeString(created);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivityLog> CREATOR = new Creator<ActivityLog>() {
        @Override
        public ActivityLog createFromParcel(Parcel in) {
            return new ActivityLog(in);
        }

        @Override
        public ActivityLog[] newArray(int size) {
            return new ActivityLog[size];
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

    public int getActivity_log_type_id() {
        return activity_log_type_id;
    }

    public void setActivity_log_type_id(int activity_log_type_id) {
        this.activity_log_type_id = activity_log_type_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLogo_id() {
        return logo_id;
    }

    public void setLogo_id(int logo_id) {
        this.logo_id = logo_id;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
