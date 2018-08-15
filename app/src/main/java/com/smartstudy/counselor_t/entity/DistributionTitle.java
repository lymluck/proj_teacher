package com.smartstudy.counselor_t.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author yqy
 * @date on 2018/8/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class DistributionTitle implements Parcelable {

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

    public DistributionTitle() {
    }

    protected DistributionTitle(Parcel in) {
        this.title = in.readString();
        this.type = in.readString();
    }

    public static final Creator<DistributionTitle> CREATOR = new Creator<DistributionTitle>() {
        public DistributionTitle createFromParcel(Parcel source) {
            return new DistributionTitle(source);
        }

        public DistributionTitle[] newArray(int size) {
            return new DistributionTitle[size];
        }
    };
}

