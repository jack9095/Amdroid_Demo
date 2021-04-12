package com.kuanquan.demo;

import android.os.Parcel;
import android.os.Parcelable;

import com.kuanquan.demo.enum_p.MediaType;

/**
 * Created by huzongyao on 2018/6/29.
 */

public class MediaInfo implements Parcelable {

    public MediaType mediaType; // 投屏过来的类型 一般有视频、音频、图片等等，这里主要是视屏
    public String title;  // 视屏标题
    public String url;    // 视屏播放链接
    public String albumArtURI; // 专辑图片链接

    public MediaInfo() {
    }

    protected MediaInfo(Parcel in) {
        title = in.readString();
        url = in.readString();
        albumArtURI = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(albumArtURI);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaInfo> CREATOR = new Creator<MediaInfo>() {
        @Override
        public MediaInfo createFromParcel(Parcel in) {
            return new MediaInfo(in);
        }

        @Override
        public MediaInfo[] newArray(int size) {
            return new MediaInfo[size];
        }
    };

    @Override
    public String toString() {
        return "MediaInfo{" +
                "mediaType=" + mediaType +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", albumArtURI='" + albumArtURI + '\'' +
                '}';
    }
}
