package com.kuanquan.customplayer.library;


/**
 * 视频 URL
 * 填写视频 URL, 如需使用直播时移功能，还需填写appId
 */
public class SuperPlayerModel {


    /**
     * ------------------------------------------------------------------
     * 直接使用URL播放
     * <p>
     * 支持 RTMP、FLV、MP4、HLS 封装格式
     * 使用腾讯云直播时移功能则需要填写appId
     * ------------------------------------------------------------------
     */
    public String url = "";      // 视频URL

    public String title = "";    // 视频文件名 （用于显示在UI层);使用file id播放，若未指定title，则使用FileId返回的Title；使用url播放需要指定title，否则title显示为空
}
