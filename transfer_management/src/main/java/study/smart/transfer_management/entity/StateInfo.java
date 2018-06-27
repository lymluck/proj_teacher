package study.smart.transfer_management.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/26
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StateInfo implements Parcelable {

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

    public StateInfo() {
    }

    protected StateInfo(Parcel in) {
        this.title = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<StateInfo> CREATOR = new Parcelable.Creator<StateInfo>() {
        public StateInfo createFromParcel(Parcel source) {
            return new StateInfo(source);
        }

        public StateInfo[] newArray(int size) {
            return new StateInfo[size];
        }
    };
}
