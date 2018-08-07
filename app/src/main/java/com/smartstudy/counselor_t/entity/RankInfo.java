package com.smartstudy.counselor_t.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author yqy
 * @date on 2018/7/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class RankInfo implements Parcelable {

    private String title;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.type);
    }

    public RankInfo() {
    }

    protected RankInfo(Parcel in) {
        this.title = in.readString();
        this.type = in.readString();
    }

    public static final Creator<RankInfo> CREATOR = new Creator<RankInfo>() {
        public RankInfo createFromParcel(Parcel source) {
            return new RankInfo(source);
        }

        public RankInfo[] newArray(int size) {
            return new RankInfo[size];
        }
    };
}

