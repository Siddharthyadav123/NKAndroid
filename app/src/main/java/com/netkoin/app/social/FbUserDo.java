package com.netkoin.app.social;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by siddharth on 1/19/2017.
 */
public class FbUserDo implements Parcelable {
    private String id;
    private String birthday;
    private String gender;
    private String name;
    private String email;
    private String profilePicUrl;
    private String fbid;
    private String token;

    protected FbUserDo(Parcel in) {
        id = in.readString();
        birthday = in.readString();
        gender = in.readString();
        name = in.readString();
        email = in.readString();
        profilePicUrl = in.readString();
        fbid = in.readString();
        token = in.readString();
    }

    public FbUserDo() {
    }


    public static final Creator<FbUserDo> CREATOR = new Creator<FbUserDo>() {
        @Override
        public FbUserDo createFromParcel(Parcel in) {
            return new FbUserDo(in);
        }

        @Override
        public FbUserDo[] newArray(int size) {
            return new FbUserDo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(birthday);
        parcel.writeString(gender);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(profilePicUrl);
        parcel.writeString(fbid);
        parcel.writeString(token);
    }

    public void parseJsonDataForFacebook(JSONObject object) {
        if (object != null) {
            try {
                if (object.has("id")) {
                    setFbid((String) object.get("id"));
                }
                if (object.has("birthday")) {
                    setBirthday((String) object.get("birthday"));
                }
                if (object.has("gender")) {
                    setGender((String) object.get("gender"));
                }
                if (object.has("name")) {
                    setName((String) object.get("name"));
                }
                if (object.has("email")) {
                    setEmail((String) object.get("email"));
                }
                if (object.has("picture")) {
                    setProfilePicUrl("https://graph.facebook.com/" + getFbid() + "/picture?width=250&height=250");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
