package com.kuanquan.activityoptions;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageBean implements Parcelable {

    private String coverUrl;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "coverUrl='" + coverUrl + '\'' +
                '}';
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {dest.writeString(this.coverUrl);}

    public ImageBean() {}

    protected ImageBean(Parcel in) {this.coverUrl = in.readString();}

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel source) {return new ImageBean(source);}

        @Override
        public ImageBean[] newArray(int size) {return new ImageBean[size];}
    };
}
