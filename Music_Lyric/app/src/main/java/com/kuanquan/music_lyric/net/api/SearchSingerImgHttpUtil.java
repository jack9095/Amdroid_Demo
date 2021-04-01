package com.kuanquan.music_lyric.net.api;

import android.content.Context;

import com.kuanquan.music_lyric.application.HPApplication;
import com.kuanquan.music_lyric.libs.utils.NetUtil;
import com.kuanquan.music_lyric.net.HttpClientUtils;
import com.kuanquan.music_lyric.net.entity.SearchSingerImgResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 搜索歌手头像
 */
public class SearchSingerImgHttpUtil {


    /**
     * 获取歌手头像
     *
     * @param context
     * @param singerName
     * @return
     */
    public static SearchSingerImgResult searchSingerImg(HPApplication hPApplication, Context context, String singerName) {

        if (!NetUtil.isNetworkAvailable(context)) {

            return null;
        }

//
        if (hPApplication.isWifi()) {
            if (!NetUtil.isWifi(context)) {


                return null;
            }
        }
        try {

            String url = "http://mobilecdn.kugou.com/new/app/i/yueku.php";
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("singer", singerName);
            params.put("size", "400");
            params.put("cmd", "104");
            params.put("type", "softhead");
            // 获取数据
            String result = HttpClientUtils.httpGetRequest(url, params);
            if (result != null) {

                JSONObject jsonNode = new JSONObject(result);
                int status = jsonNode.getInt("status");
                if (status == 1) {


                    SearchSingerImgResult singerImgResult = new SearchSingerImgResult();
                    singerImgResult.setSinger(jsonNode.getString("singer"));
                    singerImgResult.setImgUrl(jsonNode.getString("url"));


                    return singerImgResult;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
