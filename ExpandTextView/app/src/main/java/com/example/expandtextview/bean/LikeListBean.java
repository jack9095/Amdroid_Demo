package com.example.expandtextview.bean;

import java.io.Serializable;

/**
 * @作者: njb
 * @时间: 2019/7/16 18:56
 * @描述: 点赞信息实体类
 */
public class LikeListBean implements Serializable {
    private String user_id;
    private String user_name;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "LikeListBean{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
