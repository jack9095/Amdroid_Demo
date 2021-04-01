package com.kuanquan.music_lyric.utils;

import android.content.Context;
import android.content.res.AssetManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GsonUtils {
    private static Gson gson = null;

    public Gson getInstance() {
        if (gson == null) gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        return gson;
    }

    public static <T> T toObject(String json, Class clazz) {
        try {
            return (T) new GsonUtils().getInstance().fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(String json, Type type) {
        try {
            return (T) new GsonUtils().getInstance().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Map map) {
        return new GsonUtils().getInstance().toJson(map);
    }

    public static List<?> toList(String json, Type type) {
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json, type);
        return list;
    }

    public static String toJson(Object obj) {
        Gson gson = new Gson();
        if (obj == null) {
            return "";
        } else {
            return gson.toJson(obj);
        }
    }

    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    public static <T> T fromJsonType(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    public static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
//            LogUtil.e("开始解析异常");
        }
        return stringBuilder.toString();
    }
}
