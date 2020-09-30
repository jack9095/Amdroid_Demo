package com.kuanquan.weixinedit.model;

import java.util.List;

public class ProviderMultiEntity {
    public static final int TYPE_TEXT = 0;   //文本
    public static final int TYPE_IMAGE = 1; //图片

    /**
     * item 类型
     */
    public int type;
    /**
     * 内容
     */
    public String content;
    /**
     * 名称
     */
    public String name;
    /**
     * 评论时间
     */
    public String createon;
    /**
     * 图片集合
     */
    public List<String> images;

}
