package com.smartstudy.counselor_t.entity;

import android.net.Uri;
import android.os.Parcel;

import io.rong.common.ParcelUtils;
import io.rong.imlib.model.UserInfo;

/**
 * @author yqy
 * @date on 2018/1/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyUserInfo extends UserInfo {

    private String admissionTime;
    private String targetCountry;
    private String targetDegree;

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public MyUserInfo createFromParcel(Parcel source) {
            return new MyUserInfo(source);
        }

        @Override
        public MyUserInfo[] newArray(int size) {
            return new MyUserInfo[size];
        }
    };

    public MyUserInfo(String id, String name, Uri portraitUri) {
        super(id, name, portraitUri);

    }

    public MyUserInfo(Parcel in) {
        super(in);
        this.setAdmissionTime(ParcelUtils.readFromParcel(in));
        this.setTargetCountry(ParcelUtils.readFromParcel(in));
        this.setTargetDegree(ParcelUtils.readFromParcel(in));
    }


    public MyUserInfo(String id, String name, Uri portraitUri, String admissionTime, String targetCountry, String targetDegree) {
        super(id, name, portraitUri);
        this.admissionTime = admissionTime;
        this.targetCountry = targetCountry;
        this.targetDegree = targetDegree;
    }

    public String getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(String admissionTime) {
        this.admissionTime = admissionTime;
    }

    public String getTargetCountry() {
        return targetCountry;
    }

    public void setTargetCountry(String targetCountry) {
        this.targetCountry = targetCountry;
    }

    public String getTargetDegree() {
        return targetDegree;
    }

    public void setTargetDegree(String targetDegree) {
        this.targetDegree = targetDegree;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        ParcelUtils.writeToParcel(dest, this.getAdmissionTime());
        ParcelUtils.writeToParcel(dest, this.getTargetCountry());
        ParcelUtils.writeToParcel(dest, this.getTargetDegree());
    }


}
