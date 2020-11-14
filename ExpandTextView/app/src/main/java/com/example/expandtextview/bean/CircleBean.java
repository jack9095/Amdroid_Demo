package com.example.expandtextview.bean;

import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: njb
 * @时间: 2019/12/30 16:05
 * @描述:
 */
public class CircleBean {
    private int code;
    private String message;
    private List<DataBean> data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }



    public static class DataBean implements com.chad.library.adapter.base.entity.MultiItemEntity {

        private String id;
        private String user_id;
        private int type;
        private String content;
        private String share_title;
        private String share_image;
        private String share_url;
        private String longitude;
        private String latitude;
        private String position;
        private String is_del;
        private String deleteon;
        private String createon;
        private String user_name;
        private String head_img;
        private int is_like;
        public List<String> files;
        public List<CommentListBean> comments_list;
        public List<LikeListBean> like_list;
        private String video;
        public boolean isShowAll = false;
        private boolean isExpanded;
        private boolean isShowCheckAll;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            if (TextUtils.isEmpty(content)) {
                return "";
            }
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getShare_title() {
            return share_title;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getShare_image() {
            return share_image;
        }

        public void setShare_image(String share_image) {
            this.share_image = share_image;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getIs_del() {
            return is_del;
        }

        public void setIs_del(String is_del) {
            this.is_del = is_del;
        }

        public String getDeleteon() {
            return deleteon;
        }

        public void setDeleteon(String deleteon) {
            this.deleteon = deleteon;
        }

        public String getCreateon() {
            return createon;
        }

        public void setCreateon(String createon) {
            this.createon = createon;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public int getIs_like() {
            return is_like;
        }

        public void setIs_like(int is_like) {
            this.is_like = is_like;
        }

        public List<String> getFiles() {
            return files;
        }

        public List<Uri> getImageUriList() {
            List<Uri> imageUriList = new ArrayList<>();
            if (files != null && files.size() > 0) {
                for (String str : files) {
                    imageUriList.add(Uri.parse(str));
                }
            }
            return imageUriList;
        }

        public void setFiles(List<String> files) {
            this.files = files;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public List<CommentListBean> getComments_list() {
            return comments_list;
        }

        public void setComments_list(List<CommentListBean> comments_list) {
            this.comments_list = comments_list;
        }

        public List<LikeListBean> getLike_list() {
            return like_list;
        }

        public void setLike_list(List<LikeListBean> like_list) {
            this.like_list = like_list;
        }

        public boolean isShowAll() {
            return isShowAll;
        }

        public void setShowAll(boolean showAll) {
            isShowAll = showAll;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }

        public boolean isShowCheckAll() {
            return isShowCheckAll;
        }

        public void setShowCheckAll(boolean showCheckAll) {
            isShowCheckAll = showCheckAll;
        }

        @Override
        public int getItemType() {
            return getType();
        }
    }
}
